package com.visium.backend.security;

import com.visium.backend.entity.Usuario;
import com.visium.backend.entity.UsuarioEmpresa;
import com.visium.backend.entity.UsuarioEmpresaRol;
import com.visium.backend.entity.UsuarioSucursal;
import com.visium.backend.repository.UsuarioEmpresaRepository;
import com.visium.backend.repository.UsuarioEmpresaRolRepository;
import com.visium.backend.repository.UsuarioRepository;
import com.visium.backend.repository.UsuarioSucursalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Busca el usuario en la base de datos cuando Spring Security lo pide
 * (login o validacion del JWT).
 */
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioEmpresaRepository usuarioEmpresaRepository;
	private final UsuarioEmpresaRolRepository usuarioEmpresaRolRepository;
	private final UsuarioSucursalRepository usuarioSucursalRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

		List<String> roles = new ArrayList<>();
		List<UUID> empresaIds = new ArrayList<>();
		List<UUID> sucursalIds = new ArrayList<>();

		List<UsuarioEmpresa> pertenencias = usuarioEmpresaRepository.findByUsuarioId(usuario.getId());
		for (UsuarioEmpresa pertenencia : pertenencias) {
			UUID empresaId = pertenencia.getEmpresa().getId();
			if (!empresaIds.contains(empresaId)) {
				empresaIds.add(empresaId);
			}

			List<UsuarioEmpresaRol> asignaciones =
					usuarioEmpresaRolRepository.findByUsuarioEmpresaId(pertenencia.getId());
			for (UsuarioEmpresaRol asignacion : asignaciones) {
				String codigo = asignacion.getRol().getCodigo();
				if (!roles.contains(codigo)) {
					roles.add(codigo);
				}
			}

			List<UsuarioSucursal> sucursales =
					usuarioSucursalRepository.findByUsuarioEmpresaId(pertenencia.getId());
			for (UsuarioSucursal us : sucursales) {
				if (!sucursalIds.contains(us.getSucursalId())) {
					sucursalIds.add(us.getSucursalId());
				}
			}
		}

		return new UsuarioDetails(usuario, roles, empresaIds, sucursalIds);
	}
}
