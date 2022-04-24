package com.github.fridujo.markdown.junit.engine.support;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryDiagnosticListener implements DiagnosticListener<JavaFileObject> {

    private final List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.add(diagnostic);
    }

    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        return Collections.unmodifiableList(diagnostics);
    }
}
