
package org.fubabz.test.rabbitmq.consumer;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import com.rabbitmq.client.Delivery;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Profile("autoack")
public class AutoAckConsumer {

    private final Flux<Delivery> autoAckReceiverFlux;

    public AutoAckConsumer(Flux<Delivery> autoAckReceiverFlux) {
        this.autoAckReceiverFlux = autoAckReceiverFlux;
    }

    @PostConstruct
    public void consume() {
        log.info("AutoAck Consumer Started");
        autoAckReceiverFlux.flatMap(msg -> {
                               int sec = ThreadLocalRandom.current().nextInt(6, 11);
                               log.debug("AutoAck Process start: " + new String(msg.getBody()));
                               return Mono.delay(Duration.ofMillis(sec * 1000))
                                          .doOnNext(i -> log.debug("AutoAck finished: " + new String(msg.getBody()) + " / processingTime: " + sec));
                           }, 10)
                           .subscribe();
    }
}
