package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
public class ApplicationHeaderService {

	public static final String APPLICATION_NAME_HEADER = "p-application";

	@Autowired
	private ApplicationProxyService applicationProxyService;

	public HttpHeaders getHeaders(String application, HttpHeaders httpHeaders) {
		ApplicationProxyDto config = applicationProxyService.getConfig(application);
		config.getHeaderBlacklist().forEach(httpHeaders::remove);
		httpHeaders.add(APPLICATION_NAME_HEADER, application);
		httpHeaders.setHost(new InetSocketAddress(config.getHost(), config.getPort()));

		return httpHeaders;
	}

}
