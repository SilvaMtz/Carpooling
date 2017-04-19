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

    public void loadContacts() {
        view.clearContacts();
        disposables.add(repository.getContacts("A00513173")
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableObserver<Contact>() {
                    @Override
                    public void onNext(Contact contact) {
                        if (isViewAttached()) {
                            view.addContact(contact);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            view.showErrorMessageToast();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
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
