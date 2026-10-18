package org.test.practicaevaluadacorte2;

import org.test.practicaevaluadacorte2.DAO.ProductoDAO;
import org.test.practicaevaluadacorte2.model.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Prueba E2: llama al DAO directamente, sin las validaciones de la interfaz,
 * para comprobar que PostgreSQL rechaza los datos inválidos (UNIQUE y CHECK)
 * y que la SQLException se gestiona sin perder la integridad.
 * No forma parte de la interfaz; se ejecuta aparte con su propio main.
 */
public class PruebaE2 {

    public static void main(String[] args) {
        ProductoDAO dao = new ProductoDAO();

        // Caso 1: código duplicado.Debe fallar por UNIQUE
        probar(dao, "UNIQUE codigo",
                new Producto(null, "P001", "Producto duplicado", "Papelería", new BigDecimal("10.00"), 1));

        // Caso 2: precio negativo. Debe fallar por CHECK
        probar(dao, "CHECK precio",
                new Producto(null, "X002", "Producto prueba", "Papelería", new BigDecimal("-5.00"), 1));
    }

    private static void probar(ProductoDAO dao, String caso, Producto p) {
        try {
            dao.insertar(p);
            System.out.println("[E2] " + caso + ": ERROR, el dato inválido fue insertado");
        } catch (SQLException e) {
            System.out.println("[E2] " + caso + ": rechazado correctamente. SQLState: " + e.getSQLState());
        }
    }
}