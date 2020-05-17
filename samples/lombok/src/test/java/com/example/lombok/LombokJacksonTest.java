package com.example.lombok;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import lombok.Data;

public class LombokJacksonTest {
    @Data
    public static class Foo {
        @JsonProperty("foo_id")
        private String fooId;
    }

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(Foo.class);
        BeanDescription desc = objectMapper.getSerializationConfig().introspect(javaType);
        List<BeanPropertyDefinition> properties = desc.findProperties();
        System.out.println(properties);

        Foo foo = objectMapper.readValue("{\"foo_id\":\"hoge\"}", Foo.class);
        System.out.println(foo.getFooId());
    }
}
