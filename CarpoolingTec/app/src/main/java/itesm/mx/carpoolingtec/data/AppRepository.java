package itesm.mx.carpoolingtec.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import itesm.mx.carpoolingtec.model.firebase.Contact;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.rides.RidesFragment;


public class AppRepository implements Repository {

    private static final String TYPE_FROM_TEC = "FROM_TEC";
    private static final String TYPE_TO_TEC = "TO_TEC";

    private static AppRepository INSTANCE = null;

    private DatabaseReference database;
    private SharedPreferences sharedPreferences;

    public static AppRepository getInstance(SharedPreferences sharedPreferences) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(sharedPreferences);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppRepository(SharedPreferences sharedPreferences) {
        database = FirebaseDatabase.getInstance().getReference();
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Observable<Contact> getContacts(final String userId) {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                Log.d("AppRepository", "subscribed to getContacts()");

                database.child("users").child(userId).child("contacts").addChildEventListener(
                        new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("AppRepository", "onChildAdded");

                        Contact contact = dataSnapshot.getValue(Contact.class);
                        Log.d("AppRepository", contact.getName());

                        e.onNext(contact);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("AppRepository", "onChildChanged");
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("AppRepository", "onChildRemoved");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("AppRepository", "onChildedMover");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("AppRepository", "onCancelled");
                    }
                });
            }
        });

        return observable;
    }

    @Override
    public Observable<UserRide> getUserRides(int rideType) {
        final String childKey;
        if (rideType == RidesFragment.TO_TEC) {
            childKey = "rides_to_tec";
        } else {
            childKey = "rides_from_tec";
        }
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                database.child(childKey).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserRide userRide = dataSnapshot.getValue(UserRide.class);
                        e.onNext(userRide);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return observable;
    }

    @Override
    public Completable saveRide(final User user, final Ride ride) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(final CompletableEmitter e) throws Exception {
                String firebasePath;
                if (ride.getRide_type().equals(TYPE_FROM_TEC)) {
                    firebasePath = "rides_from_tec";
                } else {
                    firebasePath = "rides_to_tec";
                }

                // Add user details under ride type.
                database.child(firebasePath).child(user.getId()).child("user")
                        .setValue(user.toMap());

                String key = database.child(firebasePath).child(user.getId())
                        .child("rides").push().getKey();

                Map<String, Object> rideValues = ride.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/" + firebasePath + "/" + user.getId() + "/rides/" + key,
                        rideValues);

                database.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            e.onError(databaseError.toException());
                        } else {
                            e.onComplete();
                        }
                    }
                });
            }
        });
    }

    @Override
    public Single<User> getUser(final String id) {
        return Single.create(new SingleOnSubscribe<User>() {
            @Override
            public void subscribe(final SingleEmitter<User> e) throws Exception {
                if (id == null) {
                    e.onError(new Exception());
                    return;
                }

                database.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            e.onSuccess(user);
                        } else {
                            e.onError(new Exception());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(new Exception());
                    }
                });


            }
        });


    }

    @Override
    public void saveMyId(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Matricula", id);
        editor.commit();

    }

    @Override
    public String getMyId() {
        return sharedPreferences.getString("Matricula" ,null);

    }


}
