package com.ous.poc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ous.poc.entity.Task;

/**
 * Configuration for audit related attributes in the application such as
 * createdAt, updatedAt. Spring data is responsible for setting these audit
 * related attributes as soon as the objects are processed using spring data
 * repositories. Check {@link Task} entity for more details.
 * 
 * @author abdulhafeez
 *
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

}
