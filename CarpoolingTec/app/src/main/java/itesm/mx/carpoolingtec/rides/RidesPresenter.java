package itesm.mx.carpoolingtec.rides;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class RidesPresenter {

    private RidesView view;
    private Repository repository;
    private BaseSchedulerProvider schedulerProvider;
    private int rideType;
    private CompositeDisposable disposables;

    public RidesPresenter(RidesView view, Repository repository,
                          BaseSchedulerProvider schedulerProvider, int rideType) {
        this.view = view;
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
        this.rideType = rideType;
    }

    public void start() {
        disposables = new CompositeDisposable();
    }

    public void onRideClick(UserRide userRide) {
        // User available on UserRide only has an id, name and photo, so a request for a complete
        // User object must me made.
        disposables.add(repository.getUser(userRide.getUser().getId())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        view.openUserRideDetails(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RidesPresenter", "error: " + e.toString());
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
}
