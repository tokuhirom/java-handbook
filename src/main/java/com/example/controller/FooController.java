package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooController {
    @GetMapping("/")
    public String freemarker(Model model) {
        model.addAttribute("xmp", "<xmp>");
        return "index";
    }

    @GetMapping("/spring_ftl")
    public String spring_ftl() {
        return "spring_ftl";
    }
}
