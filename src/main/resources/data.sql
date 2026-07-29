-- Datos iniciales de roles VISIUM.
-- Se ejecuta al arrancar (ver application.yaml: spring.sql.init).

INSERT INTO roles (codigo, nombre)
VALUES
    ('SUPER_ADMIN', 'Dueño Visium (plataforma)'),
    ('JEFE', 'Jefe / Dueño de optica'),
    ('JEFE_SUCURSAL', 'Jefe de sucursal'),
    ('RECEPCIONISTA', 'Recepcionista'),
    ('PROFESIONAL', 'Profesional')
ON CONFLICT (codigo) DO NOTHING;

-- Migracion one-shot: asignaciones legacy ADMIN -> JEFE, luego elimina el rol ADMIN.
UPDATE usuarios_empresas_roles uer
SET rol_id = (SELECT id FROM roles WHERE codigo = 'JEFE')
WHERE rol_id = (SELECT id FROM roles WHERE codigo = 'ADMIN')
  AND NOT EXISTS (
      SELECT 1
      FROM usuarios_empresas_roles x
      WHERE x.usuario_empresa_id = uer.usuario_empresa_id
        AND x.rol_id = (SELECT id FROM roles WHERE codigo = 'JEFE')
  );

DELETE FROM usuarios_empresas_roles
WHERE rol_id = (SELECT id FROM roles WHERE codigo = 'ADMIN');

DELETE FROM roles WHERE codigo = 'ADMIN';
