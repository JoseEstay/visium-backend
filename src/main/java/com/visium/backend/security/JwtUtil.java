package com.visium.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Crea y lee tokens JWT (el "pase" de sesion). */
@Component
public class JwtUtil {

	private final SecretKey key;
	private final long expirationMs;

	public JwtUtil(
			@Value("${visium.jwt.secret}") String secret,
			@Value("${visium.jwt.expiration-ms}") long expirationMs
	) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	/**
	 * Crea un token con usuario, roles, empresas y sucursales.
	 */
	public String generarToken(
			UUID usuarioId,
			String email,
			List<String> roles,
			List<UUID> empresaIds,
			List<UUID> sucursalIds
	) {
		Date ahora = new Date();
		Date expiracion = new Date(ahora.getTime() + expirationMs);

		return Jwts.builder()
				.subject(email)
				.claim("usuarioId", usuarioId.toString())
				.claim("roles", roles)
				.claim("empresaIds", aStringList(empresaIds))
				.claim("sucursalIds", aStringList(sucursalIds))
				.issuedAt(ahora)
				.expiration(expiracion)
				.signWith(key)
				.compact();
	}

	public String extraerEmail(String token) {
		return parsear(token).getSubject();
	}

	public boolean esValido(String token) {
		try {
			parsear(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> extraerRoles(String token) {
		return (List<String>) parsear(token).get("roles", List.class);
	}

	public List<UUID> extraerEmpresaIds(String token) {
		return aUuidList(parsear(token).get("empresaIds", List.class));
	}

	public List<UUID> extraerSucursalIds(String token) {
		return aUuidList(parsear(token).get("sucursalIds", List.class));
	}

	private Claims parsear(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	private List<String> aStringList(List<UUID> ids) {
		List<String> out = new ArrayList<>();
		if (ids == null) {
			return out;
		}
		for (UUID id : ids) {
			out.add(id.toString());
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	private List<UUID> aUuidList(List<?> raw) {
		List<UUID> out = new ArrayList<>();
		if (raw == null) {
			return out;
		}
		for (Object item : raw) {
			if (item != null) {
				out.add(UUID.fromString(item.toString()));
			}
		}
		return out;
	}
}
