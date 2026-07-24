package com.visium.backend.enums;

/**
 * Estados posibles de una cita.
 * Deben coincidir con el CHECK de la base de datos.
 */
public enum EstadoCita {
	PENDIENTE,
	CONFIRMADA,
	ATENDIDA,
	CANCELADA,
	NO_ASISTIO
}
