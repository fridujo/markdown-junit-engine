package com.github.fridujo.markdown.junit.engine.visitor;

import org.commonmark.node.AbstractVisitor;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class TestVisitorFactory implements MarkdownVisitorFactory {

    @Override
    public MarkdownVisitor createVisitor(Path markdownFilePath) {
        return new TestMarkdownVisitor();
    }

    private static class TestMarkdownVisitor extends AbstractVisitor implements MarkdownVisitor {

        @Override
        public Collection<TestNode> getCollectedTestNode() {
            return List.of(new RunnableNode("Test1", () -> {
            }), new ContainerNode("Container2", List.of(new RunnableNode("Test2.1", () -> {
                throw new RuntimeException("failure");
            }), new RunnableNode("Test2.2", () -> {
            }))));
        }
    }
}

