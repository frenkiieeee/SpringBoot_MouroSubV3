CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(255),
    dni VARCHAR(20),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    cod_postal VARCHAR(10),
    localidad VARCHAR(100),
    fecha_nacimiento DATE
);

CREATE TABLE IF NOT EXISTS instructores (
    id_instructor BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    dni VARCHAR(20),
    certificaciones VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS categoria_cursos (
    id_categoria BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS actividades (
    id_actividad BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    tipo VARCHAR(50) NOT NULL,
    duracion_minutos INTEGER,
    plazas_max INTEGER NOT NULL DEFAULT 10,
    precio NUMERIC(10, 2) NOT NULL,
    requiere_nivel BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS cursos (
    id_curso BIGSERIAL PRIMARY KEY,
    id_categoria BIGINT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    nivel_requerido VARCHAR(50) NOT NULL DEFAULT 'ninguno',
    duracion_horas INTEGER,
    plazas_max INTEGER NOT NULL DEFAULT 6,
    precio_base NUMERIC(10, 2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cursos_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria_cursos(id_categoria)
);

CREATE TABLE IF NOT EXISTS inmersiones (
    id_inmersion BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS material (
    id_material BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    estado_conservacion VARCHAR(100) NOT NULL,
    num_serie VARCHAR(50) NOT NULL,
    marca VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS alquileres (
    id_alquiler BIGSERIAL PRIMARY KEY,
    precio_total DOUBLE PRECISION NOT NULL,
    fecha_alquiler DATE NOT NULL,
    fecha_devolucion DATE,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS reservas (
    id_reserva BIGSERIAL PRIMARY KEY,
    tipo_servicio VARCHAR(50) NOT NULL,
    ref_servicio VARCHAR(100),
    ref_servicio_id VARCHAR(100),
    num_participantes INTEGER NOT NULL,
    precio_total NUMERIC(10, 2) NOT NULL,
    estado VARCHAR(20),
    fecha_reserva DATE NOT NULL,
    fecha_actividad DATE NOT NULL,
    id_usuario BIGINT,
    id_instructor BIGINT,
    CONSTRAINT fk_reservas_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_reservas_instructor
        FOREIGN KEY (id_instructor) REFERENCES instructores(id_instructor)
);

CREATE INDEX IF NOT EXISTS idx_reservas_usuario ON reservas(id_usuario);
CREATE INDEX IF NOT EXISTS idx_reservas_instructor ON reservas(id_instructor);
CREATE INDEX IF NOT EXISTS idx_cursos_categoria ON cursos(id_categoria);
