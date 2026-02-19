package com.example.loadbalancer.controller;

import com.example.loadbalancer.instance.ServiceInstance;
import com.example.loadbalancer.registry.InstanceRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class LoadBalancerController {

    private final InstanceRegistry registry;
    private final WebClient webClient;

    @GetMapping("/service")
    @Retry(name = "backendRetry", fallbackMethod = "fallback")
    public Mono<String> route() {
        return Mono.defer(() -> {

            ServiceInstance instance = registry.getNextInstance();

            return webClient.get()
                    .uri(instance.getUrl() + "/service")
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(ex -> registry.updateHealth(instance.getUrl(), false));

        });
    }

    public Mono<String> fallback(Throwable t) {

        return Mono.just("Service temporarily unavailable");
    }
}
