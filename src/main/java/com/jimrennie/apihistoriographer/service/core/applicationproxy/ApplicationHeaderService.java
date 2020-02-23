package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class ApplicationHeaderService {

	public static final String APPLICATION_NAME_HEADER = "p-application";

	@Autowired
	private ApplicationProxyService applicationProxyService;

	public HttpHeaders getHeaders(String application, HttpHeaders httpHeaders) {
		ApplicationProxyDto applicationProxy = applicationProxyService.get(application);

		removeBlacklistedHeaders(httpHeaders, applicationProxy);

		httpHeaders.add(APPLICATION_NAME_HEADER, application);
		httpHeaders.setHost(new InetSocketAddress(applicationProxy.getHost(), applicationProxy.getPort()));

		return httpHeaders;
	}

	private void removeBlacklistedHeaders(HttpHeaders httpHeaders, ApplicationProxyDto applicationProxy) {
		httpHeaders.entrySet().stream().flatMap(this::flatten)
				.filter(header -> isBlacklisted(applicationProxy, header))
				.forEach(header -> httpHeaders.remove(header.getKey(), header.getValue()));
	}

	private boolean isBlacklisted(ApplicationProxyDto applicationProxy, Map.Entry<String, String> header) {
		return applicationProxy.getHeaderBlacklist().entrySet().stream()
				.flatMap(this::flatten)
				.anyMatch(blacklistedHeader -> matchesBlacklistedHeader(header, blacklistedHeader));
	}

	private boolean matchesBlacklistedHeader(Map.Entry<String, String> header, Map.Entry<String, String> blacklistedHeader) {
		return blacklistedHeader.getKey().equalsIgnoreCase(header.getKey())
				&& (blacklistedHeader.getValue() == null || Pattern.compile(blacklistedHeader.getValue()).matcher(header.getValue()).matches());
	}

	private Stream<Map.Entry<String, String>> flatten(Map.Entry<String, List<String>> e) {
		return e.getValue().stream()
				.map(value -> new AbstractMap.SimpleEntry<>(e.getKey(), value));
	}

}
