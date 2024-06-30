package modelo;

public class ContenedorObjetosTransaccion {
    private Usuario usuario;
    private Producto producto;
    private TransaccionInventario transaccionInventario;

    public ContenedorObjetosTransaccion(Usuario usuario, Producto producto, TransaccionInventario transaccionInventario) {
        this.usuario = usuario;
        this.producto = producto;
        this.transaccionInventario = transaccionInventario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TransaccionInventario getTransaccionInventario() {
        return transaccionInventario;
    }

    public void setTransaccionInventario(TransaccionInventario transaccionInventario) {
        this.transaccionInventario = transaccionInventario;
    }
}
