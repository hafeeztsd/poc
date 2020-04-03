package com.ous.poc.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ous.poc.service.impl.TaskServiceImpl;

/**
 * Seamlessly map two objects.Check {@link TaskServiceImpl} for more details.
 * 
 * @author abdulhafeez
 *
 */
@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD).setAmbiguityIgnored(true);
		return modelMapper;
	}

}
