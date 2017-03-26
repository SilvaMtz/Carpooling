package itesm.mx.carpoolingtec.model;

public class User {

    private String name;
    private String studentId;
    private String phone;
    private String photo;

    public User(String photo) {
        this.photo = photo;
    }

    public User(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    public User(String name, String studentId, String phone, String photo) {
        this.name = name;
        this.studentId = studentId;
        this.phone = phone;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

}
