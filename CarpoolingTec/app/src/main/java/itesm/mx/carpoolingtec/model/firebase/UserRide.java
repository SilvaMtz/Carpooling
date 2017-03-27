package itesm.mx.carpoolingtec.model.firebase;

import java.util.Map;

public class UserRide {

    private User user;
    private Map<String, Ride> rides;

    public UserRide() {

    }

    public UserRide(User user, Map<String, Ride> rides) {
        this.user = user;
        this.rides = rides;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Ride> getRides() {
        return rides;
    }

    public void setRides(Map<String, Ride> rides) {
        this.rides = rides;
    }
}
