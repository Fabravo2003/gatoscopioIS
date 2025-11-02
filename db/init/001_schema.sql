-- Esquema inicial para Postgres derivado del MER
-- Se ejecuta automáticamente al inicializar la BD (entrypoint de Postgres)

BEGIN;

-- =====================
--  Tablas base / catálogos
-- =====================

CREATE TABLE IF NOT EXISTS roles (
    nombre varchar PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS usuarios (
    id serial PRIMARY KEY,
    nombre varchar,
    correo varchar NOT NULL UNIQUE,
    contrasena varchar,
    created_at timestamp DEFAULT now()
);

CREATE TABLE IF NOT EXISTS pacientes (
    codigo varchar PRIMARY KEY,
    nombre varchar,
    correo varchar,
    telefono varchar,
    caso_control varchar
);

CREATE TABLE IF NOT EXISTS preguntas (
    id serial PRIMARY KEY,
    nombre_variable varchar UNIQUE,
    codigo varchar,
    tipo_dato varchar,
    enunciado text,
    audiencia varchar
);

CREATE TABLE IF NOT EXISTS tipos_muestra (
    id serial PRIMARY KEY,
    nombre varchar UNIQUE,
    unidad_medida varchar,
    minimo_alerta numeric
);

-- =====================
--  Entidades dependientes / relaciones
-- =====================

CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id int NOT NULL,
    rol_nombre varchar NOT NULL,
    PRIMARY KEY (usuario_id, rol_nombre),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_ur_rol FOREIGN KEY (rol_nombre)
        REFERENCES roles(nombre)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_usuarios_roles_rol
    ON usuarios_roles (rol_nombre);


CREATE TABLE IF NOT EXISTS encuestas (
    id serial PRIMARY KEY,
    estado varchar,
    paciente_codigo varchar NOT NULL,
    created_at timestamp DEFAULT now(),
    CONSTRAINT fk_enc_paciente FOREIGN KEY (paciente_codigo)
        REFERENCES pacientes(codigo)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_encuestas_paciente
    ON encuestas (paciente_codigo);


CREATE TABLE IF NOT EXISTS usuarios_encuestas (
    usuario_id int NOT NULL,
    encuesta_id int NOT NULL,
    fecha_hora_modificacion timestamp,
    PRIMARY KEY (usuario_id, encuesta_id),
    CONSTRAINT fk_ue_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_ue_encuesta FOREIGN KEY (encuesta_id)
        REFERENCES encuestas(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_usuarios_encuestas_encuesta
    ON usuarios_encuestas (encuesta_id);


CREATE TABLE IF NOT EXISTS valor_valido_pregunta (
    id serial PRIMARY KEY,
    valor varchar,
    pregunta_id int NOT NULL,
    CONSTRAINT fk_vvp_pregunta FOREIGN KEY (pregunta_id)
        REFERENCES preguntas(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_vvp_pregunta
    ON valor_valido_pregunta (pregunta_id);


CREATE TABLE IF NOT EXISTS respuestas (
    id serial PRIMARY KEY,
    encuesta_id int NOT NULL,
    pregunta_id int NOT NULL,
    valor text,
    CONSTRAINT fk_resp_encuesta FOREIGN KEY (encuesta_id)
        REFERENCES encuestas(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_resp_pregunta FOREIGN KEY (pregunta_id)
        REFERENCES preguntas(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_respuesta_encuesta_pregunta UNIQUE (encuesta_id, pregunta_id)
);

CREATE INDEX IF NOT EXISTS idx_respuestas_encuesta
    ON respuestas (encuesta_id);

CREATE INDEX IF NOT EXISTS idx_respuestas_pregunta
    ON respuestas (pregunta_id);


CREATE TABLE IF NOT EXISTS reglas_dicotomizacion (
    id serial PRIMARY KEY,
    pregunta_id int NOT NULL,
    nombre varchar,
    tipo_regla varchar,
    activa boolean DEFAULT true,
    valor text,
    created_at timestamp DEFAULT now(),
    CONSTRAINT fk_regla_pregunta FOREIGN KEY (pregunta_id)
        REFERENCES preguntas(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_reglas_pregunta
    ON reglas_dicotomizacion (pregunta_id);


CREATE TABLE IF NOT EXISTS valores_dicotomizados (
    id serial PRIMARY KEY,
    regla_id int NOT NULL,
    respuesta_id int NOT NULL,
    CONSTRAINT fk_vd_regla FOREIGN KEY (regla_id)
        REFERENCES reglas_dicotomizacion(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_vd_respuesta FOREIGN KEY (respuesta_id)
        REFERENCES respuestas(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_vd_regla
    ON valores_dicotomizados (regla_id);

CREATE INDEX IF NOT EXISTS idx_vd_respuesta
    ON valores_dicotomizados (respuesta_id);


CREATE TABLE IF NOT EXISTS muestras (
    codigo varchar PRIMARY KEY,
    tipo_muestra_id int NOT NULL,
    paciente_codigo varchar,
    observacion text,
    created_at timestamp DEFAULT now(),
    cantidad_donada numeric,
    CONSTRAINT fk_muestra_tipo FOREIGN KEY (tipo_muestra_id)
        REFERENCES tipos_muestra(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_muestra_paciente FOREIGN KEY (paciente_codigo)
        REFERENCES pacientes(codigo)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_muestras_tipo
    ON muestras (tipo_muestra_id);

CREATE INDEX IF NOT EXISTS idx_muestras_paciente
    ON muestras (paciente_codigo);


CREATE TABLE IF NOT EXISTS operaciones_muestra (
    id serial PRIMARY KEY,
    usuario_id int NOT NULL,
    muestra_codigo varchar NOT NULL,
    tipo_movimiento varchar,
    fecha_hora timestamp,
    observacion text,
    cantidad numeric,
    CONSTRAINT fk_om_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_om_muestra FOREIGN KEY (muestra_codigo)
        REFERENCES muestras(codigo)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_oper_muestra_usuario
    ON operaciones_muestra (usuario_id);

CREATE INDEX IF NOT EXISTS idx_oper_muestra_codigo
    ON operaciones_muestra (muestra_codigo);

CREATE INDEX IF NOT EXISTS idx_oper_muestra_compuesta
    ON operaciones_muestra (usuario_id, muestra_codigo, fecha_hora);

COMMIT;

