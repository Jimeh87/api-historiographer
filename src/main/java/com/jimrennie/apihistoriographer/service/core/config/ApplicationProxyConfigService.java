package com.jimrennie.apihistoriographer.service.core.config;

import com.jimrennie.apihistoriographer.service.util.ResourceDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
public class ApplicationProxyConfigService {

	@Autowired
	private ResourceDirectory resourceDirectory;

	private Map<String, ApplicationProxyConfig> proxyConfig;

	@PostConstruct
	public void createProxyConfigMap() {
		proxyConfig = resourceDirectory.read("config", "*.application-proxy.json", ApplicationProxyConfig.class)
				.stream()
				.collect(toMap(ApplicationProxyConfig::getApplication, identity()));
	}

	public ApplicationProxyConfig getConfig(String application) {
		return Optional.ofNullable(proxyConfig.get(application))
				.orElseThrow(() -> new IllegalArgumentException(String.format("Application [%s] not found", application)));
	}

}
