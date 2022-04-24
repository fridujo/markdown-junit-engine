package com.github.fridujo.markdown.junit.engine;

import com.github.fridujo.markdown.junit.engine.descriptor.MarkdownEngineDescriptor;
import com.github.fridujo.markdown.junit.engine.discovery.DiscoverySelectorResolver;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

import java.util.Optional;

public class MarkdownTestEngine extends HierarchicalTestEngine<MarkdownExecutionContext> {

    @Override
    public String getId() {
        return MarkdownTestEngineProperties.ENGINE_ID;
    }

    @Override
    public Optional<String> getGroupId() {
        return Optional.of(MarkdownTestEngineProperties.GROUP_ID);
    }

    @Override
    public Optional<String> getArtifactId() {
        return Optional.of(MarkdownTestEngineProperties.ARTIFACT_ID);
    }

    @Override
    protected MarkdownExecutionContext createExecutionContext(ExecutionRequest executionRequest) {
        return new MarkdownExecutionContext();
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest request, UniqueId uniqueId) {
        TestDescriptor engineDescriptor = new MarkdownEngineDescriptor(uniqueId);

        new DiscoverySelectorResolver(request).resolveFor(engineDescriptor);

        return engineDescriptor;
    }
}
