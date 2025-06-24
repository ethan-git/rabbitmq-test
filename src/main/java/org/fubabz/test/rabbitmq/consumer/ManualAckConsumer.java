
package org.fubabz.test.rabbitmq.consumer;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.AcknowledgableDelivery;

@Slf4j
@Component
@Profile("manualack")
public class ManualAckConsumer {

    private final Flux<AcknowledgableDelivery> manualAckReceiverFlux;

    public ManualAckConsumer(Flux<AcknowledgableDelivery> manualAckReceiverFlux) {
        this.manualAckReceiverFlux = manualAckReceiverFlux;
    }

    @PostConstruct
    public void consume() {
        log.info("Manual Consumer Started");
        AtomicInteger counter = new AtomicInteger(0);
        manualAckReceiverFlux.flatMap(msg -> {
                                 int sec = ThreadLocalRandom.current().nextInt(3, 5);
                                 log.debug("ManualAck Process start: " + new String(msg.getBody()));
                                 if (counter.incrementAndGet() < 5) {
                                     return Mono.delay(Duration.ofMillis(1_000))
                                                .doOnNext(i -> {
                                                    msg.nack(true);
                                                    log.debug("ManualAck nack: " + new String(msg.getBody()) + " / processingTime: " + 5);
                                                });
                                 } else {
                                     return Mono.delay(Duration.ofMillis(sec * 1_000))
                                                .doOnNext(i -> {
                                                    msg.ack();
                                                    log.debug("ManualAck finished: " + new String(msg.getBody()) + " / processingTime: " + sec);
                                                });
                                 }

                             }, 10)
                             .subscribe();
    }
}
