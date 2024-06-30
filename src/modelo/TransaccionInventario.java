package modelo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TransaccionInventario {
    private int idProducto;
    private int idTransaccionInventario;
    private String producto;
    private int cantidad;
    private String movimiento;
    private Timestamp fechaHoraTransaccion;
    private String realizadoPorUsuario;
    private String fechaJson;

    public TransaccionInventario() {
        this.idProducto = 0;
        this.idTransaccionInventario = 0;
        this.producto = "";
        this.cantidad = 0;
        this.movimiento = "";
        this.fechaHoraTransaccion = new Timestamp(Calendar.getInstance().getTimeInMillis()); //Fecha actual
        this.realizadoPorUsuario = "";
        
        // Formato de fecha y hora en ISO 8601
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.fechaJson = sdf.format(fechaHoraTransaccion);
    }

    public int getIdTransaccionInventario() {
        return idTransaccionInventario;
    }

    public void setIdTransaccionInventario(int idTransaccionInventario) {
        this.idTransaccionInventario = idTransaccionInventario;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getRealizadoPorUsuario() {
        return realizadoPorUsuario;
    }

    public void setRealizadoPorUsuario(String realizadoPorUsuario) {
        this.realizadoPorUsuario = realizadoPorUsuario;
    }

    public Timestamp getFechaHoraTransaccion() {
        return fechaHoraTransaccion;
    }

    public void setFechaHoraTransaccion(Timestamp fechaHoraTransaccion) {
        this.fechaHoraTransaccion = fechaHoraTransaccion;
    }

    public String getFechaJson() {
        return fechaJson;
    }

    public void setFechaJson(String fechaJson) {
        this.fechaJson = fechaJson;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
