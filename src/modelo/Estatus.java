package modelo;

public class Estatus {
    private int id_estatus;
    private String estatus;

    public Estatus() {
        this.id_estatus = 0;
        this.estatus = "";
    }

    public int getId_estatus() {
        return id_estatus;
    }

    public void setId_estatus(int id_estatus) {
        this.id_estatus = id_estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
