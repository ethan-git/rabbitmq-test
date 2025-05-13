
package org.fubabz.test.rabbitmq.consumer;

import static org.fubabz.test.rabbitmq.config.RabbitConfig.QUEUE_NAME;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
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
        manualAckReceiverFlux.flatMap(msg -> {
                               int sec = ThreadLocalRandom.current().nextInt(6, 11);
                               log.debug("ManualAck Process start: " + new String(msg.getBody()));
                               return Mono.delay(Duration.ofMillis(sec * 1000))
                                          .doOnNext(i -> {
                                              msg.ack();
                                              log.debug("ManualAck finished: " + new String(msg.getBody()) + " / processingTime: " + sec);
                                          });
                           })
                           .subscribe();
    }
}
