package com.atn.digital.inventory.application;

import com.atn.digital.inventory.application.config.InventoryDomainConfig;
import com.atn.digital.inventory.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ InventoryDomainConfig.class, RabbitMQConfiguration.class })
@ConfigurationPropertiesScan
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

}
