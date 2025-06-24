
package org.fubabz.test.rabbitmq.producer;

import static org.fubabz.test.rabbitmq.config.RabbitConfig.QUEUE_NAME_1;
import static org.fubabz.test.rabbitmq.config.RabbitConfig.QUEUE_NAME_2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;

import java.time.Duration;

@Slf4j
@Component
public class Producer implements CommandLineRunner {

    private final Sender sender;

    public Producer(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void run(String... args) {
        sender.declareQueue(QueueSpecification.queue(QUEUE_NAME_1)).block();
        sender.declareQueue(QueueSpecification.queue(QUEUE_NAME_2)).block();
//        send();
    }

    private void send() {
        Flux.interval(Duration.ofMillis(20))
            .map(i -> new OutboundMessage("", QUEUE_NAME_1, ("Message " + i).getBytes()))
            .as(sender::send)
            .subscribe();
    }
}
