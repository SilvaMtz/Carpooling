package itesm.mx.carpoolingtec.contacts;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Contact;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class ContactsPresenter {

    private ContactsView view;
    private Repository repository;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable disposables;

    public ContactsPresenter(ContactsView view, Repository repository, BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public void start() {
        disposables = new CompositeDisposable();
    }

    public void onContactClick(Contact contact) {
        view.openContactDetails(contact);
    }

    public void stop() {
        view = null;
        if (disposables != null) {
            disposables.clear();
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
