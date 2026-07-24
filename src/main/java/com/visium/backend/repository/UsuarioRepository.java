package com.visium.backend.repository;

import com.visium.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Acceso a la tabla usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	// Busca un usuario por email (sirve para el login)
	Optional<Usuario> findByEmailIgnoreCase(String email);
}
