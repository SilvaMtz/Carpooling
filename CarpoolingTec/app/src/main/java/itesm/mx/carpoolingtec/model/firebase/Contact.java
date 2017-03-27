package itesm.mx.carpoolingtec.model.firebase;

public class Contact {

    private String name;
    private String photo;
    private String phone;

    public Contact() {

    }

    public Contact(String name, String photo, String phone) {
        this.name = name;
        this.photo = photo;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
