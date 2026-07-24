package com.visium.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Datos del usuario autenticado (endpoint /auth/me).
 */
@Getter
@AllArgsConstructor
public class MeResponse {

	private UUID usuarioId;
	private String email;
	private String nombre;
	private String apellido;
	private List<String> roles;
}
