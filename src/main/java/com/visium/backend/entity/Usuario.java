package com.visium.backend.entity;

import com.visium.backend.enums.Sexo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Representa una persona que puede iniciar sesion en VISIUM. Tabla: usuarios */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 100)
  private String apellido;

  @Column(length = 12)
  private String run;

  @Column(nullable = false, length = 254)
  private String email;

  @Column(length = 30)
  private String telefono;

  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Sexo sexo;

  // Contrasena encriptada (nunca se guarda en texto plano)
  @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
  private String passwordHash;

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
