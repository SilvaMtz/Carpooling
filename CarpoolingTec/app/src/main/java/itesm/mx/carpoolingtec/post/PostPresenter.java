package itesm.mx.carpoolingtec.post;

import com.google.firebase.database.DatabaseReference;

import io.reactivex.CompletableSource;
import io.reactivex.MaybeSource;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class PostPresenter {

    private PostView view;
    private Repository repository;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable disposables;

    public PostPresenter(PostView view, Repository repository, BaseSchedulerProvider schedulerProvider) {
        this.view = view;
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public void start() {
        disposables = new CompositeDisposable();
    }

    public void saveRide(final Ride ride) {
        disposables.add(repository.getUser(repository.getMyId())
                .flatMapCompletable(new Function<User, CompletableSource>() {
                    @Override
                    public CompletableSource apply(User user) throws Exception {
                        return repository.saveRide(user, ride);
                    }
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    public void updateRide(Ride ride, String key, String oldRideType) {
        String path;
        if (ride.getRide_type().equals("TO_TEC")) {
            path = "rides_to_tec";
        } else {
            path = "rides_from_tec";
        }

        if (ride.getRide_type().equals(oldRideType)) {
            DatabaseReference dref = repository.getDatabase().child(path).child(repository.getMyId()).child("rides").child(key);
            dref.child("latitude").setValue(ride.getLatitude());
            dref.child("longitude").setValue(ride.getLongitude());
            dref.child("name").setValue(ride.getDirName());
            dref.child("ride_type").setValue(ride.getRide_type());
            dref.child("time").setValue(ride.getTime());
            dref.child("weekday").setValue(ride.getWeekday());
        } else {
            repository.removeRide(oldRideType, key);
            saveRide(ride);
        }
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
