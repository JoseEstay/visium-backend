package com.visium.backend.dto.auth;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
	private List<UUID> empresaIds;
	private List<UUID> sucursalIds;
	private UUID empresaActivaId;
}
