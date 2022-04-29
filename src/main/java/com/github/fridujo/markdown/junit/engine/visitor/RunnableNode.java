package com.github.fridujo.markdown.junit.engine.visitor;

public record RunnableNode(String name, Runnable runnable) implements TestNode {

    @Override
    public Type type() {
        return Type.TEST;
    }

    public record Builder(String name, Runnable runnable) implements TestNode.Builder {

        @Override
            public RunnableNode build() {
                return new RunnableNode(name, runnable);
            }
        }
}
