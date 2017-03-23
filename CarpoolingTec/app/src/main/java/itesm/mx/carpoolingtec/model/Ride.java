package itesm.mx.carpoolingtec.model;

import java.util.List;

public class Ride {

    public enum Gender {
        HOMBRE, MUJER
    }

    private User driver;
    private List<User> passengers;
    private boolean smoking;
    private List<Gender> genders;
    private String description;
    private boolean[] weekdays;
    private String time;

    public Ride() {
    }

    public Ride(User driver, boolean[] weekdays, String time) {
        this.driver = driver;
        this.weekdays = weekdays;
        this.time = time;
    }

    public Ride(User driver, List<User> passengers, boolean smoking, List<Gender> genders,
                String description, boolean[] weekdays, String time) {
        this.driver = driver;
        this.passengers = passengers;
        this.smoking = smoking;
        this.genders = genders;
        this.description = description;
        this.weekdays = weekdays;
        this.time = time;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean[] getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(boolean[] weekdays) {
        this.weekdays = weekdays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
