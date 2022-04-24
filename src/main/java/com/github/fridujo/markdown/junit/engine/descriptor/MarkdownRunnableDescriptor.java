package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.MarkdownExecutionContext;
import com.github.fridujo.markdown.junit.engine.visitor.NamedRunnable;
import org.junit.platform.engine.UniqueId;

public class MarkdownRunnableDescriptor extends AbstractMarkdownTestDescriptor {
    public static final String SEGMENT_TYPE = "runnable";
    private final Runnable runnable;

    MarkdownRunnableDescriptor(UniqueId parentUniqueId, NamedRunnable testNode) {
        super(parentUniqueId.append(SEGMENT_TYPE, testNode.name()), testNode.name());
        this.runnable = testNode.runnable();
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }

    @Override
    public MarkdownExecutionContext execute(MarkdownExecutionContext context, DynamicTestExecutor dynamicTestExecutor) {
        runnable.run();
        return context;
    }
}
