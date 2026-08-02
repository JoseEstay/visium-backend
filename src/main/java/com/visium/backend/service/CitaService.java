package com.visium.backend.service;

import com.visium.backend.dto.cita.CitaResponse;
import com.visium.backend.entity.Cita;
import com.visium.backend.entity.Profesional;
import com.visium.backend.entity.UsuarioEmpresa;
import com.visium.backend.enums.EstadoCita;
import com.visium.backend.exception.BadRequestException;
import com.visium.backend.exception.ForbiddenException;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.mapper.CitaMapper;
import com.visium.backend.repository.CitaRepository;
import com.visium.backend.repository.ProfesionalRepository;
import com.visium.backend.repository.UsuarioEmpresaRepository;
import com.visium.backend.security.UsuarioDetails;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CitaService {

	private final CitaRepository citaRepository;
	private final ProfesionalRepository profesionalRepository;
	private final UsuarioEmpresaRepository usuarioEmpresaRepository;
	private final CitaMapper citaMapper;
	private final AccesoService accesoService;

	@Transactional(readOnly = true)
	public List<CitaResponse> listarCitasConfirmadasPorProfesional(
			UUID profesionalId, LocalDate desde, LocalDate hasta) {
		validarRango(desde, hasta);
		Profesional profesional = profesionalRepository.findById(profesionalId)
				.orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado: " + profesionalId));

		UsuarioDetails usuario = accesoService.usuarioActual();
		validarProfesional(profesionalId, usuario);
		validarEmpresaDeProfesional(profesional, usuario);

		Instant inicio = desde.atStartOfDay().toInstant(ZoneOffset.UTC);
		Instant fin = hasta.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);

		return citaRepository
				.findByProfesionalIdAndFechaHoraInicioBetweenAndEstado(
						profesionalId, inicio, fin, EstadoCita.CONFIRMADA)
				.stream()
				.filter(this::tieneAccesoACita)
				.map(citaMapper::toResponse)
				.toList();
	}

	private boolean tieneAccesoACita(Cita cita) {
		try {
			accesoService.exigirAccesoSucursal(cita.getEmpresaId(), cita.getSucursal().getId());
			return true;
		} catch (ForbiddenException ex) {
			return false;
		}
	}

	private void validarRango(LocalDate desde, LocalDate hasta) {
		if (desde == null || hasta == null) {
			throw new BadRequestException("Debes indicar desde y hasta para filtrar por semana");
		}
		if (hasta.isBefore(desde)) {
			throw new BadRequestException("El rango de fechas es invalido: hasta no puede ser antes de desde");
		}
	}

	private void validarProfesional(UUID profesionalId, UsuarioDetails usuario) {
		if (accesoService.esSuperAdmin() || accesoService.esJefeDeEmpresa()) {
			return;
		}
		if (!usuario.getId().equals(profesionalId)) {
			throw new ForbiddenException("Solo puedes consultar tus propias citas");
		}
	}

	private void validarEmpresaDeProfesional(Profesional profesional, UsuarioDetails usuario) {
		if (accesoService.esSuperAdmin()) {
			return;
		}
		List<UsuarioEmpresa> pertenencias = usuarioEmpresaRepository.findByUsuarioId(profesional.getUsuario().getId());
		if (pertenencias.isEmpty()) {
			throw new ForbiddenException("El profesional no tiene empresa asignada");
		}
		for (UsuarioEmpresa pertenencia : pertenencias) {
			accesoService.exigirAccesoEmpresa(pertenencia.getEmpresa().getId());
		}
	}
}
