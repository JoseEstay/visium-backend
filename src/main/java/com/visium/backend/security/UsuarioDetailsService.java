package com.visium.backend.security;

import com.visium.backend.entity.Usuario;
import com.visium.backend.entity.UsuarioEmpresa;
import com.visium.backend.entity.UsuarioEmpresaRol;
import com.visium.backend.repository.UsuarioEmpresaRepository;
import com.visium.backend.repository.UsuarioEmpresaRolRepository;
import com.visium.backend.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Busca el usuario en la base de datos cuando Spring Security lo pide (por ejemplo, al hacer
 * login).
 */
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;
  private final UsuarioEmpresaRepository usuarioEmpresaRepository;
  private final UsuarioEmpresaRolRepository usuarioEmpresaRolRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Usuario usuario =
        usuarioRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

    // Junta todos los roles del usuario en todas sus empresas
    List<String> roles = new ArrayList<>();
    List<UsuarioEmpresa> pertenencias = usuarioEmpresaRepository.findByUsuarioId(usuario.getId());
    for (UsuarioEmpresa pertenencia : pertenencias) {
      List<UsuarioEmpresaRol> asignaciones =
          usuarioEmpresaRolRepository.findByUsuarioEmpresaId(pertenencia.getId());
      for (UsuarioEmpresaRol asignacion : asignaciones) {
        String codigo = asignacion.getRol().getCodigo();
        if (!roles.contains(codigo)) {
          roles.add(codigo);
        }
      }
    }

    return new UsuarioDetails(usuario, roles);
  }
}
