package com.atn.digital.payment.application;

import com.atn.digital.payment.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.payment.application.config.PaymentDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ PaymentDomainConfig.class, RabbitMQConfiguration.class })
@ConfigurationPropertiesScan
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

}
