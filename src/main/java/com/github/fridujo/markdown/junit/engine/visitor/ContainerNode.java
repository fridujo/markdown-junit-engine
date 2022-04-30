package com.github.fridujo.markdown.junit.engine.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record ContainerNode(String name, Collection<TestNode> children) implements TestNode {

    public ContainerNode(String name, Collection<TestNode> children) {
        this.name = name;
        this.children = List.copyOf(children);
    }

    @Override
    public TestNode.Type type() {
        return TestNode.Type.CONTAINER;
    }

    public static class Builder implements TestNode.Builder {
        private final String name;
        private final Collection<TestNode.Builder> children = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder addChild(TestNode.Builder testNode) {
            children.add(testNode);
            return this;
        }

        @Override
        public ContainerNode build() {
            return new ContainerNode(name, children.stream().map(TestNode.Builder::build).collect(Collectors.toList()));
        }

        @Override
        public Type type() {
            return TestNode.Type.CONTAINER;
        }

        @Override
        public String name() {
            return name;
        }
    }
}
