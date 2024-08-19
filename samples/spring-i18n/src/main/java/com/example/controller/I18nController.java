package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Demo application for demonstrating i18n features
 */
@Controller
@Slf4j
public class I18nController {
    @GetMapping("/i18n")
    public String i18n(
            @RequestParam(value = "l", required = false) String languageTag,
            HttpServletRequest request
    ) {
        if (languageTag != null) {
            Locale locale = Locale.forLanguageTag(languageTag);
            log.info("locale: {}", locale);
            request.setAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
        }
        return "i18n";
    }

    @PostMapping("/set-locale")
    public String setLocale(
            @RequestParam(value = "locale") String localeName,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Locale locale = Locale.forLanguageTag(localeName);
        log.info("locale: {}, {}", localeName, locale);
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(httpServletRequest);
        localeResolver.setLocale(httpServletRequest, httpServletResponse, locale);
        return "redirect:/i18n";
    }

    @GetMapping("/get-locale")
    @ResponseBody
    public String getLocale(HttpServletRequest httpServletRequest) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(httpServletRequest);
        Locale locale = localeResolver.resolveLocale(httpServletRequest);
        return locale.toString();
    }
}
