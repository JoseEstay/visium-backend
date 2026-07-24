package com.visium.backend.controller;

import com.visium.backend.dto.paciente.PacienteRequest;
import com.visium.backend.dto.paciente.PacienteResponse;
import com.visium.backend.service.PacienteService;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

	private final PacienteService pacienteService;

	@GetMapping
	public ResponseEntity<List<PacienteResponse>> listar(@RequestParam UUID empresaId) {
		return ResponseEntity.ok(pacienteService.listarPorEmpresa(empresaId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PacienteResponse> obtener(@PathVariable UUID id) {
		return ResponseEntity.ok(pacienteService.obtenerPorId(id));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
	public ResponseEntity<PacienteResponse> crear(@Valid @RequestBody PacienteRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.crear(request));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
	public ResponseEntity<PacienteResponse> actualizar(
			@PathVariable UUID id,
			@Valid @RequestBody PacienteRequest request
	) {
		return ResponseEntity.ok(pacienteService.actualizar(id, request));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
		pacienteService.desactivar(id);
		return ResponseEntity.noContent().build();
	}
}
