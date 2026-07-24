package com.visium.backend.repository;

import com.visium.backend.entity.EnfermedadSistemica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a la tabla enfermedades_sistemicas.
 */
public interface EnfermedadSistemicaRepository extends JpaRepository<EnfermedadSistemica, Short> {

	Optional<EnfermedadSistemica> findByCodigo(String codigo);
}
