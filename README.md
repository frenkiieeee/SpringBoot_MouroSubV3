# SpringBoot_MouroSubV3

Proyecto Spring Boot para gestionar la base de datos de MouroSub.

## Stack
- Java 21
- Spring Boot 4
- Spring Data JPA
- PostgreSQL
- Docker / Docker Compose

## Configuracion (unificada)
El proyecto usa un unico `application.properties` y toma valores desde variables de entorno.

Variables necesarias:
- `DB_URL` (ej. `jdbc:postgresql://host:5432/mourosub`)
- `DB_USER`
- `DB_PASS`

Variables opcionales:
- `SERVER_PORT` (default `8080`)
- `SPRING_JPA_SHOW_SQL` (default `false`)
- `APP_SEED_ENABLED` (default `false`)

Usa `.env.example` como referencia y crea tu `.env` local para ejecucion en Docker/Coolify.

## Esquema de base de datos
La aplicacion usa `spring.jpa.hibernate.ddl-auto=update`.
En primer despliegue crea/ajusta tablas automaticamente en el schema configurado.

## Ejecucion local
Ejemplo:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/mourosub
export DB_USER=postgres
export DB_PASS=postgres
./mvnw spring-boot:run
```

## Ejecucion con Docker Compose
1. Crear `.env` a partir de `.env.example`.
2. Ajustar `DB_URL`, `DB_USER` y `DB_PASS` con tu PostgreSQL externo.
3. Levantar:

```bash
docker compose up --build
```

Health endpoint:
- `http://localhost:${SERVER_PORT}/actuator/health`

## Despliegue en Coolify (DB externa)
- Este repo despliega solo la app Spring Boot.
- La base de datos debe existir fuera de este compose (tu PostgreSQL externo).
- Configura en Coolify las variables del `.env.example`.
- Coolify puede usar el healthcheck del contenedor para supervisar estado.
