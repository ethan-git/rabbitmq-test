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

    @GetMapping("/set-concurrency")
    public Mono<Boolean> setAutoAckFluxConcurrencyCount(@RequestParam int concurrencyCount) {
        return Mono.just(Boolean.TRUE);
    }

    @GetMapping("/send")
    public Mono<Boolean> send(@RequestParam int messageCount) {
        return producerService.send(messageCount)
                              .then(Mono.just(Boolean.TRUE));
    }
}