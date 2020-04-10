package com.example.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class JacksonTypeTest {
    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        {
            Entry entry = objectMapper.readValue("{\"type\":\"image\", \"url\": \"http://example.com/image\"}", Entry.class);
            System.out.println(entry);
        }

        {
            Entry entry = objectMapper.readValue("{\"type\":\"text\", \"body\": \"Hello!\"}", Entry.class);
            System.out.println(entry);
        }

        {
            TextEntry textEntry = new TextEntry("Hello");
            String json = objectMapper.writeValueAsString(textEntry);
            System.out.println(json);
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(ImageEntry.class),
            @JsonSubTypes.Type(TextEntry.class)
    })
    public interface Entry {
    }

    @JsonTypeName("image")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageEntry implements Entry {
        String url;
    }

    @JsonTypeName("text")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextEntry implements Entry {
        String body;
    }
}
