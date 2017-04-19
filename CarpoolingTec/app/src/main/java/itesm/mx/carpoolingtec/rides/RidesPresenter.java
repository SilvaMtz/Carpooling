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

    public void loadRides() {
        disposables.add(repository.getUserRides(rideType)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableObserver<UserRide>() {
                    @Override
                    public void onNext(UserRide userRide) {
                        if (isViewAttached()) {
                            view.addUserRide(userRide);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            view.showErrorLoadingRidesToast();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    public void onRideClick(UserRide userRide) {
        final String MONDAY = "Lunes";
        final String TUESDAY = "Martes";
        final String WEDNESDAY = "Miercoles";
        final String THURSDAY = "Jueves";
        final String FRIDAY = "Viernes";
        final String SATURDAY = "Sabado";
        final String SUNDAY = "Domingo";

        Map<String, Ride> rideMap = userRide.getRides();

        final List<String> ridesMonday = new ArrayList<>();
        final List<String> ridesTuesday = new ArrayList<>();
        final List<String> ridesWednesday = new ArrayList<>();
        final List<String> ridesThursday = new ArrayList<>();
        final List<String> ridesFriday = new ArrayList<>();
        final List<String> ridesSaturday = new ArrayList<>();
        final List<String> ridesSunday = new ArrayList<>();

        for (Map.Entry<String, Ride> entry : rideMap.entrySet()) {
            switch (entry.getValue().getWeekday()) {
                case MONDAY:
                    ridesMonday.add(entry.getValue().getTime());
                    break;
                case TUESDAY:
                    ridesTuesday.add(entry.getValue().getTime());
                    break;
                case WEDNESDAY:
                    ridesWednesday.add(entry.getValue().getTime());
                    break;
                case THURSDAY:
                    ridesThursday.add(entry.getValue().getTime());
                    break;
                case FRIDAY:
                    ridesFriday.add(entry.getValue().getTime());
                    break;
                case SATURDAY:
                    ridesSaturday.add(entry.getValue().getTime());
                    break;
                case SUNDAY:
                    ridesSunday.add(entry.getValue().getTime());
                    break;
            }
        }

        //TODO: sort lists by time of day.

        // User available on UserRide only has an id, name and photo, so a request for a complete
        // User object must me made.
        disposables.add(repository.getUser(userRide.getUser().getId())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        view.openUserRideDetails(user, ridesMonday, ridesTuesday, ridesWednesday, ridesThursday,
                                ridesFriday, ridesSaturday, ridesSunday);
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

    private boolean isViewAttached() {
        return view != null;
    }
}
