package com.jimrennie.apihistoriographer.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class ResourceDirectory {

	@Autowired
	private ResourcePatternResolver resourcePatternResolver;
	@Autowired
	private ObjectMapper objectMapper;

	@SneakyThrows
	public <T> List<T> read(String directory, String filePattern, Class<T> resourceClass) {
		try {
			return Arrays.stream(resourcePatternResolver.getResources("classpath*:" + withTrailingSlash(directory) + filePattern))
					.map(r -> mapTo(r, resourceClass))
					.collect(toList());
		} catch (FileNotFoundException e) {
			return emptyList();
		}
	}

	private String withTrailingSlash(String s) {
		return s.replaceAll("([^/])$","$1/");
	}

	@SneakyThrows
	private <T> T mapTo(Resource r, Class<T> resourceClass) {
		return objectMapper.readValue(IOUtils.toString(r.getInputStream(), StandardCharsets.UTF_8), resourceClass);
	}

}
