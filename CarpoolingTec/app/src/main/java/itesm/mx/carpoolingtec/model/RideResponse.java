package itesm.mx.carpoolingtec.model;

public class RideResponse {

    private String driver;
    private String weekday;
    private String time;
    private String ride_type;
    private double latitude;
    private double longitude;

    public RideResponse() {

    }

    public RideResponse(String driver) {
        this.driver = driver;
    }

    public RideResponse(String driver, String weekday, String time, String ride_type, double latitude, double longitude) {
        this.driver = driver;
        this.weekday = weekday;
        this.time = time;
        this.ride_type = ride_type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
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
}
