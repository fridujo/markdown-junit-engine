package com.github.fridujo.markdown.junit.engine.visitor.provided;

import com.github.fridujo.markdown.junit.engine.support.SourceMemoryJavaFileObject;
import com.github.fridujo.markdown.junit.engine.visitor.*;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Heading;
import org.commonmark.node.Text;
import org.opentest4j.AssertionFailedError;

import javax.tools.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CodeBlockCompilerVisitor extends AbstractVisitor implements MarkdownVisitor {

    public static final MarkdownVisitorFactory FACTORY = CodeBlockCompilerVisitor::new;

    private final Deque<PathElement> nodePath = new ArrayDeque<>();
    private final Collection<TestNode.Builder> testNodes = new ArrayList<>();
    private final Path markdownFilePath;

    private int codeBlockSequence = 0;

    public CodeBlockCompilerVisitor(Path markdownFilePath) {
        this.markdownFilePath = markdownFilePath;
    }

    @Override
    public void visit(Heading heading) {
        codeBlockSequence = 0;
        int level = heading.getLevel();
        String headingText = ((Text) heading.getFirstChild()).getLiteral();
        if (level > previousHeadingLevel()) {
            addNewContainer(level, headingText);
        } else {
            while (level < previousHeadingLevel()) {
                nodePath.pop();
            }
            addNewContainer(level, headingText);
        }
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        if (!"java".equals(fencedCodeBlock.getInfo())) {
            return;
        }
        codeBlockSequence++;
        PathElement parentNode = nodePath.peekFirst();
        RunnableNode.Builder runnableNode = new RunnableNode.Builder("Block#" + codeBlockSequence, new SourceCompilerRunnable(markdownFilePath, fencedCodeBlock.getLiteral().trim()));
        if (parentNode == null) {
            testNodes.add(runnableNode);
        } else {
            parentNode.node.addChild(runnableNode);
        }
    }

    @Override
    public Collection<TestNode> getCollectedTestNode() {
        return testNodes.stream().map(TestNode.Builder::build).collect(Collectors.toList());
    }

    private void addNewContainer(int level, String headingText) {
        ContainerNode.Builder newContainer = new ContainerNode.Builder(headingText);
        PathElement parentNode = nodePath.peekFirst();
        if (parentNode == null) {
            testNodes.add(newContainer);
        } else {
            parentNode.node().addChild(newContainer);
        }
        nodePath.push(new PathElement(level, newContainer));
    }

    private int previousHeadingLevel() {
        PathElement parentNode = nodePath.peekFirst();
        return parentNode != null ? parentNode.level : 0;
    }

    private static class PathElement {
        private final int level;
        private final ContainerNode.Builder node;

        private PathElement(int level, ContainerNode.Builder node) {
            this.level = level;
            this.node = node;
        }


        public ContainerNode.Builder node() {
            return node;
        }
    }

    private static class SourceCompilerRunnable implements Runnable {

        private final Path markdownFilePath;
        private final String source;

        private SourceCompilerRunnable(Path markdownFilePath, String source) {
            this.markdownFilePath = markdownFilePath;
            this.source = source;
        }

        @Override
        public void run() {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);

            JavaFileObject javaFileObject = new SourceMemoryJavaFileObject(markdownFilePath.toString(), source);

            Path targetDirectory = Paths.get("").resolve("target");
            Path containingFolder = targetDirectory.resolve("generated-markdown-classes");
            createDirectoryIfNeeded(containingFolder);
            List<String> options = Arrays.asList("-d", containingFolder.toString());

            DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<>();
            if (!compiler.getTask(null, fileManager, diagnosticListener, options, null, Collections.singleton(javaFileObject)).call()) {
                throw new AssertionFailedError("Compilation failed\n\n" + diagnosticListener.getDiagnostics().stream().map(Object::toString).collect(Collectors.joining("\n")));
            }
        }

        /**
         * Target directory is not created by old versions of the Java Compiler.
         */
        private void createDirectoryIfNeeded(Path containingFolder) {
            if (!Files.exists(containingFolder)) {
                try {
                    Files.createDirectory(containingFolder);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
    }
}
