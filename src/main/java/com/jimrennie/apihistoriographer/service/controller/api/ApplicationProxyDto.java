package com.jimrennie.apihistoriographer.service.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationProxyDto {
	private String application;
	private String scheme = "http";
	private String host;
	private int port = 80;
	private Map<String, List<String>> headerBlacklist = new HashMap<>();
	private List<ApplicationObserverDto> observers = new ArrayList<>();

	public ApplicationProxyDto addHeaderBlacklist(String key, String regexValue) {
		headerBlacklist.compute(key, (k, v) -> {
			if (v == null) {
				v = new ArrayList<>();
			}
			v.add(regexValue);
			return v;
		});

		return this;
	}
}
