package com.jimrennie.apihistoriographer.service.core;

import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfig;
import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationHeaderService.class)
class ApplicationHeaderServiceTest {

	@MockBean
	private ApplicationProxyConfigService applicationProxyConfigService;
	@Autowired
	private ApplicationHeaderService applicationHeaderService;

	@Test
	void testApplicationUriService() {
		when(applicationProxyConfigService.getConfig(any())).thenReturn(
				new ApplicationProxyConfig()
						.setApplication("ANY")
						.setHeaderBlacklist(Arrays.asList("foo", "bar")));

		HttpHeaders headers = new HttpHeaders();
		headers.set("bar", "barValue");
		headers.set("potato", "potatoValue");

		HttpHeaders newHeaders = applicationHeaderService.getHeaders("ANY", headers);

		assertNull(headers.get("foo"));
		assertNull(headers.get("bar"));
		assertEquals("potatoValue", headers.get("potato").get(0));
	}
}