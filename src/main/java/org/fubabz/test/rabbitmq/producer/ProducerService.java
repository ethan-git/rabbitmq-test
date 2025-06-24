
package org.fubabz.test.rabbitmq.producer;

import static org.fubabz.test.rabbitmq.config.RabbitConfig.QUEUE_NAME;

import java.time.Duration;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;

@Slf4j
@Service
public class ProducerService {

    private final Sender sender;

    public ProducerService(Sender sender) {
        this.sender = sender;
    }

    @PostConstruct
    public void setUp() {
        sender.declareQueue(QueueSpecification.queue(QUEUE_NAME)).block();
//        sender.declareQueue(QueueSpecification.queue("line.voom.notification.live")).block();
//        sender.declareQueue(QueueSpecification.queue("line.voom.notification.live.light")).block();
//        sender.declareQueue(QueueSpecification.queue("line.voom.notification.live.heavy")).block();
//        sender.declareQueue(QueueSpecification.queue("line.voom.gnbvariation.live")).block();
//        sender.declareQueue(QueueSpecification.queue("line.voom.gnbvariation.live.light")).block();
//        sender.declareQueue(QueueSpecification.queue("line.voom.gnbvariation.live.heavy")).block();

    }

    public Mono<Void> send(int messageCount) {
        return Flux.fromStream(IntStream.rangeClosed(1, messageCount).boxed())
                   .map(i -> new OutboundMessage("", QUEUE_NAME, ("Message " + i).getBytes()))
                   .as(sender::send);
    }
}
