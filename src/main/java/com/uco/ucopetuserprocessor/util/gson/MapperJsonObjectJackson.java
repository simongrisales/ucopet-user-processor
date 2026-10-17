package com.uco.ucopetuserprocessor.util.gson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperJsonObjectJackson implements MapperJsonObject {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public <T> String toJson(T object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException exception) {
			throw new IllegalArgumentException("Object could not be converted to JSON.", exception);
		}
	}

	@Override
	public <T> T fromJson(String json, Class<T> targetClass) {
		try {
			return objectMapper.readValue(json, targetClass);
		} catch (JsonProcessingException exception) {
			throw new IllegalArgumentException("JSON could not be converted to object.", exception);
		}
	}
}