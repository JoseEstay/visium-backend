package com.visium.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
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
      @Value("${visium.jwt.expiration-ms}") long expirationMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  // Crea un token con el email, id y roles del usuario
  public String generarToken(UUID usuarioId, String email, List<String> roles) {
    Date ahora = new Date();
    Date expiracion = new Date(ahora.getTime() + expirationMs);

    return Jwts.builder()
        .subject(email)
        .claim("usuarioId", usuarioId.toString())
        .claim("roles", roles)
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

  private Claims parsear(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
