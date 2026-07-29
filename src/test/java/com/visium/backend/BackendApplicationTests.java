package com.visium.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke: arranca el contexto Spring contra PostgreSQL (.env + Docker).
 * Requiere contenedor visium-postgres y archivo .env en la raiz del proyecto.
 */
@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
