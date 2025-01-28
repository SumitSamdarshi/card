package com.samda.card.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GameApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		Converter<String, List<Integer>> stringToListConverter = new Converter<String, List<Integer>>() {
			public List<Integer> convert(MappingContext<String, List<Integer>> context) {
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					return objectMapper.readValue(context.getSource(), new TypeReference<List<Integer>>(){});
				} catch (Exception e) {
					return new ArrayList<>();
				}
			}
		};
		modelMapper.addConverter(stringToListConverter);
		return modelMapper;
	}

}
