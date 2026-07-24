package com.visium.backend.security;

import com.visium.backend.entity.Usuario;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Adapta nuestro Usuario al formato que entiende Spring Security. */
@Getter
public class UsuarioDetails implements UserDetails {

  private final UUID id;
  private final String email;
  private final String passwordHash;
  private final boolean activo;
  private final List<String> roles;

  public UsuarioDetails(Usuario usuario, List<String> roles) {
    this.id = usuario.getId();
    this.email = usuario.getEmail();
    this.passwordHash = usuario.getPasswordHash();
    this.activo = Boolean.TRUE.equals(usuario.getActivo());
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Spring espera roles con prefijo ROLE_ (ejemplo: ROLE_ADMIN)
    return roles.stream()
        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return activo;
  }
}
