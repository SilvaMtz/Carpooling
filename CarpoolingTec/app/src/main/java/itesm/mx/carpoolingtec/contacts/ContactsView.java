package itesm.mx.carpoolingtec.contacts;

import java.util.List;

import itesm.mx.carpoolingtec.model.User;
import itesm.mx.carpoolingtec.model.firebase.Contact;

public interface ContactsView {

    void setLoadingIndicator(boolean active);

    void showErrorMessageToast();

    void openContactDetails(Contact contact);
}
