-- Datos de ejemplo para ambiente de desarrollo
-- Se ejecuta al inicializar la BD (solo con volumen vacío)

BEGIN;

-- =====================
--  Catalogos / Usuarios y Roles
-- =====================

INSERT INTO roles (nombre) VALUES
    ('admin'),
    ('investigador'),
    ('encuestador')
ON CONFLICT DO NOTHING;

INSERT INTO usuarios (nombre, correo, contrasena)
VALUES
    ('Admin',         'admin@gatoscopio.local',        'admin'),
    ('Investigadora', 'investigadora@gatoscopio.local','investigadora'),
    ('Encuestador',   'encuestador@gatoscopio.local',  'encuestador')
ON CONFLICT (correo) DO NOTHING;

-- Asignación de roles
INSERT INTO usuarios_roles (usuario_id, rol_nombre)
SELECT u.id, 'admin' FROM usuarios u WHERE u.correo = 'admin@gatoscopio.local'
ON CONFLICT DO NOTHING;

INSERT INTO usuarios_roles (usuario_id, rol_nombre)
SELECT u.id, 'investigador' FROM usuarios u WHERE u.correo = 'investigadora@gatoscopio.local'
ON CONFLICT DO NOTHING;

INSERT INTO usuarios_roles (usuario_id, rol_nombre)
SELECT u.id, 'encuestador' FROM usuarios u WHERE u.correo = 'encuestador@gatoscopio.local'
ON CONFLICT DO NOTHING;


-- =====================
--  Pacientes
-- =====================

INSERT INTO pacientes (codigo, nombre, correo, telefono, caso_control) VALUES
    ('PAC001', 'Juan Pérez',   'juan.perez@example.com',   '+56911111111', 'Caso'),
    ('PAC002', 'María López',  'maria.lopez@example.com',  '+56922222222', 'Control'),
    ('PAC003', 'Carlos Gómez', 'carlos.gomez@example.com', '+56933333333', 'Caso')
ON CONFLICT DO NOTHING;


-- =====================
--  Preguntas + valores válidos
-- =====================

INSERT INTO preguntas (nombre_variable, codigo, tipo_dato, enunciado, audiencia) VALUES
    ('edad',  'P001', 'numero', '¿Cuál es su edad?', 'paciente'),
    ('fuma',  'P002', 'texto',  '¿Fuma actualmente? (si/no)', 'paciente'),
    ('peso',  'P003', 'numero', 'Peso actual (kg)', 'paciente')
ON CONFLICT (nombre_variable) DO NOTHING;

-- Valores válidos para "fuma"
INSERT INTO valor_valido_pregunta (valor, pregunta_id)
SELECT v.valor, p.id
FROM (VALUES ('si'), ('no')) AS v(valor)
JOIN preguntas p ON p.nombre_variable = 'fuma'
ON CONFLICT DO NOTHING;


-- =====================
--  Tipos de muestra y Muestras
-- =====================

INSERT INTO tipos_muestra (nombre, unidad_medida, minimo_alerta) VALUES
    ('Sangre', 'mL', 1.0),
    ('Saliva', 'mL', 1.0)
ON CONFLICT (nombre) DO NOTHING;

-- Muestra para PAC001
INSERT INTO muestras (codigo, tipo_muestra_id, paciente_codigo, observacion, cantidad_donada)
SELECT 'MX-001', tm.id, 'PAC001', 'Ingreso inicial', 10.0
FROM tipos_muestra tm WHERE tm.nombre = 'Sangre'
ON CONFLICT DO NOTHING;


-- =====================
--  Encuestas + relación con usuarios
-- =====================

-- Encuesta para PAC001 realizada por encuestador
WITH e AS (
  INSERT INTO encuestas (estado, paciente_codigo)
  VALUES ('Incompleto', 'PAC001')
  RETURNING id
)
INSERT INTO usuarios_encuestas (usuario_id, encuesta_id, fecha_hora_modificacion)
SELECT u.id, e.id, now()
FROM e, usuarios u
WHERE u.correo = 'encuestador@gatoscopio.local'
ON CONFLICT DO NOTHING;

-- Respuestas para la última encuesta de PAC001
WITH enc AS (
  SELECT id FROM encuestas WHERE paciente_codigo='PAC001' ORDER BY id DESC LIMIT 1
)
INSERT INTO respuestas (encuesta_id, pregunta_id, valor)
SELECT enc.id, p.id, '34' FROM enc, preguntas p WHERE p.nombre_variable='edad'
UNION ALL
SELECT enc.id, p.id, 'si' FROM enc, preguntas p WHERE p.nombre_variable='fuma'
UNION ALL
SELECT enc.id, p.id, '72.5' FROM enc, preguntas p WHERE p.nombre_variable='peso';


-- =====================
--  Reglas de dicotomización + valores resultantes
-- =====================

-- Regla: fuma == 'si'
INSERT INTO reglas_dicotomizacion (pregunta_id, nombre, tipo_regla, activa, valor)
SELECT p.id, 'es_si_fuma', 'igual', true, 'si'
FROM preguntas p WHERE p.nombre_variable='fuma'
ON CONFLICT DO NOTHING;

-- Aplicación de regla a la respuesta de "fuma" de la última encuesta de PAC001
WITH r AS (
  SELECT id FROM reglas_dicotomizacion WHERE nombre='es_si_fuma'
), enc AS (
  SELECT id FROM encuestas WHERE paciente_codigo='PAC001' ORDER BY id DESC LIMIT 1
), preg AS (
  SELECT id FROM preguntas WHERE nombre_variable='fuma'
), resp AS (
  SELECT id FROM respuestas WHERE encuesta_id=(SELECT id FROM enc) AND pregunta_id=(SELECT id FROM preg)
)
INSERT INTO valores_dicotomizados (regla_id, respuesta_id)
SELECT (SELECT id FROM r), (SELECT id FROM resp)
ON CONFLICT DO NOTHING;


-- =====================
--  Operaciones sobre muestra
-- =====================

INSERT INTO operaciones_muestra (usuario_id, muestra_codigo, tipo_movimiento, fecha_hora, observacion, cantidad)
SELECT u.id, 'MX-001', 'ingreso', now(), 'Recepción de muestra', 10.0
FROM usuarios u WHERE u.correo='encuestador@gatoscopio.local'
ON CONFLICT DO NOTHING;

COMMIT;

