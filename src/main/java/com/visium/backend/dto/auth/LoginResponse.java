package com.visium.backend.dto.auth;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Respuesta del login: token + datos basicos + alcance (empresas/sucursales).
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
	private List<UUID> empresaIds;
	private List<UUID> sucursalIds;
	/** Empresa activa sugerida (la unica, o null si tiene varias y debe enviar X-Empresa-Id). */
	private UUID empresaActivaId;
}
