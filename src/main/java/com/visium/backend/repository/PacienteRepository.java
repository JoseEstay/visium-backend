package com.visium.backend.repository;

import com.visium.backend.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Acceso a la tabla pacientes.
 */
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

	List<Paciente> findByEmpresaId(UUID empresaId);
}
