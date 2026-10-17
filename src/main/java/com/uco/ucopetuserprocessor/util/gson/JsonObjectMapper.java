package com.uco.ucopetuserprocessor.util.gson;

public interface JsonObjectMapper {

	<T> String toJson(T object);

	<T> T fromJson(String json, Class<T> targetClass);
}