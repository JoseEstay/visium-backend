package com.visium.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Catalogo de enfermedades sistemicas (diabetes, hipertension, etc.).
 * Tabla: enfermedades_sistemicas
 */
@Entity
@Table(name = "enfermedades_sistemicas")
@Getter
@Setter
@NoArgsConstructor
public class EnfermedadSistemica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(nullable = false, unique = true, length = 50)
	private String codigo;

	@Column(nullable = false, length = 100)
	private String nombre;
}
