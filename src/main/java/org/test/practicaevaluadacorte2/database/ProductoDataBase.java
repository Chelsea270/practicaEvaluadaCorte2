package org.test.practicaevaluadacorte2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProductoDataBase {

    private static final String URL = "jdbc:postgresql://localhost:5432/papeleria_campus";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin123";

    private ProductoDataBase() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); // clase encargada de la conexion.
    }
}

