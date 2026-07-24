package com.visium.backend.entity;

import com.visium.backend.enums.Ojo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Detalle de una receta para un ojo (OD u OI).
 * Tabla: recetas_opticas_detalles
 */
@Entity
@Table(name = "recetas_opticas_detalles")
@IdClass(RecetaOpticaDetalle.RecetaOpticaDetalleId.class)
@Getter
@Setter
@NoArgsConstructor
public class RecetaOpticaDetalle {

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "receta_id", nullable = false)
	private RecetaOptica receta;

	@Id
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 2)
	private Ojo ojo;

	@Column(precision = 5, scale = 2)
	private BigDecimal esfera;

	@Column(precision = 5, scale = 2)
	private BigDecimal cilindro;

	private Short eje;

	@Column(precision = 5, scale = 2)
	private BigDecimal prisma;

	@Column(name = "base_prisma", length = 20)
	private String basePrisma;

	@Column(name = "agudeza_visual", length = 20)
	private String agudezaVisual;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class RecetaOpticaDetalleId implements Serializable {
		private UUID receta;
		private Ojo ojo;
	}
}
