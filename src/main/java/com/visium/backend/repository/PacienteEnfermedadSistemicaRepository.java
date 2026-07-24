package com.visium.backend.repository;

import com.visium.backend.entity.PacienteEnfermedadSistemica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Acceso a la tabla pacientes_enfermedades_sistemicas.
 */
public interface PacienteEnfermedadSistemicaRepository
		extends JpaRepository<PacienteEnfermedadSistemica, PacienteEnfermedadSistemica.PacienteEnfermedadSistemicaId> {

	List<PacienteEnfermedadSistemica> findByPacienteId(UUID pacienteId);
}
