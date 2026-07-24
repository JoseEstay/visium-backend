package com.visium.backend.service;

import com.visium.backend.dto.paciente.PacienteRequest;
import com.visium.backend.dto.paciente.PacienteResponse;
import com.visium.backend.entity.Empresa;
import com.visium.backend.entity.FichaClinica;
import com.visium.backend.entity.Paciente;
import com.visium.backend.exception.ResourceNotFoundException;
import com.visium.backend.mapper.PacienteMapper;
import com.visium.backend.repository.EmpresaRepository;
import com.visium.backend.repository.FichaClinicaRepository;
import com.visium.backend.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

	private final PacienteRepository pacienteRepository;
	private final EmpresaRepository empresaRepository;
	private final FichaClinicaRepository fichaClinicaRepository;
	private final PacienteMapper pacienteMapper;

	@Transactional(readOnly = true)
	public List<PacienteResponse> listarPorEmpresa(UUID empresaId) {
		verificarEmpresa(empresaId);
		return pacienteRepository.findByEmpresaId(empresaId).stream()
				.map(pacienteMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public PacienteResponse obtenerPorId(UUID id) {
		return pacienteMapper.toResponse(buscarOFallar(id));
	}

	@Transactional
	public PacienteResponse crear(PacienteRequest request) {
		Empresa empresa = verificarEmpresa(request.getEmpresaId());
		Paciente paciente = pacienteMapper.toEntity(request, empresa);
		if (paciente.getActivo() == null) {
			paciente.setActivo(true);
		}
		paciente = pacienteRepository.save(paciente);

		// Cada paciente nuevo nace con una ficha clinica vacia
		FichaClinica ficha = new FichaClinica();
		ficha.setPaciente(paciente);
		fichaClinicaRepository.save(ficha);

		return pacienteMapper.toResponse(paciente);
	}

	@Transactional
	public PacienteResponse actualizar(UUID id, PacienteRequest request) {
		Paciente paciente = buscarOFallar(id);
		Empresa empresa = verificarEmpresa(request.getEmpresaId());
		paciente.setEmpresa(empresa);
		pacienteMapper.aplicar(paciente, request);
		return pacienteMapper.toResponse(pacienteRepository.save(paciente));
	}

	@Transactional
	public void desactivar(UUID id) {
		Paciente paciente = buscarOFallar(id);
		paciente.setActivo(false);
		pacienteRepository.save(paciente);
	}

	private Empresa verificarEmpresa(UUID empresaId) {
		return empresaRepository.findById(empresaId)
				.orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada: " + empresaId));
	}

	private Paciente buscarOFallar(UUID id) {
		return pacienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado: " + id));
	}
}
