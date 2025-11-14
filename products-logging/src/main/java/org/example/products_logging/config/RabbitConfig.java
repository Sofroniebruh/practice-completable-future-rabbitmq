package org.example.products_logging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class RabbitConfig {
    public static final String PRACTICE_ROUTING_KEY = "practice.routing_key_1";
    public static final String PRACTICE_EXCHANGE = "practice.exchange_1";
    public static final String PRACTICE_QUEUE = "practice.queue_1";

    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        converter.setCreateMessageIds(true);

        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(CachingConnectionFactory cf) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(cf);
        factory.setMessageConverter(messageConverter());

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
