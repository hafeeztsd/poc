package com.ous.poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration/ Properties related to Application.
 * 
 * @author abdulhafeez
 *
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "app.config")
@EnableConfigurationProperties
public class ApplicationConfig {

	private int poolSize;
	private int initialDelay;
	private int maxDelay;
}
