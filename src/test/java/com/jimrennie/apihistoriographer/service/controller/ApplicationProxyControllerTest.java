package com.jimrennie.apihistoriographer.service.controller;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationProxyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

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
class ApplicationProxyControllerTest {

	@MockBean
	private ApplicationProxyService applicationProxyService;
	@Autowired
	private TestRestTemplate testRestTemplate;
	private final MockServerClient mockServerClient;

	public ApplicationProxyControllerTest(MockServerClient mockServerClient) {
		this.mockServerClient = mockServerClient;
	}

	@BeforeEach
	void resetMockServerClient() {
		mockServerClient.reset();
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

		ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange("/api/v1/applications/mock-server/proxy", HttpMethod.GET, HttpEntity.EMPTY, mapType());

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

		ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange("/api/v1/applications/mock-server/proxy/POSTING", HttpMethod.POST, new HttpEntity<>("post body"), mapType());

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
						.withPath("/QUERY-PARAMS")
						.withQueryStringParameter("name", "jim")
						.withQueryStringParameter("gender", "MALE"))
				.respond(HttpResponse.response()
						.withStatusCode(200));

		ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange("/api/v1/applications/mock-server/proxy/QUERY-PARAMS?name=jim&gender=MALE", HttpMethod.GET, HttpEntity.EMPTY, mapType());

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testHeaders() {
		withDefaultApplicationConfig();
		// TODO : Need to verify blacklist header isn't sent
		mockServerClient
				.when(HttpRequest.request()
						.withMethod("GET")
						.withPath("/HEADERS")
						.withHeader("request-header", "hello"))
				.respond(HttpResponse.response()
						.withStatusCode(200)
						.withHeader("response-header", "world"));

		ResponseEntity<Map<String, Object>> response = testRestTemplate.exchange(
				"/api/v1/applications/mock-server/proxy/HEADERS",
				HttpMethod.GET,
				new HttpEntity(new LinkedMultiValueMap<>(Map.of("request-header", List.of("hello"), "blacklisted-header", List.of("secret")))),
				mapType()
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("world", response.getHeaders().get("response-header").get(0));
	}

	private ParameterizedTypeReference<Map<String, Object>> mapType() {
		return new ParameterizedTypeReference<>() {
		};
	}

	private void withDefaultApplicationConfig() {
		when(applicationProxyService.getConfig(any())).thenReturn(
				new ApplicationProxyDto()
						.setApplication("mock-server")
						.setScheme("http")
						.setHost(mockServerClient.remoteAddress().getHostName())
						.setPort(mockServerClient.remoteAddress().getPort())
						.setHeaderBlacklist(List.of("blacklisted-header")));
	}

}
