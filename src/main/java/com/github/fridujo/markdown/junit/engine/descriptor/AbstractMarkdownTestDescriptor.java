package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.MarkdownExecutionContext;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

public abstract class AbstractMarkdownTestDescriptor extends AbstractTestDescriptor implements Node<MarkdownExecutionContext> {
    protected AbstractMarkdownTestDescriptor(UniqueId uniqueId, String displayName) {
        super(uniqueId, displayName);
    }
}
