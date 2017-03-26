package itesm.mx.carpoolingtec.model;

import android.location.Location;

public class UserLocationPair {

    private User user;
    private Location location;

    public UserLocationPair(User user, Location location) {
        this.user = user;
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
