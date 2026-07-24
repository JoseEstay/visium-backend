package com.visium.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Respuesta del login: token + datos basicos del usuario.
 */
@Getter
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private UUID usuarioId;
	private String email;
	private String nombre;
	private String apellido;
	private List<String> roles;
}
