package com.uco.ucopetuserprocessor.util.gson;

import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class MapperJsonObjectJackson implements MapperJsonObject {
	private final ObjectMapper objectMapper;
	public MapperJsonObjectJackson(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	@Override
	public <T> String toJson(T object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JacksonException exception) {
			throw new IllegalArgumentException("Object could not be converted to JSON.", exception);
		}
	}
	@Override
	public <T> T fromJson(String json, Class<T> targetClass) {
		try {
			return objectMapper.readValue(json, targetClass);
		} catch (JacksonException exception) {
			throw new IllegalArgumentException("JSON could not be converted to object.", exception);
		}
	}
}