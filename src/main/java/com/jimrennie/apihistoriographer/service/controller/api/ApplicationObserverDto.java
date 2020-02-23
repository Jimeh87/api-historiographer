package com.jimrennie.apihistoriographer.service.controller.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ApplicationObserverDto {
	private String key;
	private Map<String, List<String>> headers = new HashMap<>();
	private String path;
	private Map<String, List<String>> queryParameters = new HashMap<>();
	private String method;
	private String body;
	private Integer pollingIntervalMinutes;
}
