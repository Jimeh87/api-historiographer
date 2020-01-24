package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class ApplicationUriService {
	
	@Autowired
	private ApplicationProxyService applicationProxyService;

	@SneakyThrows
	public URI create(String application, String path, String query) {
		ApplicationProxyDto appConfig = applicationProxyService.getConfig(application);
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
