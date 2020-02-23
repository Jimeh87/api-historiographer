package com.jimrennie.apihistoriographer.service.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(
		webEnvironment = NONE,
		properties = {"spring.jpa.hibernate.ddl-auto=validate"}
)
public class ValidateSchemaTest {

	@Test
	void testValidateSchema() {
	}

}
