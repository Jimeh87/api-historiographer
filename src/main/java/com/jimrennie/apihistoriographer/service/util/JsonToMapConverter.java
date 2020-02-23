package com.jimrennie.apihistoriographer.service.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Converter
public class JsonToMapConverter implements AttributeConverter<Map<String, List<String>>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@SneakyThrows
	@Override
	public String convertToDatabaseColumn(Map<String, List<String>> attribute) {
		return objectMapper.writeValueAsString(attribute);
	}

	@SneakyThrows
	@Override
	public Map<String, List<String>> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return new HashMap<>();
		}
		return objectMapper.readValue(dbData, mapTypeReference());
	}

	private static TypeReference<Map<String, List<String>>> mapTypeReference() {
		return new TypeReference<>() {};
	}
}
