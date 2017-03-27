package itesm.mx.carpoolingtec.contacts;

import itesm.mx.carpoolingtec.model.User;
import itesm.mx.carpoolingtec.model.firebase.Contact;

public interface ContactItemListener {
    void onContactClick(Contact contact);
}
