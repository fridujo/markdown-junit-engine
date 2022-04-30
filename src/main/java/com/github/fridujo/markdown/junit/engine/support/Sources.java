package com.github.fridujo.markdown.junit.engine.support;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sources {

    private static final Pattern classNamePattern = Pattern.compile("(class|interface|enum|record|@interface)\\s+(?<className>[^\\s{]+)");

    public static Optional<String> extractName(String src) {
        Matcher matcher = classNamePattern.matcher(src);
        if (matcher.find()) {
            return Optional.of(matcher.group("className"));
        }
        return Optional.empty();
    }
}
