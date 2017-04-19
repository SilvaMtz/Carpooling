package itesm.mx.carpoolingtec.model.firebase;

public class Contact {

    private String id;
    private String name;
    private String photo;
    private String phone;

    public Contact() {

    }

    public Contact(String id, String name, String photo, String phone) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
