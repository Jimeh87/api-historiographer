package com.jimrennie.apihistoriographer.service.core.proxy;

import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationHeaderService;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationUriService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class ProxyService {

	@Autowired
	private ApplicationUriService applicationUriService;
	@Autowired
	private ApplicationHeaderService applicationHeaderService;
	@Autowired
	private RestTemplate restTemplate;

	@Builder(builderMethodName = "proxyBuilder", builderClassName = "ProxyBuilder", buildMethodName = "request")
	public ResponseEntity<String> proxy(String application, HttpMethod method, HttpHeaders headers, String body, String path, String query) {
		ResponseEntity<String> responseEntity = proxy(
				applicationUriService.create(application, path, query),
				method,
				applicationHeaderService.getHeaders(application, headers),
				body);

		return responseEntity;
	}

	private ResponseEntity<String> proxy(URI uri, HttpMethod method, HttpHeaders headers, String body) {
		try {
			return restTemplate.exchange(uri, method, new HttpEntity<>(body, headers), String.class);
		} catch(HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode())
					.headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}
	}

}
