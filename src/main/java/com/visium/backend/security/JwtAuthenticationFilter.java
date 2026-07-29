package com.visium.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 1) Valida JWT y autentica al usuario.
 * 2) Resuelve la empresa activa (header X-Empresa-Id o la unica empresa del usuario).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String HEADER_EMPRESA = "X-Empresa-Id";

	private final JwtUtil jwtUtil;
	private final UsuarioDetailsService usuarioDetailsService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		try {
			String header = request.getHeader(HttpHeaders.AUTHORIZATION);

			if (header != null && header.startsWith("Bearer ")) {
				String token = header.substring(7);

				if (jwtUtil.esValido(token)
						&& SecurityContextHolder.getContext().getAuthentication() == null) {
					String email = jwtUtil.extraerEmail(token);
					UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(
									userDetails,
									null,
									userDetails.getAuthorities()
							);
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);

					if (userDetails instanceof UsuarioDetails detalles) {
						if (!resolverEmpresaActiva(request, response, detalles)) {
							return;
						}
					}
				}
			}

			filterChain.doFilter(request, response);
		} finally {
			EmpresaContext.clear();
		}
	}

	/**
	 * Define EmpresaContext segun header o unica empresa del usuario.
	 * SUPER_ADMIN puede pedir cualquier empresa por header.
	 * @return false si ya se escribio un error 403 en la respuesta
	 */
	private boolean resolverEmpresaActiva(
			HttpServletRequest request,
			HttpServletResponse response,
			UsuarioDetails detalles
	) throws IOException {
		String raw = request.getHeader(HEADER_EMPRESA);

		if (raw != null && !raw.isBlank()) {
			UUID solicitada;
			try {
				solicitada = UUID.fromString(raw.trim());
			} catch (IllegalArgumentException ex) {
				escribirForbidden(response, "X-Empresa-Id no es un UUID valido");
				return false;
			}

			if (detalles.esSuperAdmin() || detalles.perteneceAEmpresa(solicitada)) {
				EmpresaContext.setEmpresaId(solicitada);
				return true;
			}

			escribirForbidden(response, "No tienes acceso a esa empresa");
			return false;
		}

		// Sin header: si solo tiene una empresa, esa queda activa
		if (detalles.getEmpresaIds().size() == 1) {
			EmpresaContext.setEmpresaId(detalles.getEmpresaIds().getFirst());
		}
		// Varias empresas y sin header: queda null; AccesoService pedira el header despues
		return true;
	}

	private void escribirForbidden(HttpServletResponse response, String mensaje) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(
				"{\"status\":403,\"error\":\"Forbidden\",\"message\":\"" + mensaje + "\"}"
		);
	}
}
