package itesm.mx.carpoolingtec.rides;

import itesm.mx.carpoolingtec.model.firebase.UserRide;

public interface RideItemListener {
    void onRideClick(UserRide userRide);
}