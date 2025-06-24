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

    public static final String QUEUE_NAME_1 = "test-queue_1";
    public static final String QUEUE_NAME_2 = "test-queue_2";

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
    public Receiver receiver1(ConnectionFactory testRabbitConnectionFactory) {
        ReceiverOptions options = new ReceiverOptions()
                .connectionFactory(testRabbitConnectionFactory)
                .connectionSubscriptionScheduler(Schedulers.boundedElastic());
        return RabbitFlux.createReceiver(options);
    }

    @Bean
    public Receiver receiver2(ConnectionFactory testRabbitConnectionFactory) {
        ReceiverOptions options = new ReceiverOptions()
                .connectionFactory(testRabbitConnectionFactory)
                .connectionSubscriptionScheduler(Schedulers.boundedElastic());
        return RabbitFlux.createReceiver(options);
    }

    @Profile("autoack")
    @Bean
    public Flux<Delivery> autoAckReceiverFlux1(Receiver receiver1) {
        return receiver1.consumeAutoAck(QUEUE_NAME_1, new ConsumeOptions());
    }

    @Profile("autoack")
    @Bean
    public Flux<Delivery> autoAckReceiverFlux2(Receiver receiver2) {
        return receiver2.consumeAutoAck(QUEUE_NAME_2, new ConsumeOptions());
    }

    @Profile("manualack")
    @Bean
    public Flux<AcknowledgableDelivery> manualAckReceiverFlux(Receiver receiver1) {
        return receiver1.consumeManualAck(QUEUE_NAME_1, new ConsumeOptions());
    }
}
