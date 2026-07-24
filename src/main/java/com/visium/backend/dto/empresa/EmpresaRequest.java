package com.visium.backend.dto.empresa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Datos para crear o actualizar una empresa.
 */
@Getter
@Setter
public class EmpresaRequest {

	@NotBlank(message = "El RUT es obligatorio")
	@Size(max = 12, message = "El RUT no puede superar 12 caracteres")
	private String rut;

	@NotBlank(message = "La razon social es obligatoria")
	@Size(max = 150)
	private String razonSocial;

	@Size(max = 254)
	private String email;

	private Boolean activo;
}
