package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.visitor.ContainerNode;
import com.github.fridujo.markdown.junit.engine.visitor.RunnableNode;
import com.github.fridujo.markdown.junit.engine.visitor.TestNode;
import org.junit.platform.engine.UniqueId;

class MarkdownNodeDescriptorFactory {

    static AbstractMarkdownTestDescriptor buildDescriptor(UniqueId parentUniqueId, TestNode testNode) {
        if (testNode.type() == TestNode.Type.TEST) {
            return new MarkdownRunnableDescriptor(parentUniqueId, (RunnableNode) testNode);
        } else {
            return new MarkdownContainerDescriptor(parentUniqueId, (ContainerNode) testNode);
        }
    }
}
