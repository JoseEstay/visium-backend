package com.visium.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Representa una empresa u optica que usa VISIUM.
 * Tabla: empresas
 */
@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true, length = 12)
	private String rut;

	@Column(name = "razon_social", nullable = false, length = 150)
	private String razonSocial;

	@Column(length = 254)
	private String email;

	@Column(nullable = false)
	private Boolean activo = true;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	// Se ejecuta automaticamente antes de guardar por primera vez
	@PrePersist
	void alCrear() {
		Instant ahora = Instant.now();
		createdAt = ahora;
		updatedAt = ahora;
	}

	// Se ejecuta automaticamente antes de actualizar
	@PreUpdate
	void alActualizar() {
		updatedAt = Instant.now();
	}
}
