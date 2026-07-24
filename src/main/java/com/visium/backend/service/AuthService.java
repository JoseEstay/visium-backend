package com.visium.backend.service;

import com.visium.backend.dto.auth.LoginRequest;
import com.visium.backend.dto.auth.LoginResponse;
import com.visium.backend.dto.auth.MeResponse;
import com.visium.backend.entity.Usuario;
import com.visium.backend.repository.UsuarioRepository;
import com.visium.backend.security.JwtUtil;
import com.visium.backend.security.UsuarioDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/** Logica de autenticacion: login y datos del usuario actual. */
@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UsuarioRepository usuarioRepository;

  public LoginResponse login(LoginRequest request) {
    // Spring Security valida email + password
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    UsuarioDetails detalles = (UsuarioDetails) authentication.getPrincipal();
    Usuario usuario = usuarioRepository.findByEmailIgnoreCase(detalles.getEmail()).orElseThrow();

    String token = jwtUtil.generarToken(detalles.getId(), detalles.getEmail(), detalles.getRoles());

    return new LoginResponse(
        token,
        usuario.getId(),
        usuario.getEmail(),
        usuario.getNombre(),
        usuario.getApellido(),
        detalles.getRoles());
  }

  public MeResponse me(UsuarioDetails detalles) {
    Usuario usuario = usuarioRepository.findByEmailIgnoreCase(detalles.getEmail()).orElseThrow();

    return new MeResponse(
        usuario.getId(),
        usuario.getEmail(),
        usuario.getNombre(),
        usuario.getApellido(),
        detalles.getRoles());
  }
}
