package org.test.practicaevaluadacorte2.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abre la conexión con PostgreSQL.
 * Buena práctica: la URL, el usuario y la contraseña no aparecen en el código.
 * Se leen del archivo externo db.properties, excluido con .gitignore.
 * En el repositorio solo se publica db.properties.example, sin secretos.
 */

public class ProductoDataBase {

    private static final String ARCHIVO = "db.properties";

    private ProductoDataBase (){
    }

    public static Connection getConnection() throws SQLException {
        Properties prop = new Properties();

        try (InputStream in = new FileInputStream(ARCHIVO)) {
            prop.load(in);
        } catch (IOException e) {
            throw new SQLException("No se encontró el archivo de configuración " + ARCHIVO, "08001", e);
        }
        return DriverManager.getConnection(
                prop.getProperty("db.url"),
                prop.getProperty("db.user"),
                prop.getProperty("db.password"));
    }
}

