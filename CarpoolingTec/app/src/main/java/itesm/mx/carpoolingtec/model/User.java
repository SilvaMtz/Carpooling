package itesm.mx.carpoolingtec.model;

public class User {

    private String name;
    private String matricula;
    private String phone;
    private String photo;
    private String location;

    public User(String photo, String location) {
        this.photo = photo;
        this.location = location;
    }

    public User(String name, String photo, String location) {
        this.name = name;
        this.photo = photo;
        this.location = location;
    }

    public User(String name, String matricula, String phone, String photo, String location) {
        this.name = name;
        this.matricula = matricula;
        this.phone = phone;
        this.photo = photo;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
