package com.jimrennie.apihistoriographer.service.core.applicationproxy;

import com.jimrennie.apihistoriographer.service.controller.api.ApplicationProxyDto;
import com.jimrennie.apihistoriographer.service.core.applicationproxy.ApplicationProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationProxyServiceTest {

	@Autowired
	private ApplicationProxyService applicationProxyService;

	@Test
	void testGetConfigWithAllValuesSet() {
		ApplicationProxyDto config = applicationProxyService.getConfig("TEST1");
		assertAll(
				() -> assertEquals("TEST1", config.getApplication()),
				() -> assertEquals("https", config.getScheme()),
				() -> assertEquals("jimrennie.com", config.getHost()),
				() -> assertEquals(8080, config.getPort()),
				() -> assertArrayEquals(new String[]{"cookie", "accept-encoding"}, config.getHeaderBlacklist().toArray(new String[0]))
		);
	}

	@Test
	void testGetConfigWithNoValuesSet() {
		ApplicationProxyDto config = applicationProxyService.getConfig("TEST2");
		assertAll(
				() -> assertEquals("TEST2", config.getApplication()),
				() -> assertEquals("http", config.getScheme()),
				() -> assertNull(config.getHost()),
				() -> assertEquals(80, config.getPort()),
				() -> assertArrayEquals(new String[]{}, config.getHeaderBlacklist().toArray(new String[0]))
		);
	}

	@Test
	void testConfigDoesNotExist() {
		assertThrows(IllegalArgumentException.class, () -> applicationProxyService.getConfig("YUNOEXISTCONFIG"));
	}
}