package org.test.practicaevaluadacorte2.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.test.practicaevaluadacorte2.DAO.ProductoDAO;
import org.test.practicaevaluadacorte2.model.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoController {

    // FORMULARIO
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

    // BUSQUEDA
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> cmbFiltroCategoria;
    @FXML
    private CheckBox chkConExistencias;

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
    private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colStock;
    @FXML
    private Label lblContador;

    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private Producto productoSeleccionado;

    private static final String TODAS = "Todas";
    private static final List<String> CATEGORIAS = List.of("Papelería", "Tecnología", "Accesorios");
    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("99999999.99");

    @FXML
    public void initialize() {
        configurarTabla();
        configurarSeleccion();
        cargarOpcionesCategoria();
        cargarProductos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getIdProducto()));
        colCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigo()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategoria()));
        colPrecio.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getPrecio()));
        colStock.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getStock()));

        tblProductos.setItems(listaProductos);
        tblProductos.setPlaceholder(new Label("Sin coincidencias"));
    }

    private void configurarSeleccion() {
        tblProductos.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, nuevo) -> {
                    if (nuevo != null) {
                        productoSeleccionado = nuevo;
                        txtId.setText(String.valueOf(nuevo.getIdProducto()));
                        txtCodigo.setText(nuevo.getCodigo());
                        txtNombre.setText(nuevo.getNombre());
                        cmbCategoria.setValue(nuevo.getCategoria());
                        txtPrecio.setText(nuevo.getPrecio().toPlainString());
                        txtStock.setText(String.valueOf(nuevo.getStock()));
                    }
                });
    }

    private void cargarOpcionesCategoria() {
        cmbCategoria.setItems(FXCollections.observableArrayList(CATEGORIAS));

        cmbFiltroCategoria.getItems().add(TODAS);
        cmbFiltroCategoria.getItems().addAll(CATEGORIAS);
        cmbFiltroCategoria.setValue(TODAS);
    }

    // CARGAR Y BUSCAR

    private boolean cargarProductos() {
        String categoria = cmbFiltroCategoria.getValue();
        if (TODAS.equals(categoria)) {
            categoria = null;
        }

        try {
            List<Producto> resultado = productoDAO.buscar(
                    txtBuscar.getText(), categoria, chkConExistencias.isSelected());

            listaProductos.setAll(resultado);
            lblContador.setText("Registros: " + listaProductos.size());
            tblProductos.setPlaceholder(new Label("Sin coincidencias"));
            return true;

        } catch (SQLException e) {
            listaProductos.clear();
            lblContador.setText("Registros: —");
            tblProductos.setPlaceholder(new Label("No se pudo cargar la información"));
            manejarErrorBD(e, "No se pudieron cargar los productos.");
            return false;
        }
    }

    @FXML
    private void onBuscar() {
        if (cargarProductos() && listaProductos.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin coincidencias",
                    "No hay productos que coincidan con la búsqueda.");
        }
    }

    @FXML
    private void onLimpiarFiltros() {
        txtBuscar.clear();
        cmbFiltroCategoria.setValue(TODAS);
        chkConExistencias.setSelected(false);
        cargarProductos();
    }

    @FXML
    private void onRecargar() {
        cargarProductos();
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) {
            return;
        }

        Producto nuevo = crearProductoDesdeFormulario(null);

        try {
            productoDAO.insertar(nuevo);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto registrado",
                    "El producto fue guardado correctamente.");
            limpiarFormulario();
            cargarProductos();

        } catch (SQLException e) {
            manejarErrorBD(e, "No se pudo guardar el producto.");
        }
    }

    @FXML
    private void onActualizar() {
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección",
                    "Seleccione un producto de la tabla para actualizarlo.");
            return;
        }
        if (!validarFormulario()) {
            return;
        }

        Producto editado = crearProductoDesdeFormulario(productoSeleccionado.getIdProducto());

        try {
            int filas = productoDAO.actualizar(editado);

            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado",
                        "El producto ya no existe en la base de datos. Pulse Recargar.");
                return;
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto actualizado",
                    "Los cambios fueron guardados.");
            limpiarFormulario();
            cargarProductos();

        } catch (SQLException e) {
            manejarErrorBD(e, "No se pudo actualizar el producto.");
        }
    }

    @FXML
    private void onEliminar() {
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección",
                    "Seleccione un producto de la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Desea eliminar el producto "
                + productoSeleccionado.getCodigo() + " - " + productoSeleccionado.getNombre() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            return;
        }

        try {
            int filas = productoDAO.eliminar(productoSeleccionado.getIdProducto());

            if (filas == 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado",
                        "El producto ya no existe en la base de datos. Pulse Recargar.");
                return;
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Producto eliminado",
                    "El producto fue eliminado correctamente.");
            limpiarFormulario();
            cargarProductos();

        } catch (SQLException e) {
            manejarErrorBD(e, "No se pudo eliminar el producto.");
        }
    }

    // Arma el Producto con los datos del formulario ya normalizados
    private Producto crearProductoDesdeFormulario(Integer id) {
        return new Producto(
                id,
                txtCodigo.getText().trim().toUpperCase(),
                txtNombre.getText().trim(),
                cmbCategoria.getValue(),
                new BigDecimal(txtPrecio.getText().trim()),
                Integer.parseInt(txtStock.getText().trim()));
    }

    // LIMPIAR

    @FXML
    private void onNuevo() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        productoSeleccionado = null;
        tblProductos.getSelectionModel().clearSelection();
    }

    // VALIDACIONES

    private boolean validarFormulario() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty() || codigo.length() > 10) {
            mostrarAlerta(Alert.AlertType.WARNING, "Código inválido",
                    "El código debe tener de 1 a 10 caracteres (sin espacios a los lados).");
            return false;
        }

        String nombre = txtNombre.getText().trim();
        if (nombre.length() < 3 || nombre.length() > 80) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nombre inválido",
                    "El nombre debe tener de 3 a 80 caracteres (sin espacios a los lados).");
            return false;
        }

        String categoria = cmbCategoria.getValue();
        if (categoria == null || !CATEGORIAS.contains(categoria)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Categoría inválida",
                    "Seleccione una categoría: Papelería, Tecnología o Accesorios.");
            return false;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                    "El precio debe ser un número (ejemplo: 65.00).");
            return false;
        }
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                    "El precio debe ser mayor que 0.");
            return false;
        }
        if (precio.stripTrailingZeros().scale() > 2) {
            mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                    "El precio admite como máximo 2 decimales.");
            return false;
        }
        if (precio.compareTo(PRECIO_MAXIMO) > 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                    "El precio máximo permitido es 99999999.99.");
            return false;
        }

        try {
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Stock inválido",
                        "El stock no puede ser negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Stock inválido",
                    "El stock debe ser un número entero entre 0 y 2147483647.");
            return false;
        }

        return true;
    }

    // ERRORES Y MENSAJES

    private void manejarErrorBD(SQLException e, String mensajeGeneral) {
        String estado = e.getSQLState();

        if ("23505".equals(estado)) {                       // UNIQUE
            mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                    "Ya existe un producto con ese código.");
        } else if ("23514".equals(estado)) {                // CHECK
            mostrarAlerta(Alert.AlertType.WARNING, "Dato no permitido",
                    "La base de datos rechazó el dato porque no cumple las reglas del catálogo.");
        } else if (estado != null && (estado.startsWith("08")      // conexión
                || "28P01".equals(estado)                          // usuario o contraseña
                || "3D000".equals(estado))) {                      // base no existe
            mostrarAlerta(Alert.AlertType.ERROR, "Sin conexión",
                    "No se pudo conectar con la base de datos. Verifique la configuración y pulse Recargar.");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos", mensajeGeneral);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}