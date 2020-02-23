package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationProxyServiceTest {

	@Autowired
	private ApplicationProxyService applicationProxyService;

	@BeforeEach
	void seedData() {
		applicationProxyService.save(new ApplicationProxyDto()
				.setApplication("TEST1")
				.setScheme("https")
				.setHost("jimrennie.com")
				.setPort(8080)
				.addHeaderBlacklist("cookie", "*")
				.addHeaderBlacklist("accept-encoding", "*"));

		applicationProxyService.save(new ApplicationProxyDto()
				.setApplication("TEST2")
				.setHost("foo"));
	}

	@Test
	void testGetWithAllValuesSet() {
		ApplicationProxyDto applicationProxy = applicationProxyService.get("TEST1");
		assertAll(
				() -> assertEquals("TEST1", applicationProxy.getApplication()),
				() -> assertEquals("https", applicationProxy.getScheme()),
				() -> assertEquals("jimrennie.com", applicationProxy.getHost()),
				() -> assertEquals(8080, applicationProxy.getPort()),
				() -> assertArrayEquals(new String[]{"cookie", "accept-encoding"}, applicationProxy.getHeaderBlacklist().keySet().toArray(new String[0]))
		);
	}

	@Test
	void testGetWithNoValuesSet() {
		ApplicationProxyDto applicationProxy = applicationProxyService.get("TEST2");
		assertAll(
				() -> assertEquals("TEST2", applicationProxy.getApplication()),
				() -> assertEquals("http", applicationProxy.getScheme()),
				() -> assertEquals("foo", applicationProxy.getHost()),
				() -> assertEquals(80, applicationProxy.getPort()),
				() -> assertArrayEquals(new String[]{}, applicationProxy.getHeaderBlacklist().keySet().toArray(new String[0]))
		);
	}

	@Test
	void testGetDoesNotExist() {
		assertThrows(IllegalArgumentException.class, () -> applicationProxyService.get("YUNOEXIST"));
	}

}