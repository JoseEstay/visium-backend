package com.visium.backend.repository;

import com.visium.backend.entity.RecetaOpticaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a la tabla recetas_opticas_detalles.
 */
public interface RecetaOpticaDetalleRepository
		extends JpaRepository<RecetaOpticaDetalle, RecetaOpticaDetalle.RecetaOpticaDetalleId> {
}
