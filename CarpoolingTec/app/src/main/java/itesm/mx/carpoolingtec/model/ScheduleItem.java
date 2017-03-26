package itesm.mx.carpoolingtec.model;

public class ScheduleItem {

    public enum RideType {
        DRIVER, PASSENGER, DAY_TEXT
    }

    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THRUSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    private User driver;
    private User passenger;
    private RideType rideType;
    private String time;
    private DayOfWeek day;

    public ScheduleItem(RideType rideType, DayOfWeek day) {
        this.rideType = rideType;
        this.day = day;
    }

    public ScheduleItem(User driver, User passenger, RideType rideType, String time, DayOfWeek day) {
        this.driver = driver;
        this.passenger = passenger;
        this.rideType = rideType;
        this.time = time;
        this.day = day;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public RideType getRideType() {
        return rideType;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }
}
