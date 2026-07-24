package com.visium.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Datos que envia el frontend para iniciar sesion.
 */
@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email no es valido")
	private String email;

	@NotBlank(message = "La contrasena es obligatoria")
	private String password;
}
