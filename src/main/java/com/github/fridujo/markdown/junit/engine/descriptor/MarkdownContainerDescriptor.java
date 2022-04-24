package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.visitor.ContainerNode;
import org.junit.platform.engine.UniqueId;

class MarkdownContainerDescriptor extends AbstractMarkdownTestDescriptor {
    public static final String SEGMENT_TYPE = "container";

    MarkdownContainerDescriptor(UniqueId parentUniqueId, ContainerNode testNode) {
        super(parentUniqueId.append(SEGMENT_TYPE, testNode.name()), testNode.name());
        testNode.children()
            .stream()
            .map(childNode -> MarkdownNodeDescriptorFactory.buildDescriptor(getUniqueId(), childNode))
            .forEach(this::addChild);
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }
}
