package org.test.practicaevaluadacorte2.DAO;

import org.test.practicaevaluadacorte2.database.ProductoDataBase;
import org.test.practicaevaluadacorte2.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String SELECT_BASE = """
            SELECT id_producto, codigo, nombre, categoria, precio, stock 
            FROM catalogo.producto WHERE 1 = 1""";

// BUSCAR.

    public List<Producto> buscar(String texto, String categoria, boolean soloConExistencias)
            throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_BASE);
        List<String> parametros = new ArrayList<>();

        if (texto != null && !texto.isBlank()) {
            sql.append(" AND (nombre ILIKE ? OR codigo ILIKE ?)");
            String patron = "%" + texto.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }
        if (categoria != null) {
            sql.append(" AND categoria = ?");
            parametros.add(categoria);
        }
        if (soloConExistencias) {
            sql.append(" AND stock > 0");
        }
        sql.append(" ORDER BY codigo");

        List<Producto> listaProductos = new ArrayList<>();

        try (Connection con = ProductoDataBase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setString(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaProductos.add(crearProductoDesdeResultSet(rs));
                }
            }
        }
        catch (SQLException e) {
                registrarError("BUSCAR", e);
                throw e;
        }
        return listaProductos;
    }
    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id_producto"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getBigDecimal("precio"),
                rs.getInt("stock"));
    }

    // INSERTAR PRODUCTO.

    public int insertar( Producto p) throws SQLException{
        String sql = """
                 INSERT INTO catalogo.producto ( codigo, nombre, categoria, precio, stock)
                 VALUES (?, ?, ?, ?, ?)""";

        try (Connection con = ProductoDataBase.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            asignarDatos(ps, p);
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            registrarError("INSERTAR", e);
            throw e;
        }
    }

    // ACTUALIZAR
    public int actualizar(Producto p) throws SQLException {
        String sql = """
                 UPDATE catalogo.producto SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ?
                 WHERE id_producto = ?""";

        try (Connection con = ProductoDataBase.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){
            asignarDatos(ps, p);
            ps.setInt(6, p.getIdProducto());
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            registrarError("ACTUALIZAR", e);
            throw e;
        }
    }

    // ELIMINAR
    public int eliminar(int idProducto) throws SQLException {
        String sql = """
                DELETE FROM catalogo.producto WHERE id_producto = ?""";
        try (Connection con = ProductoDataBase.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1,  idProducto);
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            registrarError("ELIMINAR", e);
            throw e;
        }
    }

    //Los 5 datos de los productos (?)
    private void asignarDatos(PreparedStatement ps, Producto p) throws SQLException {
        ps.setString(1, p.getCodigo());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getCategoria());
        ps.setBigDecimal(4, p.getPrecio());
        ps.setInt(5, p.getStock());
    }

    // Registra operación y SQLState en consola
    private void registrarError(String operacion, SQLException e) {
        System.err.println("[ProductoDAO] Operación: " + operacion
            + " | SQLState: " + e.getSQLState()
            + " | Detalle: " + e.getMessage());
    }
}
