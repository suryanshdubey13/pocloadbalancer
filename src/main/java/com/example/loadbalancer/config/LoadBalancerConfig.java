package com.example.loadbalancer.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "loadbalancer")
public class LoadBalancerConfig {

    private List<Instance> instances;
    private int connectTimeout;
    private int readTimeout;

    @Data
    public static class Instance {
        private String url;
        private int weight;
    }
}

