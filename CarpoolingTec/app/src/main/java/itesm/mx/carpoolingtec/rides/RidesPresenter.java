package itesm.mx.carpoolingtec.rides;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import itesm.mx.carpoolingtec.data.Repository;
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
