package com.github.fridujo.markdown.junit.engine.support;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceMemoryJavaFileObject extends SimpleJavaFileObject {

    private static final Logger logger = LoggerFactory.getLogger(SourceMemoryJavaFileObject.class);

    private static final Pattern classNamePattern = Pattern.compile("(class|interface|record|annotation|@annotation)\\s+(?<className>[^\\s{]+)");
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
        return URI.create("string:///" + prefix + extractName(src) + Kind.SOURCE.extension);
    }

    private static String extractName(String src) {
        Matcher matcher = classNamePattern.matcher(src);
        if (matcher.find()) {
            return matcher.group("className");
        }
        logger.warn(() -> "Cannot infer class name from\n" + src);
        return "Test";
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return src;
    }
}
