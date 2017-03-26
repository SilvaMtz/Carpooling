package itesm.mx.carpoolingtec.contacts;

import android.util.Log;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import itesm.mx.carpoolingtec.data.CarpoolingService;
import itesm.mx.carpoolingtec.model.User;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class ContactsPresenter {

    private ContactsView view;
    private CarpoolingService service;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable disposables;

    public ContactsPresenter(ContactsView view, CarpoolingService service, BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.service = service;
        this.schedulerProvider = schedulerProvider;
    }

    public void start() {
        disposables = new CompositeDisposable();
    }

    public void loadContacts() {
        Log.d("Contacts", "Loading...");
        view.setLoadingIndicator(true);
        view.hideContacts();
        disposables.clear();
        disposables.add(service.getUsers()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<List<User>>() {
                    @Override
                    public void onSuccess(List<User> users) {
                        Log.d("Contacts", "got users " + users.size());
                        Log.d("Contacts", users.get(0).getName());
                        if (isViewAttached()) {
                            if (!users.isEmpty()) {
                                view.showContacts(users);
                                view.setLoadingIndicator(false);
                            } else {
                                view.setLoadingIndicator(false);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Contacts", e.getLocalizedMessage());
                        if (isViewAttached()) {
                            view.setLoadingIndicator(false);
                            view.showErrorMessageToast();
                        }
                    }
                })
        );
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
