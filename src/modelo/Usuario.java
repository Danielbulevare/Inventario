package modelo;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasen;
    private String rol;
    private String estatus;

    public Usuario() {
        this.idUsuario = 0;
        this.nombre = "";
        this.correo = "";
        this.contrasen = "";
        this.rol = "";
        this.estatus = "";
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasen() {
        return contrasen;
    }

    public void setContrasen(String contrasen) {
        this.contrasen = contrasen;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
