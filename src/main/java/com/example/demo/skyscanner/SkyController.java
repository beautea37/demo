package com.example.demo.skyscanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class SkyController {
    @GetMapping("/sky")
    public String index() {
        return "skyscanner/index"; // points to src/main/resources/templates/index.html
    }
}