package org.test.practicaevaluadacorte2.model;

public class producto {

    private Integer idProducto;
    private String codigo;
    private String nombre;
    private String categoria;
    private Double precio;
    private int stock;

    public producto() {
    }

    public producto(Integer idProducto, int stock, Double precio, String nombre, String codigo, String categoria) {
        this.idProducto = idProducto;
        this.stock = stock;
        this.precio = precio;
        this.nombre = nombre;
        this.codigo = codigo;
        this.categoria = categoria;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
