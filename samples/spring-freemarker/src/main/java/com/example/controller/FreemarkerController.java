package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class FreemarkerController {
    @GetMapping("/")
    public String freemarker(Model model) {
        model.addAttribute("xmp", "<xmp>");
        return "index";
    }
}
