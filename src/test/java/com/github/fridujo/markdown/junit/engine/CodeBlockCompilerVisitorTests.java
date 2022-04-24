package com.github.fridujo.markdown.junit.engine;

import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.nio.file.Paths;

class CodeBlockCompilerVisitorTests {

    @Test
    void verifyJupiterContainerStats() {
        EngineExecutionResults results = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("src/test/resources/markdown-files").toString())
            .execute();

        results
            .containerEvents()
            .assertStatistics(stats -> stats.started(7).succeeded(7));

        results
            .testEvents()
            .assertStatistics(stats -> stats.started(6).succeeded(4).failed(2));
    }
}
