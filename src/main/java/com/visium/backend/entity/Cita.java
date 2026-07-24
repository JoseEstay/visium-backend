package com.visium.backend.entity;

import com.visium.backend.enums.EstadoCita;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Representa una reserva de atencion.
 * Tabla: citas
 *
 * Nota: las relaciones se mapean por el id principal.
 * Las validaciones de que todo pertenezca a la misma empresa
 * se hacen en el Service (reglas de negocio).
 */
@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
public class Cita {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "empresa_id", nullable = false)
	private UUID empresaId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sucursal_id", nullable = false)
	private Sucursal sucursal;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "paciente_id", nullable = false)
	private Paciente paciente;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "profesional_id", nullable = false)
	private Profesional profesional;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "creada_por_usuario_empresa_id", nullable = false)
	private UsuarioEmpresa creadaPor;

	@Column(name = "fecha_hora_inicio", nullable = false)
	private Instant fechaHoraInicio;

	@Column(name = "fecha_hora_fin", nullable = false)
	private Instant fechaHoraFin;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private EstadoCita estado = EstadoCita.PENDIENTE;

	@Column(columnDefinition = "TEXT")
	private String motivo;

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
