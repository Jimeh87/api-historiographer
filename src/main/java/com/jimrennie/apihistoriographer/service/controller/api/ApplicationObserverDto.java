package com.jimrennie.apihistoriographer.service.controller.api;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ApplicationObserverDto {
	private String path;
	private List<QueryParameterDto> query = new ArrayList<>();
	private String method;
	private String body;
	private Map<String, List<String>> headers = new HashMap<>();
	private Integer pollingIntervalMinutes;

	@Data
	@Accessors(chain = true)
	public static class QueryParameterDto {
		private String key;
		private String value;
	}
}
