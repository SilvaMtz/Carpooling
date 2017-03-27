package itesm.mx.carpoolingtec.rides;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import itesm.mx.carpoolingtec.data.CarpoolingService;
import itesm.mx.carpoolingtec.model.Ride;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class RidesPresenter {

    private RidesView view;
    private CarpoolingService service;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable disposables;

    public RidesPresenter(RidesView view, CarpoolingService service, BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.service = service;
        this.schedulerProvider = schedulerProvider;
    }

    public void start() {
        disposables = new CompositeDisposable();
    }

    public void loadRides() {
        disposables.add(service.getRides()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<List<Ride>>() {
                    @Override
                    public void onSuccess(List<Ride> rides) {
                        if (rides.isEmpty()) {
                            if (isViewAttached()) {
                                view.showNoRidesAvailableToast();
                            }
                        } else {
                            if (isViewAttached()) {
                                view.showRides(rides);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

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
