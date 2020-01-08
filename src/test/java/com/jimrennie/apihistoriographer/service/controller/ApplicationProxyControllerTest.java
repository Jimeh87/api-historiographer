package com.jimrennie.apihistoriographer.service.controller;

import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfig;
import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@MockServerSettings(ports = 666)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationProxyControllerTest {

	@LocalServerPort
	private String localServerPort;
	@MockBean
	private ApplicationProxyConfigService applicationProxyConfigService;
	@Autowired
	private TestRestTemplate testRestTemplate;
	private final MockServerClient mockServerClient;
	private String baseUri;

	public ApplicationProxyControllerTest(MockServerClient mockServerClient) {
		this.mockServerClient = mockServerClient;
	}

	@BeforeEach
	void setUp() {
		when(applicationProxyConfigService.getConfig(any())).thenReturn(
				new ApplicationProxyConfig()
						.setApplication("mock-server")
						.setScheme("http")
						.setHost(mockServerClient.remoteAddress().getHostName())
						.setPort(mockServerClient.remoteAddress().getPort()));
	}

	@Test
	public void testIt() {
		mockServerClient
				.when(HttpRequest.request()
					.withMethod("GET")
					.withPath(""))
				.respond(HttpResponse.response()
					.withStatusCode(200));
		assertEquals(HttpStatus.OK, testRestTemplate.getForEntity("/api/v1/applications/mock-server/proxy", Map.class).getStatusCode());
	}

}
