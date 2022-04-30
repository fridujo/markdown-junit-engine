package com.github.fridujo.markdown.junit.engine.descriptor;

import com.github.fridujo.markdown.junit.engine.MarkdownExecutionContext;
import org.junit.platform.engine.UniqueId;

public class FailingTestDescriptor extends AbstractMarkdownTestDescriptor {

    public static final String SEGMENT_TYPE = "failure";

    public FailingTestDescriptor(UniqueId parentUniqueId, String error) {
        super(parentUniqueId.append(SEGMENT_TYPE, SEGMENT_TYPE), error);
    }

    @Override
    public MarkdownExecutionContext execute(MarkdownExecutionContext context, DynamicTestExecutor dynamicTestExecutor) {
        throw new IllegalArgumentException(getDisplayName());
    }

    @Override
    public Type getType() {
        return Type.TEST;
    }
}
