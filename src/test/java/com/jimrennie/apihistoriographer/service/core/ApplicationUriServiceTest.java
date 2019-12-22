package com.jimrennie.apihistoriographer.service.core;

import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfig;
import com.jimrennie.apihistoriographer.service.core.config.ApplicationProxyConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationUriService.class)
class ApplicationUriServiceTest {

	@MockBean
	private ApplicationProxyConfigService applicationProxyConfigService;
	@Autowired
	private ApplicationUriService applicationUriService;

	@Test
	void testApplicationUriService() {
		when(applicationProxyConfigService.getConfig(any())).thenReturn(
				new ApplicationProxyConfig()
						.setApplication("ANY")
						.setScheme("https")
						.setHost("jimrennie.com")
						.setPort(1234));

		URI uri = applicationUriService.create("ANY", "abc/123", "foo=bar");
		assertEquals("https://jimrennie.com:1234/abc/123?foo=bar", uri.toString());
	}

	@Test
	void testApplicationUriService_NoQueryOrPath() {
		when(applicationProxyConfigService.getConfig(any())).thenReturn(
				new ApplicationProxyConfig()
						.setApplication("ANY")
						.setScheme("https")
						.setHost("jimrennie.com")
						.setPort(1234));

		URI uri = applicationUriService.create("ANY", null, null);
		assertEquals("https://jimrennie.com:1234", uri.toString());
	}

}