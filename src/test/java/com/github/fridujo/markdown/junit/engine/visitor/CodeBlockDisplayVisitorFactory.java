package com.github.fridujo.markdown.junit.engine.visitor;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CodeBlockDisplayVisitorFactory implements MarkdownVisitorFactory {
    @Override
    public MarkdownVisitor createVisitor(Path markdownFilePath) {
        return new CodeBlockDisplayVisitor();
    }

    private static class CodeBlockDisplayVisitor extends AbstractVisitor implements MarkdownVisitor {

        private final List<TestNode> testNodes = new ArrayList<>();
        private int blockSequence = 0;

        @Override
        public void visit(FencedCodeBlock fencedCodeBlock) {
            blockSequence++;
            if (!"java".equals(fencedCodeBlock.getInfo())) {
                return;
            }
            testNodes.add(new RunnableNode("Block#" + blockSequence, () -> {
                System.out.println(fencedCodeBlock.getLiteral().trim());
            }));
        }

        @Override
        public Collection<TestNode> getCollectedTestNode() {
            return testNodes;
        }
    }
}
