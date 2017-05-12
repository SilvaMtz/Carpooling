package itesm.mx.carpoolingtec.profile;

import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;

public interface ProfileView {

    void openEditRideOptions(Ride ride, String key);

    void openPostActivity(Ride ride, String key);

    void showUserData(User user);
}
