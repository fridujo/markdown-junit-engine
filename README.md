# Markdown JUnit Engine
JUnit Engine for Markdown-based tests.

![build](https://github.com/ledoyen/markdown-junit-engine/actions/workflows/build.yml/badge.svg)
[![codecov](https://codecov.io/gh/ledoyen/markdown-junit-engine/branch/main/graph/badge.svg?token=TJHCFMJBBK)](https://codecov.io/gh/ledoyen/markdown-junit-engine)
[![License](https://img.shields.io/github/license/fridujo/spring-automocker.svg)](https://opensource.org/licenses/Apache-2.0)

## Default behavior

### Markdown sources
By default, the engine will scan for markdown files within `doc/`.  
To override this, use the `markdown-junit-engine.markdown-files-root-path` property.

### Translation from Markdown to tests
By default, the engine will use the `com.github.fridujo.markdown.junit.engine.visitor.provided.CodeBlockCompilerVisitor` which compiles all code blocks with the **Content-Type** `java`.  
To specify a custom `MarkdownVisitorFactory`, use the `markdown-junit-engine.markdown-visitor-factory-class` property.

The provided factory must return an implementation of `MarkdownVisitor` which will
* First, be notified of each Markdown AST element
* Then, queried for `TestNode` to process as part of the JUnit execution tree

A `TestNode` can either be a `ContainerNode` or a `NamedRunnable`.
