package com.example.pocloadbalancer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Hello")
public class HelloController {

    @Value("${HOSTNAME:unknown}")
    private String hostname;

    @GetMapping("/hello")
    public String hello() {
        return "Response from container: " + hostname;
    }
}
