package itesm.mx.carpoolingtec.post;

import io.reactivex.disposables.CompositeDisposable;
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

    public void saveRide(User user, Ride ride) {
        disposables.add(repository.saveRide(user, ride)
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
