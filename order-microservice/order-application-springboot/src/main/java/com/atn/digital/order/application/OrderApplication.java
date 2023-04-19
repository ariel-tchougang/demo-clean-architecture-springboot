package com.atn.digital.order.application;

import com.atn.digital.order.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.order.application.config.OrderDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ OrderDomainConfig.class, RabbitMQConfiguration.class })
@ConfigurationPropertiesScan
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
