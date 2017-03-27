package itesm.mx.carpoolingtec.data;

import io.reactivex.Observable;
import itesm.mx.carpoolingtec.model.firebase.Contact;

public interface Repository {

    Observable<Contact> getContacts(String userId);
}
