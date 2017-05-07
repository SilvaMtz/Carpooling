package itesm.mx.carpoolingtec.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.LoginService;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.main.MainActivity;
import itesm.mx.carpoolingtec.userinfo.PedirInfoActivity;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.edit_student_id) EditText etStudentId;
    @BindView(R.id.edit_password) EditText etPassword;
    @BindView(R.id.button_login) Button btnLogin;

    private MaterialDialog progressDialog;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = new MaterialDialog.Builder(this)
                .content("Iniciando sesión")
                .progress(true, 0)
                .build();

        AppRepository repository = AppRepository.getInstance(getSharedPreferences(
                MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE));

        presenter = new LoginPresenter(new LoginService(), repository,
                SchedulerProvider.getInstance(), new CompositeDisposable());
        presenter.attachView(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(etStudentId.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.stop();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void startPedirInfoActivity(String userId, String name) {
        Intent intent = new Intent(this, PedirInfoActivity.class);
        intent.putExtra("ïd",0);
        intent.putExtra(PedirInfoActivity.MATRICULA, userId);
        intent.putExtra(PedirInfoActivity.NOMBRE, name);
        startActivity(intent);
    }

    @Override
    public void showLoginErrorMessage() {
        Toast.makeText(this, "No se pudo iniciar sesión. Intenta de nuevo más tarde", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInvalidCredentialsMessage() {
        Toast.makeText(this, "Matricula o contraseña incorrecta", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMissingFieldsMessage() {
        Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
