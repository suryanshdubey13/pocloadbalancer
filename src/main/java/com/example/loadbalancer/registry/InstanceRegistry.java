package com.example.loadbalancer.registry;

import com.example.loadbalancer.config.LoadBalancerConfig;
import com.example.loadbalancer.instance.ServiceInstance;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class InstanceRegistry {

    private final LoadBalancerConfig properties;

    private final List<ServiceInstance> instances = new CopyOnWriteArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        properties.getInstances().forEach(i ->
                instances.add(new ServiceInstance(i.getUrl(), i.getWeight(), true))
        );
    }

    public ServiceInstance getNextInstance() {

        List<ServiceInstance> healthy =
                instances.stream()
                        .filter(ServiceInstance::isHealthy)
                        .toList();

        if (healthy.isEmpty()) {
            throw new RuntimeException("No healthy instances available");
        }

        // Expand list by weight
        List<ServiceInstance> weightedList = new
                ArrayList<>();
        for (ServiceInstance instance : healthy) {
            for (int i = 0; i < instance.getWeight(); i++) {
                weightedList.add(instance);
            }
        }

        int index = counter.getAndIncrement() % weightedList.size();
        return weightedList.get(index);
    }

    public List<ServiceInstance> getInstances() {
        return instances;
    }

    public void updateHealth(String url, boolean health) {
        instances.stream()
                .filter(i -> i.getUrl().equals(url))
                .findFirst()
                .ifPresent(i -> i.setHealthy(health));
    }
}