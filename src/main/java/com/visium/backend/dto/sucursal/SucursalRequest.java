package com.visium.backend.dto.sucursal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Datos para crear o actualizar una sucursal.
 */
@Getter
@Setter
public class SucursalRequest {

	@NotNull(message = "La empresa es obligatoria")
	private UUID empresaId;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 120)
	private String nombre;

	@NotBlank(message = "La direccion es obligatoria")
	private String direccion;

	@Size(max = 100)
	private String comuna;

	@Size(max = 100)
	private String ciudad;

	@Size(max = 100)
	private String region;

	@Size(max = 30)
	private String telefono;

	private Boolean activo;
}
