package com.example.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class OjisanTest {

    // Unrecognized field で死ぬ例
    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE);
        System.out.println(objectMapper.getPropertyNamingStrategy());
        Foo foo = new Foo("Hello", "world");
        log.info("{}", objectMapper.writeValueAsString(foo));

        BeanDescriptor beanDescriptor = new BeanDescriptor(Foo.class);
        Enumeration<String> stringEnumeration = beanDescriptor.attributeNames();
        while (stringEnumeration.hasMoreElements()) {
            System.out.println(stringEnumeration.nextElement());
        }
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(Foo.class);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            System.out.println(propertyDescriptor.getName());
            System.out.println(propertyDescriptor.getReadMethod());
        }
    }

    @Test
    public void testBar() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Bar foo = new Bar("Hello", "world");
        log.info("{}", objectMapper.writeValueAsString(foo));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Foo {
        @JsonProperty("ok")
        private String lower;

        @JsonProperty("ng")
        private String cAmel;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bar {
        @JsonProperty("ok")
        private String lower;

        @Getter(onMethod = @__({@JsonProperty("ng")}))
        @Setter(onMethod = @__({@JsonProperty("ng")}))
        private String cAmel;
    }
}
