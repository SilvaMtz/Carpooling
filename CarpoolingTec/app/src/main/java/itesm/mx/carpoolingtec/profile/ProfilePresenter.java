package itesm.mx.carpoolingtec.profile;

import android.content.Intent;
import android.view.View;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.base.BasePresenter;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.post.PostActivity;
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

    public void loadUserData() {
        disposables.add(repository.getUser(repository.getMyId())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        view.showUserData(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })
        );
    }

    public void onEditRide(Ride ride) {
        /*view.openEditRideOptions();
        Intent intent = new Intent(,PostActivity.class);
        //Intent intent = new Intent(getActivity().findViewById(R.id.viewid), PostActivity.class);
        intent.putExtra("id",1); // 1 representa Perfil
        intent.putExtra("ride", (Serializable) ride);
        startActivity(intent);*/
        view.openPostActivity(ride);
    }

    public void onDeleteRide(Ride ride, String key) {
        repository.removeRide(ride, key);
        view.showRemovedRideToast();
    }

    public void stop() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
