package itesm.mx.carpoolingtec.model.firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String id;
    private String name;
    private String phone;
    private String photo;
    private int seats;
    private double latitude;
    private double longitude;
    private boolean smoking;
    private int gender;
    private int passenger_gender;
    private String notes;
    List<User> contacts;
    Map<String, Boolean> rides;

    public User() {

    }

    public User(String id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public User(String id, String name, String phone, String photo, int seats, double latitude, double longitude, boolean smoking, int gender, int passenger_gender, String notes, List<User> contacts, Map<String, Boolean> rides) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.photo = photo;
        this.seats = seats;
        this.latitude = latitude;
        this.longitude = longitude;
        this.smoking = smoking;
        this.gender = gender;
        this.passenger_gender = passenger_gender;
        this.notes = notes;
        this.contacts = contacts;
        this.rides = rides;
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

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getPassenger_gender() {
        return passenger_gender;
    }

    public void setPassenger_gender(int passenger_gender) {
        this.passenger_gender = passenger_gender;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public Map<String, Boolean> getRides() {
        return rides;
    }

    public void setRides(Map<String, Boolean> rides) {
        this.rides = rides;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("photo", photo);
        return map;
    }
}
