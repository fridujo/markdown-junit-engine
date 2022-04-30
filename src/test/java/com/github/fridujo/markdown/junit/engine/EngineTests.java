package com.github.fridujo.markdown.junit.engine;

import com.github.fridujo.markdown.junit.engine.visitor.CodeBlockDisplayVisitorFactory;
import com.github.fridujo.markdown.junit.engine.visitor.PrivateConstructorVisitorFactory;
import com.github.fridujo.markdown.junit.engine.visitor.TestVisitorFactory;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.EventType;

import java.nio.file.Paths;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;

public class EngineTests {

    @Test
    void engine_should_execute_nodes_given_by_visitor() {
        EngineExecutionResults results = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("src/test/resources/markdown-files/hello.md").toString())
            .configurationParameter("markdown-junit-engine.markdown-visitor-factory-class", TestVisitorFactory.class.getName()).execute();

        results.allEvents().assertThatEvents()
            .extracting(
                e -> e.getType(),
                e -> e.getTestDescriptor().toString(),
                e -> e.getPayload().map(TestExecutionResult.class::cast).map(ter -> ter.getStatus())
            )
            .containsExactly(
                tuple(EventType.STARTED, "MarkdownEngineDescriptor: [engine:markdown]", empty()),
                tuple(EventType.STARTED, "MarkdownFileDescriptor: [engine:markdown]/[file:hello.md]", empty()),
                tuple(EventType.STARTED, "MarkdownRunnableDescriptor: [engine:markdown]/[file:hello.md]/[runnable:Test1]", empty()),
                tuple(EventType.FINISHED, "MarkdownRunnableDescriptor: [engine:markdown]/[file:hello.md]/[runnable:Test1]", of(TestExecutionResult.Status.SUCCESSFUL)),
                tuple(EventType.STARTED, "MarkdownContainerDescriptor: [engine:markdown]/[file:hello.md]/[container:Container2]", empty()),
                tuple(EventType.STARTED, "MarkdownRunnableDescriptor: [engine:markdown]/[file:hello.md]/[container:Container2]/[runnable:Test2.1]", empty()),
                tuple(EventType.FINISHED, "MarkdownRunnableDescriptor: [engine:markdown]/[file:hello.md]/[container:Container2]/[runnable:Test2.1]", of(TestExecutionResult.Status.FAILED)),
                tuple(EventType.STARTED, "MarkdownRunnableDescriptor: [engine:markdown]/[file:hello.md]/[container:Container2]/[runnable:Test2.2]", empty()),
                tuple(EventType.FINISHED, "MarkdownRunnableDescriptor: [engine:markdown]/[file:hello.md]/[container:Container2]/[runnable:Test2.2]", of(TestExecutionResult.Status.SUCCESSFUL)),
                tuple(EventType.FINISHED, "MarkdownContainerDescriptor: [engine:markdown]/[file:hello.md]/[container:Container2]", of(TestExecutionResult.Status.SUCCESSFUL)),
                tuple(EventType.FINISHED, "MarkdownFileDescriptor: [engine:markdown]/[file:hello.md]", of(TestExecutionResult.Status.SUCCESSFUL)),
                tuple(EventType.FINISHED, "MarkdownEngineDescriptor: [engine:markdown]", of(TestExecutionResult.Status.SUCCESSFUL))
            );
    }

    @Test
    void engine_executes_nothing_when_root_path_does_not_exist_and_fail_on_missing_root_path_is_disabled() {
        EngineExecutionResults results = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("non-existing-folder").toString())
            .configurationParameter("markdown-junit-engine.fail-on-missing-root-path", "false")
            .execute();

        results.testEvents().assertThatEvents().isEmpty();
    }

    @Test
    void engine_produce_on_failing_test_when_root_path_does_not_exist() {
        EngineExecutionResults results = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("non-existing-folder").toString())
            .execute();

        results.testEvents().assertStatistics(
            e -> e.started(1).failed(1)
        );
    }

    @Test
    void engine_should_discover_all_markdown_files() {
        EngineExecutionResults results = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("src/test/resources/markdown-files").toString())
            .configurationParameter("markdown-junit-engine.markdown-visitor-factory-class", CodeBlockDisplayVisitorFactory.class.getName()).execute();

        results.testEvents().assertStatistics(
            e -> e.started(6).succeeded(6)
        );
    }

    @Test
    void engine_fails_if_given_factory_class_does_not_exist() {
        EngineTestKit.Builder builder = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("src/test/resources/markdown-files").toString())
            .configurationParameter("markdown-junit-engine.markdown-visitor-factory-class", "UnexistingClass");

        assertThatExceptionOfType(JUnitException.class)
            .isThrownBy(() -> builder.execute());
    }

    @Test
    void engine_fails_if_given_factory_class_does_not_implement_visitor_factory() {
        EngineTestKit.Builder builder = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("src/test/resources/markdown-files").toString())
            .configurationParameter("markdown-junit-engine.markdown-visitor-factory-class", "java.lang.String");

        assertThatExceptionOfType(JUnitException.class)
            .isThrownBy(() -> builder.execute());
    }

    @Test
    void engine_fails_if_given_factory_class_does_not_have_a_usable_no_arg_constructor() {
        EngineTestKit.Builder builder = EngineTestKit
            .engine("markdown")
            .configurationParameter("markdown-junit-engine.disabled", "false") // override as by default engine is disabled in IDE
            .configurationParameter("markdown-junit-engine.markdown-files-root-path", Paths.get("src/test/resources/markdown-files").toString())
            .configurationParameter("markdown-junit-engine.markdown-visitor-factory-class", PrivateConstructorVisitorFactory.class.getName());

        assertThatExceptionOfType(JUnitException.class)
            .isThrownBy(() -> builder.execute());
    }
}
