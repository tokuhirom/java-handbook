package com.example.controller;

import com.example.I18nApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.Cookie;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackageClasses = I18nApplication.class)
public class I18nControllerTest {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    public void i18n() throws Exception {
        // EN
        {
            mockMvc.perform(get("/i18n").locale(Locale.ENGLISH))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("i18n"))
                    .andExpect(content().string(containsString("Welcome")));
        }

        // KO
        {
            mockMvc.perform(get("/i18n").locale(Locale.KOREAN))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("i18n"))
                    .andExpect(content().string(containsString("오신 것을 환영합니다")));
        }

        // FRENCH(There's no french resource. fallback to english)
        {
            mockMvc.perform(get("/i18n").locale(Locale.FRENCH))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("i18n"))
                    .andExpect(content().string(containsString("Welcome")));
        }
    }

    @Test
    public void testI18nTempSet() throws Exception {
        // temporary set to japanese
        {
            mockMvc.perform(get("/i18n").param("l", "ja").locale(Locale.ENGLISH))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("i18n"))
                    .andExpect(content().string(containsString("ようこそ")))
                    .andExpect(header().doesNotExist("set-cookie"));
        }
    }

    @Test
    public void setLocale() throws Exception {
        // set locale to japanese
        Cookie cookie = mockMvc.perform(
                post("/set-locale").param("locale", "ja")
        )
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/i18n"))
                .andExpect(header().string("location", "/i18n"))
                .andExpect(cookie().value("locale", "ja"))
                .andReturn()
                .getResponse()
                .getCookie("locale");

        // prefer cookie
        mockMvc.perform(get("/i18n").locale(Locale.ENGLISH).cookie(cookie))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("i18n"))
                    .andExpect(content().string(containsString("ようこそ")));
    }
}