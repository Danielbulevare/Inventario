package modelo;

public class Producto {
    private int id_producto;
    private String producto;
    private String estatus;
    private int cantidad;

    public Producto() {
        this.id_producto = 0;
        this.producto = "";
        this.estatus = "";
        this.cantidad = 0;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String id_estatus) {
        this.estatus = id_estatus;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
