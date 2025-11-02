-- Limpieza opcional: eliminar columna 'rol' si existe (modelo usa ManyToMany)
-- Nota: los scripts en /docker-entrypoint-initdb.d corren solo en la primera inicialización
-- Para entornos ya inicializados, ejecutar manualmente o reinicializar el volumen.

ALTER TABLE IF EXISTS usuarios
    DROP COLUMN IF EXISTS rol;

