# Catálogo de productos — Papelería Campus

Aplicación JavaFX + PostgreSQL para consultar, buscar y administrar (CRUD) los productos de una papelería, con validaciones en la interfaz y restricciones en la base de datos.

**Autora:** Chelsea Yosmara Quintanilla Blandón · **Grupo:** ____ · **Docente:** Freddy Luis López Barrios

## Tecnologías
JDK 26 (nivel 21) · JavaFX 21.0.6 · PostgreSQL 18 · Driver JDBC 42.7.13 · Maven · IntelliJ IDEA

## Base de datos
1. En pgAdmin: `CREATE DATABASE papeleria_campus;`
2. Abrir el Query Tool **sobre `papeleria_campus`** y ejecutar `sql/estructura.sql` y luego `sql/datos.sql`.
3. Para restaurar los 12 productos después de las pruebas, volver a ejecutar `sql/datos.sql`.

## Configuración y ejecución
1. Copiar `db.properties.example` como `db.properties` (en la raíz) y poner la contraseña local. Este archivo no se sube: está en `.gitignore`.
2. Ejecutar `HelloApplication` (o `mvnw clean javafx:run`).

## Estructura
| Clase | Función |
|---|---|
| `Producto` | Modelo de una fila (precio en BigDecimal). |
| `ProductoDataBase` | Abre la conexión leyendo `db.properties`. |
| `ProductoDAO` | Todo el SQL con PreparedStatement: buscar, insertar, actualizar, eliminar. |
| `ProductoController` | Eventos, validaciones y carga del TableView. |
| `PruebaE2` | Prueba del DAO sin la interfaz. |

**Flujo:** Interfaz → Controlador → DAO → PostgreSQL → ResultSet → Producto → ObservableList → TableView.

## Pruebas
Resultados en `pruebas.md` y capturas en `evidencias/`.

## Fuentes y apoyo
Guía del laboratorio, documentación de PostgreSQL y JDBC, proyecto de clase ActividadPractica2 y Claude (IA) como apoyo en revisión de código y documentación.