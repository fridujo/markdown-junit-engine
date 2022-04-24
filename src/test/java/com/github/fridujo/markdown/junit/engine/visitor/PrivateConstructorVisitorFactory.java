package com.github.fridujo.markdown.junit.engine.visitor;

import java.nio.file.Path;

public class PrivateConstructorVisitorFactory implements MarkdownVisitorFactory {

    private PrivateConstructorVisitorFactory() {

    }

    @Override
    public MarkdownVisitor createVisitor(Path markdownFilePath) {
        return null;
    }
}
