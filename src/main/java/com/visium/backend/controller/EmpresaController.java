package com.visium.backend.controller;

import com.visium.backend.dto.empresa.EmpresaRequest;
import com.visium.backend.dto.empresa.EmpresaResponse;
import com.visium.backend.service.EmpresaService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de empresas.
 * Crear / desactivar: SUPER_ADMIN. Actualizar: SUPER_ADMIN o JEFE (AccesoService limita alcance).
 */
@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

	private final EmpresaService empresaService;

	@GetMapping("/")
	public ResponseEntity<List<EmpresaResponse>> listar() {
		return ResponseEntity.ok(empresaService.listar());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmpresaResponse> obtener(@PathVariable UUID id) {
		return ResponseEntity.ok(empresaService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<EmpresaResponse> crear(@Valid @RequestBody EmpresaRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.crear(request));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<EmpresaResponse> actualizar(
			@PathVariable UUID id, @Valid @RequestBody EmpresaRequest request) {
		return ResponseEntity.ok(empresaService.actualizar(id, request));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
		empresaService.desactivar(id);
		return ResponseEntity.noContent().build();
	}
}
