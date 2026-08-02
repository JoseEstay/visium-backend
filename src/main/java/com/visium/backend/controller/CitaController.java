package com.visium.backend.controller;

import com.visium.backend.dto.cita.CitaResponse;
import com.visium.backend.service.CitaService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {

	private final CitaService citaService;

	@GetMapping("/profesional/{profesionalId}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'JEFE', 'PROFESIONAL')")
	public ResponseEntity<List<CitaResponse>> listarCitasConfirmadasPorProfesional(
			@PathVariable UUID profesionalId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return ResponseEntity.ok(
				citaService.listarCitasConfirmadasPorProfesional(profesionalId, desde, hasta));
	}
}
