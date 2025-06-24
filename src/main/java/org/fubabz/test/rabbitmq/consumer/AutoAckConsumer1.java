
package org.fubabz.test.rabbitmq.consumer;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

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
public class AutoAckConsumer1 {

    private final Flux<Delivery> autoAckReceiverFlux1;

    public AutoAckConsumer1(Flux<Delivery> autoAckReceiverFlux1) {
        this.autoAckReceiverFlux1 = autoAckReceiverFlux1;
    }

    @PostConstruct
    public void consume() {
        log.info("AutoAck Consumer1 Started");
        autoAckReceiverFlux1.flatMap(msg -> {
                               int sec = ThreadLocalRandom.current().nextInt(6, 11);
                               log.debug("AutoAck1 Process start: " + new String(msg.getBody()));
                               return Mono.delay(Duration.ofMillis(sec * 1000))
//                                          .doOnNext(i -> log.debug("AutoAck1 finished: " + new String(msg.getBody()) + " / processingTime: " + sec))
                                       ;
                           }, 5)
                           .subscribe();
    }
}
