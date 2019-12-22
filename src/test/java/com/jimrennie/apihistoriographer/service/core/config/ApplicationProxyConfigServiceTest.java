package com.jimrennie.apihistoriographer.service.core.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationProxyConfigServiceTest {

	@Autowired
	private ApplicationProxyConfigService applicationProxyConfigService;

	@Test
	void testGetConfigWithAllValuesSet() {
		ApplicationProxyConfig config = applicationProxyConfigService.getConfig("TEST1");
		assertAll(
				() -> assertEquals("TEST1", config.getApplication()),
				() -> assertEquals("https", config.getScheme()),
				() -> assertEquals("jimrennie.com", config.getHost()),
				() -> assertEquals(8080, config.getPort()),
				() -> assertArrayEquals(new String[]{"cookie", "accept-encoding"}, config.getHeaderBlacklist().toArray(new String[0])),
				() -> assertEquals(15, config.getPollingIntervalMinutes())
		);
	}

	@Test
	void testGetConfigWithNoValuesSet() {
		ApplicationProxyConfig config = applicationProxyConfigService.getConfig("TEST2");
		assertAll(
				() -> assertEquals("TEST2", config.getApplication()),
				() -> assertEquals("http", config.getScheme()),
				() -> assertNull(config.getHost()),
				() -> assertEquals(80, config.getPort()),
				() -> assertArrayEquals(new String[]{}, config.getHeaderBlacklist().toArray(new String[0])),
				() -> assertEquals(60, config.getPollingIntervalMinutes())
		);
	}

	@Test
	void testConfigDoesNotExist() {
		assertThrows(IllegalArgumentException.class, () -> applicationProxyConfigService.getConfig("YUNOEXISTCONFIG"));
	}
}