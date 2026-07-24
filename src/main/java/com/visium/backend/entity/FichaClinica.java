package com.visium.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Ficha clinica basica de un paciente (antecedentes, alergias, etc.).
 * Tabla: fichas_clinicas
 */
@Entity
@Table(name = "fichas_clinicas")
@Getter
@Setter
@NoArgsConstructor
public class FichaClinica {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "paciente_id", nullable = false, unique = true)
	private Paciente paciente;

	@Column(name = "antecedentes_oftalmologicos", columnDefinition = "TEXT")
	private String antecedentesOftalmologicos;

	@Column(name = "antecedentes_medicos", columnDefinition = "TEXT")
	private String antecedentesMedicos;

	@Column(columnDefinition = "TEXT")
	private String alergias;

	@Column(columnDefinition = "TEXT")
	private String medicamentos;

	@Column(columnDefinition = "TEXT")
	private String observaciones;

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
