package itesm.mx.carpoolingtec.login;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import itesm.mx.carpoolingtec.base.BasePresenter;
import itesm.mx.carpoolingtec.data.LoginService;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.Alumno;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.util.NotRegisteredException;
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
                    public void onSuccess(final Alumno alumno) {
                        repository.getDatabase().child("users").child(alumno.getMatricula().toUpperCase())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user != null) {
                                            user.setId(dataSnapshot.getKey());
                                            repository.saveMyId(user.getId());
                                            repository.saveMyName(user.getName());

                                            Log.d(TAG, "starting MainActivity");
                                            view.startMainActivity();
                                        } else {
                                            Log.d(TAG, "starting PedirInfoActivity");
                                            view.startPedirInfoActivity(alumno.getMatricula().toUpperCase(),
                                                    alumno.getNombre() + " " + alumno.getApellidoPaterno());
                                        }

                                        view.setLoadingIndicator(false);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        view.setLoadingIndicator(false);
                                        view.showLoginErrorMessage();
                                    }
                                });

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
