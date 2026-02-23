package com.example.pocloadbalancer.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/service")
    public String hello() {
          return "Hello from port: " + serverPort;
    }
}
