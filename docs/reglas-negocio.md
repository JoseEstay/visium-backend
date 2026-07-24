# Reglas de negocio

## Usuarios y roles

- Un usuario puede pertenecer a una o varias empresas.
- Un usuario puede tener varios roles dentro de una empresa.
- Los roles deben verificarse en Spring Security.
- El rol no reemplaza la información específica de un profesional.

## Profesionales

Para registrar un profesional se deben crear:

1. El registro en `usuarios`.
2. La pertenencia en `usuarios_empresas`.
3. La asignación del rol `PROFESIONAL`.
4. El registro en `profesionales`.
5. La asignación en `usuarios_sucursales`.

Estas operaciones deben ejecutarse dentro de una única transacción.

## Pacientes

- Un paciente pertenece a una empresa.
- Puede atenderse en distintas sucursales.
- No se relaciona directamente con una sucursal.
- Las sucursales visitadas se obtienen desde las citas.

## Citas

Antes de crear una cita se debe validar que:

- El paciente pertenezca a la empresa.
- La sucursal pertenezca a la empresa.
- El profesional esté activo.
- El profesional pertenezca a la empresa.
- El profesional tenga el rol `PROFESIONAL`.
- El profesional esté asignado a la sucursal.
- El profesional no tenga otra cita en el mismo horario.

## Consultas

- Una cita puede generar como máximo una consulta.
- Una consulta solamente debe iniciarse para una cita confirmada.
- Al finalizar una consulta, la cita debe cambiar a `ATENDIDA`.

## Recetas ópticas

- Una consulta puede generar como máximo una receta en el MVP.
- Una receta debe contener un detalle para `OD` y otro para `OI`.
- La adición se almacena una sola vez en `recetas_opticas`.
- El eje debe estar entre 0 y 180.
