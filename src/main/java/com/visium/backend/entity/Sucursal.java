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
 * Representa una sucursal de una empresa.
 * Tabla: sucursales
 */
@Entity
@Table(name = "sucursales")
@Getter
@Setter
@NoArgsConstructor
public class Sucursal {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	// Cada sucursal pertenece a una empresa
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@Column(nullable = false, length = 120)
	private String nombre;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String direccion;

	@Column(length = 100)
	private String comuna;

	@Column(length = 100)
	private String ciudad;

	@Column(length = 100)
	private String region;

	@Column(length = 30)
	private String telefono;

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
