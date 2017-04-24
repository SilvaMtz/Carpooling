package itesm.mx.carpoolingtec.profile;

import io.reactivex.disposables.CompositeDisposable;
import itesm.mx.carpoolingtec.base.BasePresenter;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class ProfilePresenter extends BasePresenter<ProfileView> {

    private static final String TAG = "ProfilePresenter";

    private Repository repository;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable disposables;

    public ProfilePresenter(Repository repository, BaseSchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;

        disposables = new CompositeDisposable();
    }

    public void onEditRide(Ride ride) {

    }

    public void onDeleteRide(Ride ride, String key) {
        repository.removeRide(ride, "A00513173", key);
        view.showRemovedRideToast();
    }

    public void stop() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
