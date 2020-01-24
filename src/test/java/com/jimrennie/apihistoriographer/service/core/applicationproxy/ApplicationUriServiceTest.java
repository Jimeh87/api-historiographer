package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationProxyService;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationUriService;
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
	private ApplicationProxyService applicationProxyService;
	@Autowired
	private ApplicationUriService applicationUriService;

	@Test
	void testApplicationUriService() {
		when(applicationProxyService.getConfig(any())).thenReturn(
				new ApplicationProxyDto()
						.setApplication("ANY")
						.setScheme("https")
						.setHost("jimrennie.com")
						.setPort(1234));

		URI uri = applicationUriService.create("ANY", "abc/123", "foo=bar");
		assertEquals("https://jimrennie.com:1234/abc/123?foo=bar", uri.toString());
	}

	@Test
	void testApplicationUriService_NoQueryOrPath() {
		when(applicationProxyService.getConfig(any())).thenReturn(
				new ApplicationProxyDto()
						.setApplication("ANY")
						.setScheme("https")
						.setHost("jimrennie.com")
						.setPort(1234));

		URI uri = applicationUriService.create("ANY", null, null);
		assertEquals("https://jimrennie.com:1234", uri.toString());
	}

}