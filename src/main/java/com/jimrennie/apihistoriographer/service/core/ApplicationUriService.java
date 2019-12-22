package com.jimrennie.apihistoriographer.service.core;

import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfig;
import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfigService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ApplicationUriService {
	
	@Autowired
	private ApplicationProxyConfigService applicationProxyConfigService;

	@SneakyThrows
	public URI create(String application, String path, String query) {
		ApplicationProxyConfig appConfig = applicationProxyConfigService.getConfig(application);
		return UriComponentsBuilder.newInstance()
				.scheme(appConfig.getScheme())
				.host(appConfig.getHost())
				.port(appConfig.getPort())
				.path(path)
				.query(query)
				.build(true)
				.toUri();
	}
}
