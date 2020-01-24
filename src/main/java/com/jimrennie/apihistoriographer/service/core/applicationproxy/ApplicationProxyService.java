package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import com.jimrennie.apihistoriographer.service.util.ResourceDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class ApplicationProxyService {

	@Autowired
	private ResourceDirectory resourceDirectory;

	private Map<String, ApplicationProxyDto> proxyConfig;

	@PostConstruct
	public void createProxyConfigMap() {
		proxyConfig = resourceDirectory.read("config", "*.application-proxy.json", ApplicationProxyDto.class)
				.stream()
				.collect(toMap(ApplicationProxyDto::getApplication, identity()));
	}

	public ApplicationProxyDto getConfig(String application) {
		return Optional.ofNullable(proxyConfig.get(application))
				.orElseThrow(() -> new IllegalArgumentException(String.format("Application [%s] not found", application)));
	}

}
