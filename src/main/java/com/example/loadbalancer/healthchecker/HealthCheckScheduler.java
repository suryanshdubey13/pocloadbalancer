package com.example.loadbalancer.healthchecker;

import com.example.loadbalancer.instance.ServiceInstance;
import com.example.loadbalancer.registry.InstanceRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler {

    private final InstanceRegistry registry;
    private final WebClient webClient;

    @Scheduled(fixedDelay = 5000)
    public void checkHealth() {

        for (ServiceInstance instance : registry.getInstances()) {

            webClient.get()
                    .uri(instance.getUrl() + "/actuator/health")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(2))
                    .subscribe(
                            res -> registry.updateHealth(instance.getUrl(), true),
                            err -> registry.updateHealth(instance.getUrl(), false)
                    );
        }
    }
}
