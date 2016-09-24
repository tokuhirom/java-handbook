package com.example.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Value;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class JacksonTest {

    // Unrecognized field で死ぬ例
    @Test
    public void testUnrecognizedField() throws IOException {
        Assertions.assertThatThrownBy(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Foo foo = objectMapper.readValue("{\"foo\":3}", Foo.class);
        }).isInstanceOf(UnrecognizedPropertyException.class);
    }

    public static class Foo {
    }

    @Value
    public static class Bar {
        String title;
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        Bar hoge = new Bar("hoge");

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(hoge));
    }
}
