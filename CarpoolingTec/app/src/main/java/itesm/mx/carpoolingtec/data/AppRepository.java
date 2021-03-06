package itesm.mx.carpoolingtec.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

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
import itesm.mx.carpoolingtec.model.firebase.Request;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.rides.RidesFragment;
import itesm.mx.carpoolingtec.util.NotRegisteredException;


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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance().getReference();
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public DatabaseReference getDatabase() {
        return database;
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
    public void removeRide(final Ride ride, String key) {
        String rideTypeKey;
        if (ride.getRide_type().equals("FROM_TEC")) {
            rideTypeKey = "rides_from_tec";
        } else {
            rideTypeKey = "rides_to_tec";
        }

        database.child(rideTypeKey).child(getMyId()).child("rides").child(key)
                .removeValue();
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
                            user.setId(dataSnapshot.getKey());
                            e.onSuccess(user);
                        } else {
                            e.onError(new NotRegisteredException());
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
        return sharedPreferences.getString("Matricula", null);
    }

    @Override
    public void saveMyName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Nombre", name);
        editor.commit();
    }

    @Override
    public String getMyName() {
        return sharedPreferences.getString("Nombre", null);
    }

    @Override
    public void setMyLatitude(Float latitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("Latitude", latitude);
        editor.apply();
    }

    @Override
    public Float getMyLatitude() {
        return sharedPreferences.getFloat("Latitude", 0f);
    }

    @Override
    public void setMyLongitude(Float longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("Longitude", longitude);
        editor.apply();
    }

    @Override
    public Float getMyLongitude() {
        return sharedPreferences.getFloat("Longitude", 0f);
    }

    @Override
    public void addContact(final User contact) {
        // Add contact in this users contacts.
        database.child("users").child(contact.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setId(dataSnapshot.getKey());

                String key = database.child("users").child(getMyId()).child("contacts").push().getKey();
                Map<String, Object> values = user.toMapForContact();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + getMyId() + "/contacts/" + key, values);

                database.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // TODO: do something
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: do something
            }
        });

        // Add this user in contact's contacts
        database.child("users").child(getMyId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setId(dataSnapshot.getKey());

                String key = database.child("users").child(contact.getId()).child("contacts").push().getKey();
                Map<String, Object> values = user.toMapForContact();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + contact.getId() + "/contacts/" + key, values);

                database.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // TODO: do something
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: do something
            }
        });
    }

    @Override
    public void addRequest(final User receptor) {
        // Me
        database.child("users").child(getMyId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User me = dataSnapshot.getValue(User.class);
                me.setId(dataSnapshot.getKey());

                Request request = new Request(me, false, false);
                String key = database.child("solicitudes").child(receptor.getId()).push().getKey();
                Map<String, Object> values = request.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/solicitudes/" + receptor.getId() + "/" + key, values);

                database.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // TODO: do something
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: do something
            }
        });
    }

    @Override
    public void removeRequest(String requestKey) {
        database.child("solicitudes").child(getMyId()).child(requestKey).removeValue();
    }

    @Override
    public void saveUserPrefs(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserPrefs", new Gson().toJson(user));
        editor.apply();
    }

    @Override
    public User getUserPrefs() {
        String  userJson = sharedPreferences.getString("UserPrefs", null);
        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        } else {
            return null;
        }
    }

    @Override
    public void removeRide(String type, String key) {
        String rideTypeKey;
        if (type.equals("FROM_TEC")) {
            rideTypeKey = "rides_from_tec";
        } else {
            rideTypeKey = "rides_to_tec";
        }

        database.child(rideTypeKey).child(getMyId()).child("rides").child(key).removeValue();
    }


}
