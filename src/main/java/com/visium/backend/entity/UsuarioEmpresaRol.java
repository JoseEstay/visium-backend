package com.visium.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Asigna un rol a un usuario dentro de una empresa.
 * Tabla: usuarios_empresas_roles
 * Clave primaria compuesta: (usuario_empresa_id, rol_id)
 */
@Entity
@Table(name = "usuarios_empresas_roles")
@IdClass(UsuarioEmpresaRol.UsuarioEmpresaRolId.class)
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEmpresaRol {

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_empresa_id", nullable = false)
	private UsuarioEmpresa usuarioEmpresa;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "rol_id", nullable = false)
	private Rol rol;

	/**
	 * Clase auxiliar para la clave primaria compuesta.
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class UsuarioEmpresaRolId implements Serializable {
		private UUID usuarioEmpresa;
		private Short rol;
	}
}
