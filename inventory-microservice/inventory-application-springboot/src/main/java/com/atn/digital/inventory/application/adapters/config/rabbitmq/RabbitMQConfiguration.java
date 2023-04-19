package com.atn.digital.inventory.application.adapters.config.rabbitmq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(RabbitMQHostConfig config) {
        return initFactory(config.uri(), config.username(), config.password());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    private ConnectionFactory initFactory(String uri, String username, String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(URI.create(uri));

        if (username != null && !username.isBlank()) {
            connectionFactory.setUsername(username);
        }

        if (password != null && !password.isBlank()) {
            connectionFactory.setPassword(password);
        }

        return connectionFactory;
    }
}
