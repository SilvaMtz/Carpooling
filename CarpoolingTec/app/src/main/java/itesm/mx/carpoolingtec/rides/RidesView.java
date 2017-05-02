package itesm.mx.carpoolingtec.rides;

import java.util.List;

import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;

public interface RidesView {

    void openUserRideDetails(User user,
                             List<String> ridesMonday,
                             List<String> ridesTuesday,
                             List<String> ridesWednesday,
                             List<String> ridesThursday,
                             List<String> ridesFriday,
                             List<String> ridesSaturday,
                             List<String> ridesSunday);
}
