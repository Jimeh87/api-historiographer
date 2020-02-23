package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
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
	private ApplicationProxyService applicationProxyService;
	@Autowired
	private ApplicationHeaderService applicationHeaderService;

	@Test
	void testApplicationUriService() {
		when(applicationProxyService.get(any())).thenReturn(
				new ApplicationProxyDto()
						.setApplication("ANY")
						.setHost("asdf")
						.addHeaderBlacklist("foo", "*")
						.addHeaderBlacklist("bar", "*"));


		HttpHeaders headers = new HttpHeaders();
		headers.set("bar", "barValue");
		headers.set("potato", "potatoValue");

		HttpHeaders newHeaders = applicationHeaderService.getHeaders("ANY", headers);

		assertNull(headers.get("foo"));
		assertNull(headers.get("bar"));
		assertEquals("potatoValue", headers.get("potato").get(0));
	}
}