package com.visium.backend.dto.empresa;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Datos de una empresa que se envian al frontend.
 */
@Getter
@Builder
public class EmpresaResponse {

	private UUID id;
	private String rut;
	private String razonSocial;
	private String email;
	private Boolean activo;
	private Instant createdAt;
	private Instant updatedAt;
}
