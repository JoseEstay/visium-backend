package com.visium.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Representa la pertenencia de un usuario a una empresa.
 * Un mismo usuario puede pertenecer a varias empresas.
 * Tabla: usuarios_empresas
 */
@Entity
@Table(name = "usuarios_empresas")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEmpresa {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@Column(nullable = false)
	private Boolean activo = true;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@PrePersist
	void alCrear() {
		Instant ahora = Instant.now();
		createdAt = ahora;
		updatedAt = ahora;
	}

	@PreUpdate
	void alActualizar() {
		updatedAt = Instant.now();
	}
}
