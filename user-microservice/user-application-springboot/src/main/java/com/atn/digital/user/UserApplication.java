package com.atn.digital.user;

import com.atn.digital.user.config.OrderDomainConfig;
import com.atn.digital.user.config.UserDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ UserDomainConfig.class, OrderDomainConfig.class })
@ConfigurationPropertiesScan
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
