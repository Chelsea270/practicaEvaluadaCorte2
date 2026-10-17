-- Requiere crear antes la base: CREATE DATABASE papeleria_campus;
-- Ejecutar en el Query Tool de papeleria_campus.

-- Esquema que agrupa las tablas del catálogo
CREATE SCHEMA catalogo;

-- Creacion de la tabla en el esquema
CREATE TABLE catalogo.producto (
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo      VARCHAR(10)   NOT NULL,
    nombre      VARCHAR(80)   NOT NULL,
    categoria   VARCHAR(20)   NOT NULL,
    precio      NUMERIC(10,2) NOT NULL,
    stock       INTEGER       NOT NULL,

    -- Restricciones con nombre propio.
    CONSTRAINT uq_producto_codigo UNIQUE (codigo),
    -- Código sin espacios laterales, en mayúsculas y no vacío.
    CONSTRAINT ck_producto_codigo
        CHECK (codigo = UPPER(BTRIM(codigo)) AND codigo <> ''),
    -- Nombre de 3 a 80 caracteres después de quitar espacios.
    CONSTRAINT ck_producto_nombre
        CHECK (CHAR_LENGTH(BTRIM(nombre)) BETWEEN 3 AND 80),
    -- Solo deben existir 3 categorias.
    CONSTRAINT ck_producto_categoria
        CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),
    -- Precio positivo.
    CONSTRAINT ck_producto_precio
        CHECK (precio > 0),
    -- Stock no negativo, permitido 0.
    CONSTRAINT ck_producto_stock
        CHECK (stock >= 0)
);