package com.visium.backend.service;

import com.visium.backend.dto.empresa.EmpresaRequest;
import com.visium.backend.dto.empresa.EmpresaResponse;
import com.visium.backend.entity.Empresa;
import com.visium.backend.exception.BadRequestException;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.mapper.EmpresaMapper;
import com.visium.backend.repository.EmpresaRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio de empresas (filtrada por AccesoService).
 */
@Service
@RequiredArgsConstructor
public class EmpresaService {

	private final EmpresaRepository empresaRepository;
	private final EmpresaMapper empresaMapper;
	private final AccesoService accesoService;

	@Transactional(readOnly = true)
	public List<EmpresaResponse> listar() {
		if (accesoService.veTodasLasEmpresas()) {
			return empresaRepository.findAll().stream()
					.map(empresaMapper::toResponse)
					.toList();
		}

		List<UUID> visibles = accesoService.empresaIdsVisibles();
		return empresaRepository.findAll().stream()
				.filter(e -> visibles.contains(e.getId()))
				.map(empresaMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public EmpresaResponse obtenerPorId(UUID id) {
		accesoService.exigirAccesoEmpresa(id);
		return empresaMapper.toResponse(buscarOFallar(id));
	}

	@Transactional
	public EmpresaResponse crear(EmpresaRequest request) {
		accesoService.exigirSuperAdmin();

		empresaRepository.findByRut(request.getRut()).ifPresent(existente -> {
			throw new BadRequestException("Ya existe una empresa con el RUT " + request.getRut());
		});

		Empresa empresa = empresaMapper.toEntity(request);
		if (empresa.getActivo() == null) {
			empresa.setActivo(true);
		}
		return empresaMapper.toResponse(empresaRepository.save(empresa));
	}

	@Transactional
	public EmpresaResponse actualizar(UUID id, EmpresaRequest request) {
		accesoService.exigirAccesoEmpresa(id);
		Empresa empresa = buscarOFallar(id);

		empresaRepository.findByRut(request.getRut()).ifPresent(otra -> {
			if (!otra.getId().equals(id)) {
				throw new BadRequestException("Ya existe otra empresa con el RUT " + request.getRut());
			}
		});

		empresaMapper.aplicar(empresa, request);
		return empresaMapper.toResponse(empresaRepository.save(empresa));
	}

	/** Baja logica: solo SUPER_ADMIN (corte de servicio / no pago). */
	@Transactional
	public void desactivar(UUID id) {
		accesoService.exigirSuperAdmin();
		Empresa empresa = buscarOFallar(id);
		empresa.setActivo(false);
		empresaRepository.save(empresa);
	}

	private Empresa buscarOFallar(UUID id) {
		return empresaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada: " + id));
	}
}
