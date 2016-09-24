package com.example.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class MessageSourceTest {
    @Autowired
    MessageSource messageSource;

    @Test
    public void contextLoads() {
        System.out.println(messageSource.getMessage("greeting.message", new Object[]{"Taro"}, Locale.ENGLISH));
    }
}
