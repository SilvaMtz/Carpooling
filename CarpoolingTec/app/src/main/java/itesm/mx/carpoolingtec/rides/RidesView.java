package itesm.mx.carpoolingtec.rides;

import itesm.mx.carpoolingtec.model.firebase.UserRide;

public interface RidesView {

    void setLoadingIndicator(boolean active);

    void showUserRides();

    void hideUserRides();

    void addUserRide(UserRide userRide);

    void clearUserRides();

    void showErrorLoadingRidesToast();
}
