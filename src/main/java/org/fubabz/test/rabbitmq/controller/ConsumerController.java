package org.fubabz.test.rabbitmq.controller;

import org.fubabz.test.rabbitmq.producer.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {

    private final ProducerService producerService;

    @GetMapping("/send1")
    public Mono<Boolean> send1(@RequestParam int messageCount) {
        return producerService.send1(messageCount)
                              .then(Mono.just(Boolean.TRUE));
    }

    @GetMapping("/send2")
    public Mono<Boolean> send2(@RequestParam int messageCount) {
        return producerService.send2(messageCount)
                              .then(Mono.just(Boolean.TRUE));
    }
}