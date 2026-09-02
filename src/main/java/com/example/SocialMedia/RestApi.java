package com.example.SocialMedia;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class RestApi {

    @GetMapping("/hi")
    String hello() {
        return "Hello World";
    }

    @PostMapping("/enterYourAge")
    String CanMarry(@RequestParam int age) {
        if (age < 18) {
            return "Your age is too low";
        } else {
            return "Your age is enough to get married";
        }
    }
}
