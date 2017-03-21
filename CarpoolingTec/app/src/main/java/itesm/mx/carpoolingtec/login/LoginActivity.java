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
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("Matricula", etStudentId.getText().toString());
            startActivity(intent);

        }else{
            Toast.makeText(v.getContext(),getResources().getString(R.string.ErrorMessage),Toast.LENGTH_LONG).show();
        }
    }
}
