package com.github.fridujo.markdown.junit.engine.visitor;

public interface TestNode {

    String name();

    Type type();

    enum Type {
        CONTAINER,
        TEST,
    }

    interface Builder {
        TestNode build();

        Type type();

        String name();
    }
}
