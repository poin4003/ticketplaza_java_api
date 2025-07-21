package com.ticketplaza.ddd.controller.http;

import com.ticketplaza.ddd.application.service.event.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
public class HiController {
    private final EventAppService eventAppService;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String hello() {
        return eventAppService.sayHi("Pchuy");
    }


    public String fallbackHello(Throwable throwable) {
        return "Too many request";
    }

    @GetMapping
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHello")
    public String sayHi() {
        return eventAppService.sayHi("Pchuy2");
    }

    private static final SecureRandom random = new SecureRandom();
    @GetMapping("/circuit/breaker")
    @CircuitBreaker(name="checkRandom", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreaker()  {
        String url = "https://fakestoreapi.com/products/" +  random.nextInt(10);

        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackCircuitBreaker(Throwable throwable) {
        return "Service fakestoreapi Error";
    }
}
