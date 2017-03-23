package itesm.mx.carpoolingtec.model;

public class User {

    private String nombre;
    private String matricula;
    private String celular;
    private String foto;
    private String location;

    public User(String foto, String location) {
        this.foto = foto;
        this.location = location;
    }

    public User(String nombre, String matricula, String celular, String foto, String location) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.celular = celular;
        this.foto = foto;
        this.location = location;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
