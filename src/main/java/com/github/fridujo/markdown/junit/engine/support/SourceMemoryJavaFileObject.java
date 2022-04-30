package com.github.fridujo.markdown.junit.engine.support;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class SourceMemoryJavaFileObject extends SimpleJavaFileObject {

    private static final Logger logger = LoggerFactory.getLogger(SourceMemoryJavaFileObject.class);
    private final String src;

    public SourceMemoryJavaFileObject(String prefix, String src) {
        super(buildUri(prefix.replace('\\', '/'), src), JavaFileObject.Kind.SOURCE);
        this.src = src;
    }

    private static URI buildUri(String prefixElement, String src) {
        String prefix = prefixElement != null ? prefixElement
            .replaceFirst("^(/+)", "")
            .replaceAll("(/+)$", "") + "/"
            : "";
        String typeName = Sources.extractName(src).orElseGet(() -> {
            logger.warn(() -> "Cannot infer class name from\n" + src);
            return "Test";
        });
        return URI.create("string:///" + prefix + typeName + Kind.SOURCE.extension);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return src;
    }
}
