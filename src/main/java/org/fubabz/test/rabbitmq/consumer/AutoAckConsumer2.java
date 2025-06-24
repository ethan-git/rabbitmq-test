
package org.fubabz.test.rabbitmq.consumer;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Delivery;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Profile("autoack")
public class AutoAckConsumer2 {

    private final Flux<Delivery> autoAckReceiverFlux2;

    public AutoAckConsumer2(Flux<Delivery> autoAckReceiverFlux2) {
        this.autoAckReceiverFlux2 = autoAckReceiverFlux2;
    }

    @PostConstruct
    public void consume() {
        log.info("AutoAck Consumer2 Started");
        autoAckReceiverFlux2.flatMap(msg -> {
                               int sec = ThreadLocalRandom.current().nextInt(6, 11);
                               log.debug("AutoAck2 Process start: " + new String(msg.getBody()));
                               return Mono.delay(Duration.ofMillis(sec * 1000))
//                                          .doOnNext(i -> log.debug("AutoAck2 finished: " + new String(msg.getBody()) + " / processingTime: " + sec))
                                       ;
                           }, 5)
                           .subscribe();
    }
}
