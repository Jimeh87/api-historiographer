package com.jimrennie.apihistoriographer.service;

import com.jimrennie.apihistoriographer.service.core.ProxyInterceptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiHistoriographerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiHistoriographerServiceApplication.class, args);
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
	}

	@Bean
	public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory, ProxyInterceptor proxyInterceptor) {
		RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
		addInterceptor(restTemplate, proxyInterceptor);
		return restTemplate;
	}

	private List<ClientHttpRequestInterceptor> addInterceptor(RestTemplate restTemplate, ClientHttpRequestInterceptor interceptor) {
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(interceptor);
		restTemplate.setInterceptors(interceptors);
		return interceptors;
	}

}
