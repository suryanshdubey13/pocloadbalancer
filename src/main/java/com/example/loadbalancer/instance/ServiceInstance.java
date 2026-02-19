package com.example.loadbalancer.instance;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceInstance {
    private String url;
    private int weight;
    private boolean healthy;
}