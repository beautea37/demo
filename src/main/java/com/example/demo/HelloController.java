package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Controller
public class HelloController {
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello Spring Boot Board";
    }

    @GetMapping("/test")
    public String test() {
        return "test_form";
    }

    @PostMapping("/test")
    public String addFile(@RequestParam String username, @RequestParam MultipartFile file) throws IOException {
        System.out.println("username : " + username);

        if (!file.isEmpty()) {
            String fullPath = "C:\\Users\\82106\\Pictures\\Upload\\upload" + file.getOriginalFilename();
            System.out.println("파일저장 Path : " + fullPath);
            file.transferTo(new File(fullPath));
        }
        return "test_form";
    }
}