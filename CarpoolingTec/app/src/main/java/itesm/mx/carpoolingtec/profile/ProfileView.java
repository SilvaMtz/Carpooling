package itesm.mx.carpoolingtec.profile;

import itesm.mx.carpoolingtec.model.firebase.Ride;

public interface ProfileView {

    void addRide(Ride ride);

    void removeRide(Ride ride);

    void openEditRideOptions(Ride ride, String key);

    void showRemovedRideToast();
}
