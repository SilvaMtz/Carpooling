package itesm.mx.carpoolingtec.contacts;

import java.util.List;

import itesm.mx.carpoolingtec.model.User;

public interface ContactsView {

    void setLoadingIndicator(boolean active);

    void showContacts(List<User> users);

    void hideContacts();

    void showErrorMessageToast();
}
