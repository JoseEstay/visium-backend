package com.visium.backend.service;

import com.visium.backend.dto.sucursal.SucursalRequest;
import com.visium.backend.dto.sucursal.SucursalResponse;
import com.visium.backend.entity.Empresa;
import com.visium.backend.entity.Sucursal;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.mapper.SucursalMapper;
import com.visium.backend.repository.EmpresaRepository;
import com.visium.backend.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SucursalService {

	private final SucursalRepository sucursalRepository;
	private final EmpresaRepository empresaRepository;
	private final SucursalMapper sucursalMapper;

	@Transactional(readOnly = true)
	public List<SucursalResponse> listarPorEmpresa(UUID empresaId) {
		verificarEmpresa(empresaId);
		return sucursalRepository.findByEmpresaId(empresaId).stream()
				.map(sucursalMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public SucursalResponse obtenerPorId(UUID id) {
		return sucursalMapper.toResponse(buscarOFallar(id));
	}

	@Transactional
	public SucursalResponse crear(SucursalRequest request) {
		Empresa empresa = verificarEmpresa(request.getEmpresaId());
		Sucursal sucursal = sucursalMapper.toEntity(request, empresa);
		if (sucursal.getActivo() == null) {
			sucursal.setActivo(true);
		}
		return sucursalMapper.toResponse(sucursalRepository.save(sucursal));
	}

	@Transactional
	public SucursalResponse actualizar(UUID id, SucursalRequest request) {
		Sucursal sucursal = buscarOFallar(id);
		Empresa empresa = verificarEmpresa(request.getEmpresaId());
		sucursal.setEmpresa(empresa);
		sucursalMapper.aplicar(sucursal, request);
		return sucursalMapper.toResponse(sucursalRepository.save(sucursal));
	}

	@Transactional
	public void desactivar(UUID id) {
		Sucursal sucursal = buscarOFallar(id);
		sucursal.setActivo(false);
		sucursalRepository.save(sucursal);
	}

	private Empresa verificarEmpresa(UUID empresaId) {
		return empresaRepository.findById(empresaId)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada: " + empresaId));
	}

	private Sucursal buscarOFallar(UUID id) {
		return sucursalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada: " + id));
	}
}
