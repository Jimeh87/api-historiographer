package com.jimrennie.apihistoriographer.service.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationProxyDto {
	private String application;
	private String scheme = "http";
	private String host;
	private int port = 80;
	private List<String> headerBlacklist = new ArrayList<>();
	private List<ApplicationObserverDto> observers = new ArrayList<>();
}
