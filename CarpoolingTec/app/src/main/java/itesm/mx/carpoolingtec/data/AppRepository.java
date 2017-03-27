package itesm.mx.carpoolingtec.data;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import itesm.mx.carpoolingtec.model.firebase.Contact;


public class AppRepository implements Repository {

    private static AppRepository INSTANCE = null;

    private DatabaseReference database;

    public static AppRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppRepository() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public Observable<Contact> getContacts(final String userId) {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                Log.d("AppRepository", "subscribed");

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
}
