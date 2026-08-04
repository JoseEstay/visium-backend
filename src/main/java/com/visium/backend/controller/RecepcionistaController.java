package com.visium.backend.controller;

import com.visium.backend.dto.recepcionista.RecepcionistaRequest;
import com.visium.backend.dto.recepcionista.RecepcionistaResponse;
import com.visium.backend.dto.usuario.CambiarEstadoRequest;
import com.visium.backend.service.RecepcionistaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gestion de recepcionistas (usuarios con rol RECEPCIONISTA).
 * Solo SUPER_ADMIN y JEFE operan este modulo.
 */
@RestController
@RequestMapping("/recepcionistas")
@RequiredArgsConstructor
public class RecepcionistaController {

	private final RecepcionistaService recepcionistaService;

	@GetMapping
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<List<RecepcionistaResponse>> listar() {
		return ResponseEntity.ok(recepcionistaService.listar());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<RecepcionistaResponse> obtener(@PathVariable UUID id) {
		return ResponseEntity.ok(recepcionistaService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<RecepcionistaResponse> crear(
			@Valid @RequestBody RecepcionistaRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(recepcionistaService.crear(request));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<RecepcionistaResponse> editar(
			@PathVariable UUID id, @Valid @RequestBody RecepcionistaRequest request) {
		return ResponseEntity.ok(recepcionistaService.editar(id, request));
	}

	@PatchMapping("/{id}/estado")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<Void> cambiarEstado(
			@PathVariable UUID id, @Valid @RequestBody CambiarEstadoRequest request) {
		recepcionistaService.cambiarEstado(id, Boolean.TRUE.equals(request.getActivo()));
		return ResponseEntity.noContent().build();
	}
}