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
 * Representa una atencion clinica realizada a partir de una cita.
 * Una cita genera como maximo una consulta.
 * Tabla: consultas
 */
@Entity
@Table(name = "consultas")
@Getter
@Setter
@NoArgsConstructor
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cita_id", nullable = false, unique = true)
	private Cita cita;

	@Column(name = "motivo_consulta", columnDefinition = "TEXT")
	private String motivoConsulta;

	@Column(columnDefinition = "TEXT")
	private String anamnesis;

	@Column(name = "examen_visual", columnDefinition = "TEXT")
	private String examenVisual;

	@Column(columnDefinition = "TEXT")
	private String diagnostico;

	@Column(columnDefinition = "TEXT")
	private String observaciones;

	@Column(name = "fecha_inicio", nullable = false)
	private Instant fechaInicio;

	@Column(name = "fecha_fin")
	private Instant fechaFin;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@PrePersist
	void alCrear() {
		Instant ahora = Instant.now();
		if (fechaInicio == null) {
			fechaInicio = ahora;
		}
		createdAt = ahora;
		updatedAt = ahora;
	}

	@PreUpdate
	void alActualizar() {
		updatedAt = Instant.now();
	}
}
