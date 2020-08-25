package com.springboot.springsocialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.springboot.springsocialapp.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SpringSocialappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSocialappApplication.class, args);
	}

}
