package com.visium.backend.repository;

import com.visium.backend.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Acceso a la tabla sucursales.
 */
public interface SucursalRepository extends JpaRepository<Sucursal, UUID> {

	// Lista las sucursales de una empresa
	List<Sucursal> findByEmpresaId(UUID empresaId);
}
