package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.visitor.MarkdownVisitor;
import com.github.fridujo.markdown.junit.engine.visitor.MarkdownVisitorFactory;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.platform.engine.UniqueId;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownFileDescriptor extends AbstractMarkdownTestDescriptor {

    public static final String SEGMENT_TYPE = "file";

    public MarkdownFileDescriptor(UniqueId parentUniqueId, Path path, MarkdownVisitorFactory markdownVisitorFactory) {
        super(parentUniqueId.append(SEGMENT_TYPE, path.getFileName().toString()),
            path.getFileName().toString());

        Node document = parseMarkdown(path);

        MarkdownVisitor markdownVisitor = markdownVisitorFactory.createVisitor(path);
        document.accept(markdownVisitor);

        markdownVisitor.getCollectedTestNode()
            .stream()
            .map(testNode -> MarkdownNodeDescriptorFactory.buildDescriptor(getUniqueId(), testNode))
            .forEach(this::addChild);
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    private Node parseMarkdown(Path path) {
        Parser parser = Parser.builder().build();
        try (Reader reader = Files.newBufferedReader(path)) {
            return parser.parseReader(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
