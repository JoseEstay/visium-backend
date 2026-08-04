package com.visium.backend.controller;

import com.visium.backend.dto.usuario.CambiarEstadoRequest;
import com.visium.backend.dto.usuario.UsuarioRequest;
import com.visium.backend.dto.usuario.UsuarioResponse;
import com.visium.backend.service.UsuarioService;
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
 * Gestion de usuarios. Los usuarios nunca se eliminan: solo se activan/desactivan.
 * Solo SUPER_ADMIN y JEFE operan este modulo.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;

	@GetMapping
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<List<UsuarioResponse>> listar() {
		return ResponseEntity.ok(usuarioService.listar());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<UsuarioResponse> obtener(@PathVariable UUID id) {
		return ResponseEntity.ok(usuarioService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<UsuarioResponse> editar(
			@PathVariable UUID id, @Valid @RequestBody UsuarioRequest request) {
		return ResponseEntity.ok(usuarioService.editar(id, request));
	}

	@PatchMapping("/{id}/estado")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE')")
	public ResponseEntity<Void> cambiarEstado(
			@PathVariable UUID id, @Valid @RequestBody CambiarEstadoRequest request) {
		usuarioService.cambiarEstado(id, Boolean.TRUE.equals(request.getActivo()));
		return ResponseEntity.noContent().build();
	}
}