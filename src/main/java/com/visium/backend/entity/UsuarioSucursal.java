package com.visium.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Indica en que sucursales puede trabajar un usuario de una empresa.
 * Tabla: usuarios_sucursales
 */
@Entity
@Table(name = "usuarios_sucursales")
@IdClass(UsuarioSucursal.UsuarioSucursalId.class)
@Getter
@Setter
@NoArgsConstructor
public class UsuarioSucursal {

	@Id
	@Column(name = "usuario_empresa_id", nullable = false)
	private UUID usuarioEmpresaId;

	@Id
	@Column(name = "sucursal_id", nullable = false)
	private UUID sucursalId;

	@Column(name = "empresa_id", nullable = false)
	private UUID empresaId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({
		@JoinColumn(name = "usuario_empresa_id", referencedColumnName = "id", insertable = false, updatable = false),
		@JoinColumn(name = "empresa_id", referencedColumnName = "empresa_id", insertable = false, updatable = false)
	})
	private UsuarioEmpresa usuarioEmpresa;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({
		@JoinColumn(name = "sucursal_id", referencedColumnName = "id", insertable = false, updatable = false),
		@JoinColumn(name = "empresa_id", referencedColumnName = "empresa_id", insertable = false, updatable = false)
	})
	private Sucursal sucursal;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@PrePersist
	void alCrear() {
		createdAt = Instant.now();
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class UsuarioSucursalId implements Serializable {
		private UUID usuarioEmpresaId;
		private UUID sucursalId;
	}
}
