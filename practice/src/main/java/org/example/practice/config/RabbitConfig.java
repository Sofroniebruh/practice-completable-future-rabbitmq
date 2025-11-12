package org.example.practice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.core.Queue;


@Configuration
@Slf4j
public class RabbitConfig {
    private static final String PRACTICE_ROUTING_KEY = "practice.routing_key_1";
    private static final String PRACTICE_EXCHANGE = "practice.exchange_1";
    private static final String PRACTICE_QUEUE = "practice.queue_1";

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cf) {
        RabbitTemplate rt = new RabbitTemplate(cf);

        rt.setMandatory(true);
        rt.setConfirmCallback(((correlationData, ack, cause) -> {
            if (!ack) {
                if (correlationData != null) {
                    log.error("Message with id: {} failed to reach exchange", correlationData.getId());
                }

                log.error("Message failed to reach exchange");
            } else {
                if (correlationData != null) {
                    log.debug("Message with id: {} was received successfully", correlationData.getId());
                }

                log.debug("Message was received successfully");
            }
        }));
        rt.setReturnsCallback((returnedMessage) -> {
            log.error("Failed to route message to a queue. Message: {}, Exchange: {}, Reply text: {}",
                    returnedMessage.getMessage(),
                    returnedMessage.getExchange(),
                    returnedMessage.getReplyText());
        });

        return rt;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(CachingConnectionFactory cf) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);

        return factory;
    }

    @Bean
    public Queue practiceQueue() {
        return new Queue(PRACTICE_QUEUE, false);
    }

    @Bean
    public DirectExchange practiceExchange() {
        return new DirectExchange(PRACTICE_EXCHANGE);
    }

    @Bean
    public Binding practiceBinding() {
        return BindingBuilder
                .bind(practiceQueue())
                .to(practiceExchange())
                .with(PRACTICE_ROUTING_KEY);
    }
}
