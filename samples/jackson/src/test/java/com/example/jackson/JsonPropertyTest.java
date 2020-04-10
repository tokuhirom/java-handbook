package com.example.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class JsonPropertyTest {
    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Entry entry = objectMapper.readValue("{\"blog_id\":3, \"title\":\"hoge\"}", Entry.class);
        System.out.println(entry);
    }

    @Test
    public void test2() throws IOException {
        Entry entry = new Entry("hoge", 5963L);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entry);
        System.out.println(json);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        String title;
        @JsonProperty("blog_id")
        long blogId;
    }

}
