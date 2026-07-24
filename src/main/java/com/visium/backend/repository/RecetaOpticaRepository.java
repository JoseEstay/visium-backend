package com.visium.backend.repository;

import com.visium.backend.entity.RecetaOptica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a la tabla recetas_opticas.
 */
public interface RecetaOpticaRepository extends JpaRepository<RecetaOptica, UUID> {

	Optional<RecetaOptica> findByConsultaId(UUID consultaId);
}
