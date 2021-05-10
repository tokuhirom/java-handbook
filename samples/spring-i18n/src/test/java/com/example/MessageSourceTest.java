package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MessageSourceTest {
    @Autowired
    MessageSource messageSource;

    @Test
    public void contextLoads() {
        System.out.println(messageSource.getMessage("greeting.message", new Object[]{"Taro"}, Locale.ENGLISH));
    }
}
