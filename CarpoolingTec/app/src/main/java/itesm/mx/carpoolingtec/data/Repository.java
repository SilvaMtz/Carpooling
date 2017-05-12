package itesm.mx.carpoolingtec.data;

import com.google.firebase.database.DatabaseReference;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import itesm.mx.carpoolingtec.model.firebase.Contact;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;

public interface Repository {

    DatabaseReference getDatabase();

    Completable saveRide(User user, Ride ride);

    void removeRide(Ride ride, String key);

    Single<User> getUser(String id);

    void saveMyId(String id);

    String getMyId();

    void saveMyName(String name);

    String getMyName();

    void setMyLatitude(Float latitude);

    Float getMyLatitude();

    void setMyLongitude(Float longitude);

    Float getMyLongitude();

    void addContact(User contact);

    void addRequest(User receptor);

    void removeRequest(String requestKey);

    void saveUserPrefs(User user);

    User getUserPrefs();

    void removeRide(String type, String key);
}
