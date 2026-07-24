package com.visium.backend.repository;

import com.visium.backend.entity.FichaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a la tabla fichas_clinicas.
 */
public interface FichaClinicaRepository extends JpaRepository<FichaClinica, UUID> {

	Optional<FichaClinica> findByPacienteId(UUID pacienteId);
}
