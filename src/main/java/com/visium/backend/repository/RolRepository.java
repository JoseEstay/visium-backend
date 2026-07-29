package com.visium.backend.repository;

import com.visium.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a la tabla roles.
 */
public interface RolRepository extends JpaRepository<Rol, Short> {

	// Busca un rol por su codigo, ejemplo: "JEFE"
	Optional<Rol> findByCodigo(String codigo);
}
