# MouroSub

Aplicación web de la escuela de buceo **MouroSub** (Cantabria). Permite a los
clientes explorar y reservar cursos, actividades e inmersiones, alquilar
material, contratar seguros y solicitar servicio técnico de equipos. Incluye un
panel de administración para gestionar todo el catálogo, las reservas y los
usuarios.

## ¿De qué trata?

La web cubre dos perfiles:

- **Cliente**: navega el catálogo (cursos, actividades, inmersiones, alquileres,
  seguros), hace reservas con sus datos precargados, contrata seguros, sube
  certificados de buceo y abre tickets de soporte / servicio técnico. Cada
  usuario puede consultar sus reservas y seguros desde "Mi cuenta".
- **Administrador**: gestiona desde `/admin` el catálogo completo (CRUD de
  actividades, cursos, inmersiones, materiales, seguros, instructores),
  reservas, tickets, validación de certificados y la administración de
  usuarios (editar, banear, eliminar).

La autenticación se apoya en **Supabase Auth**: el registro/login se hace contra
Supabase y la aplicación sincroniza cada usuario en su propia base de datos
mediante el `supabaseUserId`.

## Arquitectura

Arquitectura **MVC clásica de Spring Boot**, con renderizado en servidor
(Thymeleaf) y algo de JavaScript en el cliente para la sesión y los formularios.

```
Navegador (Thymeleaf + Bootstrap 5 + JS)
        │  HTTP
        ▼
Controllers  ──►  Services  ──►  Repositories (Spring Data JPA)
(@Controller)     (@Service)      (interfaces JpaRepository)
        │                              │
        ▼                              ▼
   Vistas Thymeleaf              PostgreSQL (schema "mourosub")
        ▲
        │ login / registro
   Supabase Auth (JWT)
```

Capas dentro de `com.mourosub.web`:

- **controller/** — rutas web (cliente y `/admin`), endpoints de fragmentos de
  header/footer y el puente de autenticación (`AuthController`).
- **service/** — lógica de negocio (reservas, seguros, certificados, etc.).
- **repository/** — acceso a datos con Spring Data JPA.
- **model/** — entidades JPA: `Usuario`, `Reserva`, `Actividad`, `Curso`,
  `Inmersiones`, `Material`, `Alquileres`, `Seguros`, `Instructor`, `Fichero`,
  `Tickets`, `TicketRespuesta`, `ServicioTecnico`, `TicketServicioTecnico`.
- **security/** — configuración de Spring Security y validación de JWT de Supabase.
- **resources/templates/** — vistas Thymeleaf (incluye fragmentos reutilizables
  de header y footer).
- **resources/static/** — CSS, JS e imágenes. Páginas estáticas del catálogo
  bajo `static/page/`.

El esquema de base de datos lo gestiona Hibernate con
`spring.jpa.hibernate.ddl-auto=update` sobre el schema `mourosub` (se crea solo).

## Tecnologías

- **Java 21**
- **Spring Boot 4** (Web MVC, Data JPA, Security, Actuator)
- **Thymeleaf** (plantillas en servidor)
- **Bootstrap 5.3** + Bootstrap Icons (frontend, diseño responsive)
- **PostgreSQL** (vía Supabase)
- **Supabase** (Auth, Storage, REST, Studio)
- **JWT** (`jjwt`) para validar los tokens de Supabase
- **Docker / Docker Compose**
- **Maven** (build, con `mvnw` incluido)

## Configuración

La aplicación usa un único `application.properties` que lee variables de entorno.
Copia `.env.example` a `.env` y ajusta los valores.

Variables principales de la app:

| Variable | Descripción | Por defecto |
|----------|-------------|-------------|
| `APP_PORT` | Puerto expuesto en el host | `8090` |
| `SERVER_PORT` | Puerto interno del contenedor | `8080` |
| `DB_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://localhost:5432` |
| `DB_USER` / `DB_PASS` | Credenciales de la base de datos | — |
| `APP_SEED_ENABLED` | Carga datos de ejemplo al arrancar | `false` |
| `SPRING_JPA_SHOW_SQL` | Muestra el SQL en consola | `false` |

El `docker-compose.yaml` define además los servicios de **Supabase**
(`supabase-db`, `supabase-auth`, `supabase-rest`, `supabase-storage`,
`supabase-studio`, etc.) y sus variables (`JWT_SECRET`, `ANON_KEY`,
`SERVICE_ROLE_KEY`, `SUPABASE_PUBLIC_URL`, ...). Revisa `.env.example` para la
lista completa.

> En Docker, las carpetas `static/` y `templates/` se montan como volúmenes:
> los cambios de HTML/CSS/JS se ven al recargar el navegador, sin reconstruir.
> Los cambios en código **Java** sí requieren reconstruir la imagen.

## Ejecución con Docker Compose

```bash
# 1. Crear el .env a partir del ejemplo y ajustar credenciales
cp .env.example .env

# 2. Levantar toda la pila (app + Supabase + PostgreSQL)
docker compose up --build
```

La aplicación queda disponible en `http://localhost:8090`.
El panel de Supabase Studio se expone en su propio puerto (ver `.env`).

Tras cambios solo en Java, reconstruye únicamente la app:

```bash
docker compose up -d --build springboot-app
```

## Ejecución local (sin Docker)

Requiere un PostgreSQL accesible y las variables `DB_URL`, `DB_USER`, `DB_PASS`.

```bash
./mvnw spring-boot:run
```

Por defecto arranca en `http://localhost:8080`.

## Salud y monitorización

Spring Boot Actuator expone los endpoints de salud:

- `GET /actuator/health`
- `GET /actuator/info`

El contenedor de la app usa `/actuator/health` como healthcheck en Compose.
