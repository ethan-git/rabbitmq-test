package org.fubabz.test.rabbitmq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.AcknowledgableDelivery;
import reactor.rabbitmq.ConsumeOptions;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "test-queue";

    @Bean
    public ConnectionFactory testRabbitConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("test");
        factory.setPassword("test");
        return factory;
    }

    @Bean
    public Sender sender(ConnectionFactory testRabbitConnectionFactory) {
        SenderOptions options = new SenderOptions().connectionFactory(testRabbitConnectionFactory);
        return RabbitFlux.createSender(options);
    }

    @Bean
    public Receiver receiver(ConnectionFactory testRabbitConnectionFactory) {
        ReceiverOptions options = new ReceiverOptions()
                .connectionFactory(testRabbitConnectionFactory)
                .connectionSubscriptionScheduler(Schedulers.boundedElastic());
        return RabbitFlux.createReceiver(options);
    }

    @Profile("autoack")
    @Bean
    public Flux<Delivery> autoAckReceiverFlux(Receiver receiver) {
        return receiver.consumeAutoAck(QUEUE_NAME, new ConsumeOptions())
                       .limitRate(10);
    }

    @Profile("manualack")
    @Bean
    public Flux<AcknowledgableDelivery> manualAckReceiverFlux(Receiver receiver) {
        return receiver.consumeManualAck(QUEUE_NAME, new ConsumeOptions().qos(10));
    }
}
