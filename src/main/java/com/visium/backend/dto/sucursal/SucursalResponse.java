package com.visium.backend.dto.sucursal;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class SucursalResponse {

	private UUID id;
	private UUID empresaId;
	private String nombre;
	private String direccion;
	private String comuna;
	private String ciudad;
	private String region;
	private String telefono;
	private Boolean activo;
	private Instant createdAt;
	private Instant updatedAt;
}
