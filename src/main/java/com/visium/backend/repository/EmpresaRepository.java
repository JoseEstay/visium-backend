package com.visium.backend.repository;

import com.visium.backend.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a la tabla empresas.
 * JpaRepository ya trae metodos como findById, save, delete, etc.
 */
public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

	// Busca una empresa por su RUT
	Optional<Empresa> findByRut(String rut);
}
