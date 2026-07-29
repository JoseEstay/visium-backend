package com.visium.backend.controller;

import com.visium.backend.dto.sucursal.SucursalRequest;
import com.visium.backend.dto.sucursal.SucursalResponse;
import com.visium.backend.service.SucursalService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de sucursales.
 * Mutaciones: SUPER_ADMIN o JEFE. JEFE_SUCURSAL solo lee las suyas (AccesoService).
 */
@RestController
@RequestMapping("/sucursales")
@RequiredArgsConstructor
public class SucursalController {

	private final SucursalService sucursalService;

	@GetMapping
	public ResponseEntity<List<SucursalResponse>> listar(@RequestParam UUID empresaId) {
		return ResponseEntity.ok(sucursalService.listarPorEmpresa(empresaId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SucursalResponse> obtener(@PathVariable UUID id) {
		return ResponseEntity.ok(sucursalService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<SucursalResponse> crear(@Valid @RequestBody SucursalRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.crear(request));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<SucursalResponse> actualizar(
			@PathVariable UUID id,
			@Valid @RequestBody SucursalRequest request
	) {
		return ResponseEntity.ok(sucursalService.actualizar(id, request));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
		sucursalService.desactivar(id);
		return ResponseEntity.noContent().build();
	}
}
