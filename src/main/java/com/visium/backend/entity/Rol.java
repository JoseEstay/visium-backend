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
 * Representa un rol del sistema: ADMIN, RECEPCIONISTA o PROFESIONAL.
 * Tabla: roles
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	// Codigo que usa Spring Security, ejemplo: ADMIN
	@Column(nullable = false, unique = true, length = 50)
	private String codigo;

	@Column(nullable = false, length = 100)
	private String nombre;
}
