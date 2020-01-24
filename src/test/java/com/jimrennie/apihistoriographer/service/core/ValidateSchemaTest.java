package com.jimrennie.apihistoriographer.service.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {"spring.jpa.hibernate.ddl-auto=validate"}
)
public class ValidateSchemaTest {

	@Test
	void testValidateSchema() {
	}

}
