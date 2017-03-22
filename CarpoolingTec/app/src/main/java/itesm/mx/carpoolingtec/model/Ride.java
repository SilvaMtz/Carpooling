package itesm.mx.carpoolingtec.model;

import java.util.List;

public class Ride {

    private String driverId;
    private List<String> passengersIds;
    private boolean smoking;
    private int genders;
    private String description;
    private boolean[] weekdays;
    private String time;

    public Ride() {
    }

    public Ride(String driverId, List<String> passengersIds, boolean smoking, int genders, String description, boolean[] weekdays, String time) {
        this.driverId = driverId;
        this.passengersIds = passengersIds;
        this.smoking = smoking;
        this.genders = genders;
        this.description = description;
        this.weekdays = weekdays;
        this.time = time;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public List<String> getPassengersIds() {
        return passengersIds;
    }

    public String getPassangers_IdStudent(int index) {
        return passengersIds.get(index);
    }

    public void setPassengersIds(List<String> passengersIds) {
        this.passengersIds = passengersIds;
    }

    public int getGenders() {
        return genders;
    }

    public String getGendersString() {
        switch (genders){
            case 1:
                return "Male";
            case 2:
                return "Female";
            case 3:
                return "Both";
        }
        return null;
    }

    public void setGenders(int genders) {
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

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }
}
