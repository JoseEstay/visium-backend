-- Datos iniciales de roles VISIUM.
-- Se ejecuta al arrancar (ver application.yaml: spring.sql.init).

-- Roles actuales del sistema
INSERT INTO roles (codigo, nombre)
VALUES
    ('SUPER_ADMIN', 'Dueño Visium (plataforma)'),
    ('JEFE', 'Jefe / Dueño de optica'),
    ('JEFE_SUCURSAL', 'Jefe de sucursal'),
    ('RECEPCIONISTA', 'Recepcionista'),
    ('PROFESIONAL', 'Profesional')
ON CONFLICT (codigo) DO NOTHING;

-- TEMPORAL: se mantiene ADMIN hasta actualizar los @PreAuthorize del codigo.
-- Despues de eso, migrar asignaciones ADMIN -> JEFE y borrar ADMIN.
INSERT INTO roles (codigo, nombre)
VALUES ('ADMIN', 'Administrador (legacy)')
ON CONFLICT (codigo) DO NOTHING;
