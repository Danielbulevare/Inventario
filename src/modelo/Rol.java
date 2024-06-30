package modelo;

public class Rol {
    private int id_rol;
    private String rol;

    public Rol() {
        this.id_rol = 0;
        this.rol = "";
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
