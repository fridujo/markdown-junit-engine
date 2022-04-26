package com.github.fridujo.markdown.junit.engine.discovery;

import com.github.fridujo.markdown.junit.engine.MarkdownTestEngineProperties;
import com.github.fridujo.markdown.junit.engine.descriptor.MarkdownFileDescriptor;
import com.github.fridujo.markdown.junit.engine.visitor.MarkdownVisitorFactory;
import com.github.fridujo.markdown.junit.engine.visitor.provided.CodeBlockCompilerVisitor;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.discovery.ClassSelector;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DiscoverySelectorResolver {

    private final Logger logger = LoggerFactory.getLogger(DiscoverySelectorResolver.class);
    private final boolean uniqueClassSelector;
    private final boolean disableEngine;
    private final Path markdownFilesRootPath;
    private final MarkdownVisitorFactory markdownVisitorFactory;

    public DiscoverySelectorResolver(EngineDiscoveryRequest request) {
        uniqueClassSelector = request.getSelectorsByType(ClassSelector.class).size() == 1;
        disableEngine = request
            .getConfigurationParameters()
            .get(MarkdownTestEngineProperties.DISABLED_PROPERTY_NAME)
            .map(Boolean::parseBoolean)
            .orElseGet(this::isRunningInIde);

        markdownFilesRootPath = request
            .getConfigurationParameters()
            .get(MarkdownTestEngineProperties.MARKDOWN_FILES_ROOT_PATH_PROPERTY_NAME)
            .map(Paths::get)
            .orElseGet(() -> Paths.get("doc")).toAbsolutePath();

        markdownVisitorFactory = request
            .getConfigurationParameters()
            .get(MarkdownTestEngineProperties.MARKDOWN_VISITOR_FACTORY_CLASS_PROPERTY_NAME)
            .map(this::instantiate)
            .orElse(CodeBlockCompilerVisitor.FACTORY);
    }

    public void resolveFor(TestDescriptor testDescriptor) {
        if(uniqueClassSelector) {
            // This is the behavior of the Surefire Maven plugin to call the discovery phase of each engine for each Java test file it detects
            // These invocations are not relevant as they will not lead to nay execution of the current engine
            return;
        }
        if (disableEngine) {
            logger.info(() -> MarkdownTestEngineProperties.ENGINE_DISPLAY_NAME + " disabled");
            return;
        }
        if (!Files.exists(markdownFilesRootPath)) {
            logger.error(() -> "Markdown files root path " + markdownFilesRootPath + " does not exist");
            return;
        }

        if (!Files.isDirectory(markdownFilesRootPath)) {
            testDescriptor.addChild(new MarkdownFileDescriptor(testDescriptor.getUniqueId(), markdownFilesRootPath, markdownVisitorFactory));
        } else {
            try (Stream<Path> walk = Files.walk(markdownFilesRootPath)) {
                walk.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".md"))
                    .map(path -> new MarkdownFileDescriptor(testDescriptor.getUniqueId(), path, markdownVisitorFactory))
                    .forEach(testDescriptor::addChild);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private MarkdownVisitorFactory instantiate(String className) {
        try {
            Class<?> visitorClass = Class.forName(className);
            if (!MarkdownVisitorFactory.class.isAssignableFrom(visitorClass)) {
                throw new RuntimeException("Given visitor factory class " + className + " does not implement " + MarkdownVisitorFactory.class.getName());
            }
            try {
                return (MarkdownVisitorFactory) visitorClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Given visitor factory class " + className + " no-arg constructor cannot be used", e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Given visitor factory class " + className + " does not exist");
        }
    }

    private boolean isRunningInIde() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; i--) {
            if (stackTrace[i].getClassName().startsWith("com.intellij")) {
                return true;
            }
        }
        return false;
    }
}
