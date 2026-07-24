package com.visium.backend.entity;

import jakarta.persistence.Column;
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
import java.time.LocalDate;
import java.util.UUID;

/**
 * Relacion entre un paciente y una enfermedad sistemica.
 * Tabla: pacientes_enfermedades_sistemicas
 */
@Entity
@Table(name = "pacientes_enfermedades_sistemicas")
@IdClass(PacienteEnfermedadSistemica.PacienteEnfermedadSistemicaId.class)
@Getter
@Setter
@NoArgsConstructor
public class PacienteEnfermedadSistemica {

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "paciente_id", nullable = false)
	private Paciente paciente;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "enfermedad_id", nullable = false)
	private EnfermedadSistemica enfermedad;

	@Column(name = "fecha_diagnostico")
	private LocalDate fechaDiagnostico;

	@Column(columnDefinition = "TEXT")
	private String observaciones;

	@Column(nullable = false)
	private Boolean activo = true;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class PacienteEnfermedadSistemicaId implements Serializable {
		private UUID paciente;
		private Short enfermedad;
	}
}
