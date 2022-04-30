package com.github.fridujo.markdown.junit.engine;

public interface MarkdownTestEngineProperties {

    String ENGINE_ID = "markdown";
    String ENGINE_DISPLAY_NAME = "Markdown Engine";
    String GROUP_ID = "com.github.fridujo";
    String ARTIFACT_ID = "markdown-junit-engine";

    String DISABLED_PROPERTY_NAME = ARTIFACT_ID + ".disabled";
    String MARKDOWN_FILES_ROOT_PATH_PROPERTY_NAME = ARTIFACT_ID + ".markdown-files-root-path";
    String FAIL_ON_MISSING_ROOT_PATH_PROPERTY_NAME = ARTIFACT_ID + ".fail-on-missing-root-path";
    String MARKDOWN_VISITOR_FACTORY_CLASS_PROPERTY_NAME = ARTIFACT_ID + ".markdown-visitor-factory-class";
}
