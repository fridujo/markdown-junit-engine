package com.github.fridujo.markdown.junit.engine.visitor;

public record NamedRunnable(String name, Runnable runnable) implements TestNode {

    @Override
    public Type type() {
        return Type.TEST;
    }

    public record Builder(String name, Runnable runnable) implements TestNode.Builder {

        @Override
            public NamedRunnable build() {
                return new NamedRunnable(name, runnable);
            }
        }
}
