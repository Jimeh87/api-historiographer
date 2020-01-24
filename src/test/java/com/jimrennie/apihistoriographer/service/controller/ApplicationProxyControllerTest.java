package com.jimrennie.apihistoriographer.service.controller;

import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfig;
import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@MockServerSettings(ports = 666)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationProxyControllerTest {

	@MockBean
	private ApplicationProxyConfigService applicationProxyConfigService;
	@Autowired
	private TestRestTemplate testRestTemplate;
	private final MockServerClient mockServerClient;

	public ApplicationProxyControllerTest(MockServerClient mockServerClient) {
		this.mockServerClient = mockServerClient;
	}

	@Test
	void testGet() {
		withDefaultApplicationConfig();
		mockServerClient
				.when(HttpRequest.request()
					.withMethod("GET")
					.withPath(""))
				.respond(HttpResponse.response()
					.withStatusCode(200)
					.withBody("{\"key\": \"value\"}"));

		ResponseEntity<Map<String, String>> response = testRestTemplate.exchange("/api/v1/applications/mock-server/proxy", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("value", response.getBody().get("key"));
	}

	@Test
	void testPost() {
		withDefaultApplicationConfig();
		mockServerClient
				.when(HttpRequest.request()
						.withMethod("POST")
						.withPath("/POSTING")
						.withBody("post body"))
				.respond(HttpResponse.response()
					.withStatusCode(201)
					.withBody("{\"key\": \"value\"}"));

		ResponseEntity<Map<String, String>> response = testRestTemplate.exchange("/api/v1/applications/mock-server/proxy/POSTING", HttpMethod.POST, new HttpEntity<>("post body"), new ParameterizedTypeReference<>() {});

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("value", response.getBody().get("key"));
	}

	@Test
	void testQueryParams() {
		withDefaultApplicationConfig();
		mockServerClient
				.when(HttpRequest.request()
						.withMethod("GET")
						.withPath("")
						.withQueryStringParameter("name", "jim")
						.withQueryStringParameter("gender", "MALE"))
				.respond(HttpResponse.response()
						.withStatusCode(200));

		ResponseEntity<Map<String, String>> response = testRestTemplate.exchange("/api/v1/applications/mock-server/proxy?name=jim&gender=MALE", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testHeaders() {
		withDefaultApplicationConfig();
		mockServerClient
				.when(HttpRequest.request()
						.withMethod("GET")
						.withPath("")
						.withHeader("request-header", "hello"))
				.respond(HttpResponse.response()
						.withStatusCode(200)
						.withHeader("response-header", "world"));

		ResponseEntity<Map<String, String>> response = testRestTemplate.exchange(
				"/api/v1/applications/mock-server/proxy",
				HttpMethod.GET,
				new HttpEntity(new LinkedMultiValueMap<>(Map.of("request-header", List.of("hello"), "blacklisted-header", List.of("secret")))),
				new ParameterizedTypeReference<>() {}
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("world", response.getHeaders().get("response-header").get(0));
	}

	private void withDefaultApplicationConfig() {
		when(applicationProxyConfigService.getConfig(any())).thenReturn(
				new ApplicationProxyConfig()
						.setApplication("mock-server")
						.setScheme("http")
						.setHost(mockServerClient.remoteAddress().getHostName())
						.setPort(mockServerClient.remoteAddress().getPort())
						.setHeaderBlacklist(List.of("blacklisted-header")));
	}

}
