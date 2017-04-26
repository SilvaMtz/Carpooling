package itesm.mx.carpoolingtec.login;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import itesm.mx.carpoolingtec.base.BasePresenter;
import itesm.mx.carpoolingtec.data.LoginService;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.Alumno;
import itesm.mx.carpoolingtec.util.UnauthorizedException;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;

public class LoginPresenter extends BasePresenter<LoginView> {

    private static final String TAG = "LoginPresenter";

    private LoginService loginService;
    private Repository repository;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    public LoginPresenter(LoginService loginService, Repository repository,
                          BaseSchedulerProvider schedulerProvider,
                          CompositeDisposable compositeDisposable) {
        this.loginService = loginService;
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    public void login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            view.showMissingFieldsMessage();
            return;
        }

        username = username.toLowerCase();

        view.setLoadingIndicator(true);
        compositeDisposable.add(loginService.login(username, password)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getLocalizedMessage());
                    }
                })
                .subscribeWith(new DisposableSingleObserver<Alumno>() {
                    @Override
                    public void onSuccess(Alumno alumno) {
                        repository.saveMyId(alumno.getMatricula());
                        view.showToast("Hola " + alumno.getNombre());

                        boolean registered = true;
                        if (registered) { // TODO: check if user exists in firebase
                            view.startMainActivity();
                        } else {
                            view.startPedirInfoActivity(alumno.getMatricula(),
                                    alumno.getMatricula() + alumno.getApellidoPaterno());
                        }

                        view.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnauthorizedException) {
                            view.showInvalidCredentialsMessage();
                        } else {
                            view.showLoginErrorMessage();
                        }
                        view.setLoadingIndicator(false);
                    }
                })
        );
    }

    public void stop() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
