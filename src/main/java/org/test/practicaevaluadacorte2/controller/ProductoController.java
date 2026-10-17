package org.test.practicaevaluadacorte2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.test.practicaevaluadacorte2.DAO.ProductoDAO;
import org.test.practicaevaluadacorte2.database.ProductoDataBase;
import org.test.practicaevaluadacorte2.model.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoController {

    // LOS TEXTFIELD Y COMBOBOX

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCodigo;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;

    // LA TABLA

    @FXML
    private TableView<Producto> tblProductos;
    @FXML
    private TableColumn<Producto, Integer> colId;
    @FXML
    private TableColumn<Producto, String> colCodigo;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colCategoria;
    @FXML
    private TableColumn<Producto, Integer> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colStock;


    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private Producto productoSeleccionado;

    private static final String TODAS = "Todas";
    private static final List<String> CATEGORIAS = List.of("Papelería", "Tecnología", "Accesorios");
    private static final Double PRECIO_MAXIMO = new Double("99999999.99");

    @FXML
    public void initialize() {
        configurarTabla();
        configurarSeleccion();
        cargarOpcionesCategoria();
        cargarProductos();
    }

        private void cargarProductos () {
            try (Connection con = ProductoDataBase.getConnection()) {
                System.out.println("Conexión exitosa a: " + con.getCatalog());
            } catch (SQLException e) {
                System.out.println("Error de conexión. SQLState: " + e.getSQLState()
                        + " | " + e.getMessage());
            }
        }
    private void cargarOpcionesCategoria() {
    }

    private void configurarSeleccion() {
    }

    private void configurarTabla() {
    }

    @FXML
    private void onNuevo() {
    }

    @FXML
    private void onGuardar() {
    }

    @FXML
    private void onActualizar() {
    }

    @FXML
    private void onEliminar() {
    }

    @FXML
    private void onBuscar() {
    }

    @FXML
    private void onLimpiarFiltros() {
    }

    @FXML
    private void onRecargar() {
    }
}
