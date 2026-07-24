package com.visium.backend.service;

import com.visium.backend.dto.empresa.EmpresaRequest;
import com.visium.backend.dto.empresa.EmpresaResponse;
import com.visium.backend.entity.Empresa;
import com.visium.backend.exception.BadRequestException;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.mapper.EmpresaMapper;
import com.visium.backend.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Logica de negocio de empresas.
 */
@Service
@RequiredArgsConstructor
public class EmpresaService {

	private final EmpresaRepository empresaRepository;
	private final EmpresaMapper empresaMapper;

	@Transactional(readOnly = true)
	public List<EmpresaResponse> listar() {
		return empresaRepository.findAll().stream()
				.map(empresaMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public EmpresaResponse obtenerPorId(UUID id) {
		return empresaMapper.toResponse(buscarOFallar(id));
	}

	@Transactional
	public EmpresaResponse crear(EmpresaRequest request) {
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
		Empresa empresa = buscarOFallar(id);

		empresaRepository.findByRut(request.getRut()).ifPresent(otra -> {
			if (!otra.getId().equals(id)) {
				throw new BadRequestException("Ya existe otra empresa con el RUT " + request.getRut());
			}
		});

		empresaMapper.aplicar(empresa, request);
		return empresaMapper.toResponse(empresaRepository.save(empresa));
	}

	// Baja logica: no borra la fila, solo marca activo = false
	@Transactional
	public void desactivar(UUID id) {
		Empresa empresa = buscarOFallar(id);
		empresa.setActivo(false);
		empresaRepository.save(empresa);
	}

	private Empresa buscarOFallar(UUID id) {
		return empresaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada: " + id));
	}
}
