# Plan de trabajo — Backend VISIUM

Este documento explica qué se está construyendo, en qué orden y cómo continuar el trabajo.
Está escrito para que cualquier persona del equipo pueda retomar el proyecto sin ayuda extra.

---

## 1. ¿Qué es VISIUM?

VISIUM es una aplicación web para ópticas. Centraliza fichas clínicas, recetas ópticas
e historial de pacientes. Tiene dos partes:

- **Frontend**: hecho en React ([repositorio visium-react](https://github.com/JulioJulioso/visium-react)).
- **Backend**: este repositorio. Una API hecha en Java con Spring Boot que guarda y entrega
  los datos que el frontend muestra.

---

## 2. Documentos de referencia (fuente de verdad)

Antes de programar cualquier cosa, hay que leer estos dos documentos:

| Documento | Qué contiene |
|---|---|
| `docs/modelamiento-datos.md` | Las 13 tablas de la base de datos, sus relaciones y el SQL completo |
| `docs/reglas-negocio.md` | Las reglas que el código debe respetar (validaciones de citas, roles, etc.) |

**Importante**: si algún otro material (por ejemplo, apuntes del bootcamp) contradice a estos
documentos, mandan estos documentos. Decisiones ya tomadas:

- Los roles son `ADMIN`, `RECEPCIONISTA` y `PROFESIONAL` (no existe rol "MEDICO").
- No existe la entidad "Expediente"; su función la cumplen `consultas` y `recetas`.
- La base de datos es **PostgreSQL** (no MySQL).
- El modelo es multi-empresa: una misma instalación puede servir a varias ópticas.

---

## 3. Tecnologías usadas

| Herramienta | Para qué |
|---|---|
| Java 21 + Spring Boot 4.1 | El lenguaje y el framework del backend |
| Maven (incluido como `mvnw`) | Compilar y manejar dependencias. No hay que instalarlo |
| PostgreSQL | Base de datos |
| Spring Security + JWT | Login y protección de endpoints |
| Spring Data JPA | Leer y escribir en la base de datos desde Java |

---

## 4. Estructura del código

El código vive en `src/main/java/com/visium/backend/` y se organiza en capas.
Cada módulo (pacientes, citas, etc.) repite el mismo patrón:

```text
controller/   → Recibe las peticiones HTTP (ej: PacienteController)
service/      → Contiene la lógica y las reglas de negocio (ej: PacienteService)
repository/   → Habla con la base de datos (ej: PacienteRepository)
entity/       → Clases que representan las tablas (ej: Paciente)
dto/          → Objetos que entran y salen por la API (ej: PacienteRequest, PacienteResponse)
mapper/       → Convierte entre entity y dto (ej: PacienteMapper)
security/     → Todo lo relacionado con login y JWT
exception/    → Manejo de errores uniforme para toda la API
```

Regla simple: **una petición siempre viaja así**:
`Controller → Service → Repository → base de datos`, y la respuesta vuelve por el mismo camino.

---

## 5. Orden de construcción (fases)

Se avanza fase por fase. Una fase no se empieza hasta que la anterior compila y está probada.

El historial de versiones está en [`CHANGELOG.md`](../CHANGELOG.md) (versión actual del artefacto: `0.2.0-SNAPSHOT` en `pom.xml`).

| Fase | Qué se hace | Estado |
|---|---|---|
| 0 | Rama `desarrollo-backend` y este documento | Hecho |
| 1 | Arreglar `pom.xml`, configurar conexión a PostgreSQL, crear tablas (`schema.sql`, `data.sql`) | Hecho |
| 2 | Entidades JPA y repositorios para las 13 tablas | Hecho |
| 3 | Seguridad: login con JWT, roles y CORS para el frontend | Hecho |
| 4 | CRUD de administración: empresas, sucursales, usuarios, profesionales | Hecho |
| 4b | Roles SUPER_ADMIN / JEFE / JEFE_SUCURSAL + aislamiento por empresa/sucursal | En curso (AccesoService listo; falta PreAuthorize + Bruno) |
| 5 | CRUD clínico: pacientes, citas (con validaciones), consultas y recetas | Parcial (pacientes) |
| 6 | Manejo de errores global y endpoint de dashboard | Pendiente |
| 7 | Tests básicos, README con instrucciones y ROADMAP | Pendiente |

*(Actualizar la columna "Estado" a medida que se avanza.)*

---

## 6. Cómo trabajamos con Git

- `main` es la rama estable: **no se trabaja directamente sobre ella**.
- El desarrollo se hace en la rama `desarrollo-backend`.
- Cuando una fase está completa y probada, se puede subir y abrir un Pull Request hacia `main`
  para que otra persona lo revise antes de mezclar.

Comandos útiles:

```bash
git switch desarrollo-backend   # moverse a la rama de trabajo
git status                      # ver qué archivos cambiaron
git log --oneline               # ver el historial de commits
```

---

## 7. Cómo levantar el proyecto en una máquina nueva

1. Instalar **JDK 21** y **PostgreSQL** (recordar la contraseña del usuario `postgres`).
2. Clonar el repositorio y moverse a la rama `desarrollo-backend`.
3. Crear la base de datos `visium` (las instrucciones detalladas quedarán en el README
   cuando se complete la Fase 1).
4. Ejecutar en la terminal, dentro de la carpeta del proyecto:

```bash
./mvnw spring-boot:run     # en Windows PowerShell: .\mvnw spring-boot:run
```

5. La API queda disponible en `http://localhost:8080`.

Sirve cualquier editor: Cursor, IntelliJ o VS Code. Todos leen el mismo `pom.xml`.

---

## 8. Reglas de negocio que NUNCA hay que saltarse

Resumen de `docs/reglas-negocio.md` (leer el documento completo para el detalle):

- Registrar un profesional implica 5 pasos en **una sola transacción**
  (usuario, pertenencia a empresa, rol, registro profesional, sucursales).
- Antes de crear una cita se validan 7 condiciones (paciente y sucursal de la empresa,
  profesional activo, con rol, asignado a la sucursal y sin choque de horario).
- Una cita genera como máximo una consulta, y solo si está confirmada.
- Al finalizar una consulta, la cita pasa a estado `ATENDIDA`.
- Una consulta genera como máximo una receta, siempre con detalle para OD y OI (ambos ojos).
