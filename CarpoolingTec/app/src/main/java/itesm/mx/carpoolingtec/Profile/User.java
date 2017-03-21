package itesm.mx.carpoolingtec.Profile;

/**
 * Created by DavidMartinez on 3/21/17.
 */

public class User {

    private String nombre;
    private String matricula;
    private String celular;
    private String foto;

    public User(String arg) {

        this.matricula = matricula;
    }

    public static void main (String [] args)
    {
        for (String arg : args)
        {
            User pojoProfile = new User(arg);

        }
    }
    // get
    public String getNombre() { return this.nombre; }
    public String getMatricula() { return this.matricula; }
    public String getCelular() { return this.celular; }
    public String getFoto() { return this.foto; }

    // set
    public void setCelular(String tel) { this.celular = tel; }
    public void setNombre(String name) { this.nombre = name; }
    public void setMatricula(String studID) { this.matricula = studID; }
    public void setFoto(String fotoName) { this.foto = fotoName; }
}
