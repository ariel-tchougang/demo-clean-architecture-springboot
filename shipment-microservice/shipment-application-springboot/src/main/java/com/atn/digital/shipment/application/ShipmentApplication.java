package com.atn.digital.shipment.application;

import com.atn.digital.shipment.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.shipment.application.config.ShipmentDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ ShipmentDomainConfig.class, RabbitMQConfiguration.class })
@ConfigurationPropertiesScan
public class ShipmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentApplication.class, args);
	}

}
