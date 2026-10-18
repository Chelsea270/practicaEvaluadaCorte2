# Pruebas — Catálogo de productos Papelería Campus

Base de prueba local `papeleria_campus`. Después de cada bloque se restauraron los 12 datos iniciales con `sql/datos.sql`.
Todas las capturas están en `evidencias/Evidencias_LabTableView.pdf`, identificadas con el código de cada prueba.

## Búsqueda y filtros (B1–B7)

| Caso | Entrada | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| B1 | Texto CUADERNO | P001 y P002; contador 2 | P001 y P002; Registros: 2 | ✅ | PDF – B1 |
| B2 | Texto T00 | T001 a T004; contador 4 | T001 a T004; Registros: 4 | ✅ | PDF – B2 |
| B3 | Categoría Papelería | P001 a P004; contador 4 | P001 a P004; Registros: 4 | ✅ | PDF – B3 |
| B4 | Solo con existencias, Todas | 9 productos, sin P004, T004, A003 | 9 productos, excluye P004, T004 y A003 | ✅ | PDF – B4 |
| B5 | USB + Tecnología + existencias | T001 y T003; contador 2 | T001 y T003; Registros: 2 | ✅ | PDF – B5 |
| B6 | Texto ZZZ999 | 0 filas, aviso Sin coincidencias | 0 filas, Registros: 0 y aviso | ✅ | PDF – B6 |
| B7 | Limpiar filtros tras B5 | Controles limpios y 12 filas | Controles restablecidos; Registros: 12 | ✅ | PDF – B7 |

## Ciclo CRUD

| Paso | Entrada | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| Crear | X001, Producto de prueba, Accesorios, 10.00, 1 | 13 filas | Guardado con ID 13; Registros: 13 | ✅ | PDF – CRUD 1 |
| Actualizar | X001 precio 15.50, stock 3 | 13 filas, solo X001 cambia | Solo X001 actualizado; Registros: 13 | ✅ | PDF – CRUD 2 |
| Cancelar eliminación | Eliminar X001 → Cancelar | X001 permanece | X001 permanece; Registros: 13 | ✅ | PDF – CRUD 3 |
| Eliminar | Eliminar X001 → Aceptar | 12 filas | X001 eliminado; Registros: 12 | ✅ | PDF – CRUD 4 |
| Verificación | SELECT en pgAdmin y reinicio | 12 filas | 12 filas en pgAdmin y en la app | ✅ | PDF – CRUD 5 |

## Validaciones (V1–V10)

| Grupo | Entradas | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| V1 | Código vacío, espacios, 11 caracteres | Rechaza | "Código inválido"; base sin cambios | ✅ | PDF – V1 |
| V2 | Nombre espacios, AB, 81 caracteres | Rechaza | "Nombre inválido" | ✅ | PDF – V2 |
| V3 | Categoría sin seleccionar | Rechaza | "Categoría inválida" | ✅ | PDF – V3 |
| V4 | Precio abc | Rechaza sin cerrar | "Precio inválido"; la app sigue abierta | ✅ | PDF – V4 |
| V5 | Precio 0 y -1 | Rechaza ambos | "El precio debe ser mayor que 0" | ✅ | PDF – V5 |
| V6 | Precio 12.345 y 100000000.00 | Rechaza sin redondear | Rechazo por decimales y por rango | ✅ | PDF – V6 |
| V7 | Stock abc y 2.5 | Rechaza | "Stock inválido" | ✅ | PDF – V7 |
| V8 | Stock -1, 2147483648 y 0 | Rechaza dos, admite 0 | Rechaza -1 y 2147483648; guarda con 0 | ✅ | PDF – V8 |
| V9 | " p001 " nuevo; P002→P001; P001 sin cambio | Rechaza duplicados; permite el propio | "Código duplicado" dos veces; P001 se actualiza | ✅ | PDF – V9 |
| V10 | Actualizar/Eliminar sin selección | Bloquea e informa | "Sin selección" en ambos | ✅ | PDF – V10 |

## Excepciones (E1–E4)

| Prueba | Acción | Esperado | Obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| E1 | Puerto 5433 en db.properties | Aviso de conexión, sin cierre ni falso listado | "Sin conexión"; Registros: —; consola BUSCAR 08001 | ✅ | PDF – E1 |
| E2 | PruebaE2: duplicado y precio negativo vía DAO | SQLException; integridad conservada | Rechazos 23505 y 23514; siguen 12 filas | ✅ | PDF – E2 |
| E3 | Borrar en pgAdmin y actualizar/eliminar en app | Aviso de que ya no existe, sin éxito falso | "Producto no encontrado… Pulse Recargar" | ✅ | PDF – E3 |
| E4 | Revisión de consola y alertas | Operación y SQLState registrados; mensaje seguro | Consola con operación y SQLState; alertas sin datos técnicos | ✅ | PDF – E1 y E2 |