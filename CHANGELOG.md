# Changelog

Todos los cambios notables de Visium Backend se documentan en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/),
y este proyecto adhiere a [Versionado Semántico](https://semver.org/lang/es/).

## [Unreleased]

### Added
- Claims JWT `roles`, `empresaIds`, `sucursalIds`
- Respuestas `login` / `me` con alcance (`empresaIds`, `sucursalIds`, `empresaActivaId`)
- Header `X-Empresa-Id` + `EmpresaContext` (empresa activa por request)

### Planeado
- `AccesoService` y filtros por empresa/sucursal
- Reemplazar `@PreAuthorize('ADMIN')` por `SUPER_ADMIN` / `JEFE` / `JEFE_SUCURSAL`
- Migrar asignaciones legacy `ADMIN` → `JEFE` y eliminar el rol `ADMIN`
- CRUD de citas, consultas y recetas
- Dashboard y tests

---

## [0.1.0] - 2026-07-28

Primera versión versionada del backend VISIUM (rama `desarrollo-backend`).

### Added
- Proyecto Spring Boot 4.1 + Java 21 + PostgreSQL + JWT
- Modelo de datos multi-empresa (entidades JPA y repositorios)
- Autenticación: `POST /auth/login`, `GET /auth/me`
- CRUD empresas, sucursales, pacientes
- Registro transaccional de profesionales (5 pasos de reglas de negocio)
- Seguridad con roles y CORS para el frontend (`localhost:5173`)
- Documentación: `docs/modelamiento-datos.md`, `docs/reglas-negocio.md`, `docs/PLAN-DE-TRABAJO.md`
- Roles documentados y seed en `data.sql`:
  - `SUPER_ADMIN` — dueños Visium (plataforma)
  - `JEFE` — dueño de una o varias ópticas
  - `JEFE_SUCURSAL` — jefe de una sucursal
  - `RECEPCIONISTA`, `PROFESIONAL`
- `ADMIN` se mantiene temporalmente como legacy hasta migrar los controllers
- Configuración por variables de entorno (`.env-ejemplo`)

### Changed
- Aislamiento por diseño: una sola base de datos filtrada por `empresa_id` (no una BD por óptica)
- `ddl-auto: update` + carga de `data.sql` al arrancar

### Security
- Secretos fuera del código (`DB_*`, `JWT_SECRET` vía `.env`)
- Endpoints protegidos por JWT salvo `/auth/login`

---

## Cómo versionar de aquí en adelante

1. Antes de cada commit relevante, actualiza la sección `[Unreleased]` o crea una versión nueva `## [X.Y.Z] - AAAA-MM-DD`.
2. Sube la versión en `pom.xml` (`<version>`) para que coincida.
3. Tipos de cambio:
   - **Added** — algo nuevo
   - **Changed** — cambio en lo existente
   - **Deprecated** — pronto se quita
   - **Removed** — eliminado
   - **Fixed** — corrección de bugs
   - **Security** — seguridad

### Convención de números
- `0.1.x` — base del MVP (auth, admin, pacientes, roles en docs)
- `0.2.x` — aislamiento real SUPER_ADMIN / JEFE / JEFE_SUCURSAL en código
- `0.3.x` — flujo clínico (citas, consultas, recetas)
- `1.0.0` — MVP listo para clientes

[Unreleased]: https://github.com/JulioJulioso/visium-backend/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/JulioJulioso/visium-backend/releases/tag/v0.1.0
