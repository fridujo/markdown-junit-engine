package com.github.fridujo.markdown.junit.engine.visitor;

import org.commonmark.node.Visitor;

import java.util.Collection;

public interface MarkdownVisitor extends Visitor {

    Collection<TestNode> getCollectedTestNode();
}
