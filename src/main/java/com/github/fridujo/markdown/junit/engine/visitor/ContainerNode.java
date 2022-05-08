package com.github.fridujo.markdown.junit.engine.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerNode implements TestNode {

    private final String name;
    private final Collection<TestNode> children;

    public ContainerNode(String name, Collection<TestNode> children) {
        this.name = name;
        this.children = Collections.unmodifiableList(new ArrayList<>(children));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public TestNode.Type type() {
        return TestNode.Type.CONTAINER;
    }

    public Collection<TestNode> children() {
        return children;
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
            List<TestNode> builtChildren = children.stream()
                .map(TestNode.Builder::build)
                .filter(tn -> !(tn.type() == Type.CONTAINER && ((ContainerNode) tn).children.isEmpty()))
                .collect(Collectors.toList());
            return new ContainerNode(name, builtChildren);
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
