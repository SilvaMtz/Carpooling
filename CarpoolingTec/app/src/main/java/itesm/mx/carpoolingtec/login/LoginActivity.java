package itesm.mx.carpoolingtec.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.edit_student_id) EditText etStudentId;
    @BindView(R.id.edit_password) EditText etPassword;
    @BindView(R.id.button_login) Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!etStudentId.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()){
            AppRepository app = AppRepository.getInstance(getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE));
            app.saveMyId(etStudentId.getText().toString());
            if(false){ //TODO buscar en firebase que exista cuenta
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, PedirInfo.class);
                intent.putExtra("Mat",etStudentId.getText().toString());
                intent.putExtra("Nom","Andres" + " "+ "Sosa"); //TODO conseguir el nombre y el apellido del parse del xml
                startActivity(intent);
            }

        }else{
            Toast.makeText(v.getContext(),getResources().getString(R.string.ErrorMessage),Toast.LENGTH_LONG).show();
        }
    }
}
