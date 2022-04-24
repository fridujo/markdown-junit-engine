package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.MarkdownExecutionContext;
import com.github.fridujo.markdown.junit.engine.MarkdownTestEngineProperties;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

public class MarkdownEngineDescriptor extends EngineDescriptor implements Node<MarkdownExecutionContext> {

    public MarkdownEngineDescriptor(UniqueId uniqueId) {
        super(uniqueId, MarkdownTestEngineProperties.ENGINE_DISPLAY_NAME);
    }
}
