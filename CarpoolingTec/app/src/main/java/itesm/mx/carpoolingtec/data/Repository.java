package itesm.mx.carpoolingtec.data;

import io.reactivex.Observable;
import itesm.mx.carpoolingtec.model.firebase.Contact;
import itesm.mx.carpoolingtec.model.firebase.UserRide;

public interface Repository {

    Observable<Contact> getContacts(String userId);

    Observable<UserRide> getUserRides(int rideType);
}
