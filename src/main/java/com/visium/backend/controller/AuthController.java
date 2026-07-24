package com.visium.backend.controller;

import com.visium.backend.dto.auth.LoginRequest;
import com.visium.backend.dto.auth.LoginResponse;
import com.visium.backend.dto.auth.MeResponse;
import com.visium.backend.security.UsuarioDetails;
import com.visium.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de autenticacion.
 * POST /auth/login  -> publico
 * GET  /auth/me     -> requiere token
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@GetMapping("/me")
	public ResponseEntity<MeResponse> me(@AuthenticationPrincipal UsuarioDetails detalles) {
		return ResponseEntity.ok(authService.me(detalles));
	}
}
