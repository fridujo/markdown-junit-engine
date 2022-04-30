package com.github.fridujo.markdown.junit.engine.support;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SourcesTest {

    @ParameterizedTest
    @CsvSource({
        "class Machin {}, Machin",
        "public interface Bidule{}, Bidule",
        "'final record Chose \n{\n}', Chose",
        "enum MyEnum { }, MyEnum",
        "static @interface MyAnnotation {}, MyAnnotation"
    })
    void extract_type_name(String source, String expectedTypeName) {
        Optional<String> typeName = Sources.extractName(source);

        assertThat(typeName).hasValue(expectedTypeName);
    }

    @ParameterizedTest
    @CsvSource({
        "clas Bidule",
        "classs Bidule",
        " something",
        "MyClass {}"
    })
    void extract_unresolvable_type_name_returns_empty(String source) {
        Optional<String> typeName = Sources.extractName(source);

        assertThat(typeName).isEmpty();
    }
}
