package com.github.fridujo.markdown.junit.engine.visitor;

import java.nio.file.Path;

@FunctionalInterface
public interface MarkdownVisitorFactory {

    MarkdownVisitor createVisitor(Path markdownFilePath);
}
