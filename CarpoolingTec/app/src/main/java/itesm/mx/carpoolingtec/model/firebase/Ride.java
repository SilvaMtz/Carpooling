package itesm.mx.carpoolingtec.model.firebase;

import java.util.HashMap;
import java.util.Map;

public class Ride {

    private String ride_type;
    private String time;
    private String weekday;
    private double longitude;
    private double latitude;
    private String key;

    public Ride() {

    }

    public Ride(String ride_type, String time, String weekday, double longitude, double latitude, String key) {
        this.ride_type = ride_type;
        this.time = time;
        this.weekday = weekday;
        this.longitude = longitude;
        this.latitude = latitude;
        this.key = key;
    }

    public Ride(String ride_type, String time, String weekday, double longitude, double latitude) {
        this.ride_type = ride_type;
        this.time = time;
        this.weekday = weekday;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("weekday", weekday);
        map.put("ride_type", ride_type);
        map.put("time", time);
        return map;
    }
}
