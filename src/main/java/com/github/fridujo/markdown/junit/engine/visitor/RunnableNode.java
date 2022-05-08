package com.github.fridujo.markdown.junit.engine.visitor;

public class RunnableNode implements TestNode {

    private final String name;
    private final Runnable runnable;

    public RunnableNode(String name, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Type type() {
        return Type.TEST;
    }

    public Runnable runnable() {
        return runnable;
    }

    public static class Builder implements TestNode.Builder {

        private final String name;
        private final Runnable runnable;

        public Builder(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        @Override
        public RunnableNode build() {
            return new RunnableNode(name, runnable);
        }

        @Override
        public Type type() {
            return Type.TEST;
        }

        @Override
        public String name() {
            return name;
        }
    }
}
