package com.visium.backend.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.visium.backend.entity.Usuario;
import com.visium.backend.exception.BadRequestException;
import com.visium.backend.exception.ForbiddenException;
import com.visium.backend.security.EmpresaContext;
import com.visium.backend.security.UsuarioDetails;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Verifica el aislamiento multi-empresa / multi-sucursal sin base de datos.
 */
class AccesoServiceTest {

	private static final UUID EMPRESA_A = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	private static final UUID EMPRESA_B = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
	private static final UUID EMPRESA_C = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
	private static final UUID SUCURSAL_A1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
	private static final UUID SUCURSAL_A2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

	private final AccesoService accesoService = new AccesoService();

	@BeforeEach
	void limpiar() {
		SecurityContextHolder.clearContext();
		EmpresaContext.clear();
	}

	@AfterEach
	void despues() {
		SecurityContextHolder.clearContext();
		EmpresaContext.clear();
	}

	@Test
	void superAdminPuedeAccederCualquierEmpresa() {
		autenticar(roles("SUPER_ADMIN"), List.of(), List.of());

		assertTrue(accesoService.puedeAccederEmpresa(EMPRESA_C));
		assertDoesNotThrow(() -> accesoService.exigirAccesoEmpresa(EMPRESA_C));
		assertTrue(accesoService.veTodasLasEmpresas());
		assertTrue(accesoService.empresaIdsVisibles().isEmpty());
	}

	@Test
	void jefeSoloAccedeSusEmpresas() {
		autenticar(roles("JEFE"), List.of(EMPRESA_A, EMPRESA_B), List.of());

		assertTrue(accesoService.puedeAccederEmpresa(EMPRESA_A));
		assertTrue(accesoService.puedeAccederEmpresa(EMPRESA_B));
		assertFalse(accesoService.puedeAccederEmpresa(EMPRESA_C));
		assertThrows(ForbiddenException.class, () -> accesoService.exigirAccesoEmpresa(EMPRESA_C));
		assertEquals(List.of(EMPRESA_A, EMPRESA_B), accesoService.empresaIdsVisibles());
	}

	@Test
	void adminLegacySeTrataComoJefe() {
		autenticar(roles("ADMIN"), List.of(EMPRESA_A), List.of(SUCURSAL_A1));

		assertTrue(accesoService.esJefeDeEmpresa());
		assertFalse(accesoService.tieneAlcanceSoloSucursalesAsignadas());
		assertTrue(accesoService.sucursalIdsVisiblesEnEmpresa().isEmpty());
	}

	@Test
	void jefeSucursalSoloVeSucursalesAsignadas() {
		autenticar(roles("JEFE_SUCURSAL"), List.of(EMPRESA_A), List.of(SUCURSAL_A1));

		assertDoesNotThrow(() -> accesoService.exigirAccesoSucursal(EMPRESA_A, SUCURSAL_A1));
		assertThrows(
				ForbiddenException.class,
				() -> accesoService.exigirAccesoSucursal(EMPRESA_A, SUCURSAL_A2));
		assertEquals(List.of(SUCURSAL_A1), accesoService.sucursalIdsVisiblesEnEmpresa());
	}

	@Test
	void jefeVeTodasLasSucursalesDeSuEmpresa() {
		autenticar(roles("JEFE"), List.of(EMPRESA_A), List.of());

		assertDoesNotThrow(() -> accesoService.exigirAccesoSucursal(EMPRESA_A, SUCURSAL_A2));
		assertTrue(accesoService.sucursalIdsVisiblesEnEmpresa().isEmpty());
	}

	@Test
	void multiEmpresaSinHeaderFalla() {
		autenticar(roles("JEFE"), List.of(EMPRESA_A, EMPRESA_B), List.of());

		assertThrows(BadRequestException.class, () -> accesoService.resolverEmpresaObjetivo(null));
	}

	@Test
	void multiEmpresaUsaContextoOParametro() {
		autenticar(roles("JEFE"), List.of(EMPRESA_A, EMPRESA_B), List.of());

		assertEquals(EMPRESA_B, accesoService.resolverEmpresaObjetivo(EMPRESA_B));

		EmpresaContext.setEmpresaId(EMPRESA_A);
		assertEquals(EMPRESA_A, accesoService.resolverEmpresaObjetivo(null));
	}

	@Test
	void unaSolaEmpresaSeResuelveSola() {
		autenticar(roles("JEFE"), List.of(EMPRESA_A), List.of());

		assertEquals(EMPRESA_A, accesoService.resolverEmpresaObjetivo(null));
	}

	@Test
	void superAdminSinEmpresaIndicadaFalla() {
		autenticar(roles("SUPER_ADMIN"), List.of(), List.of());

		assertThrows(BadRequestException.class, () -> accesoService.resolverEmpresaObjetivo(null));
	}

	@Test
	void exigirSuperAdminRechazaJefe() {
		autenticar(roles("JEFE"), List.of(EMPRESA_A), List.of());

		assertThrows(ForbiddenException.class, accesoService::exigirSuperAdmin);
	}

	@Test
	void sinUsuarioAutenticadoFalla() {
		assertThrows(ForbiddenException.class, accesoService::usuarioActual);
	}

	private void autenticar(List<String> roles, List<UUID> empresas, List<UUID> sucursales) {
		Usuario usuario = new Usuario();
		usuario.setId(UUID.randomUUID());
		usuario.setEmail("test@visium.cl");
		usuario.setPasswordHash("hash");
		usuario.setActivo(true);
		usuario.setNombre("Test");
		usuario.setApellido("User");

		UsuarioDetails detalles = new UsuarioDetails(usuario, roles, empresas, sucursales);
		UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(detalles, null, detalles.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private static List<String> roles(String... codigos) {
		return List.of(codigos);
	}
}
