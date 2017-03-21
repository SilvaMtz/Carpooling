package itesm.mx.carpoolingtec.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.main.MainActivity;

public class Login extends AppCompatActivity implements View.OnClickListener{

    //@BindView(R.id.email) EditText etemail;
    //@BindView(R.id.password) EditText etpassword;
    //@BindView(R.id.email_sign_in_button) Button emailButton;
    private EditText etemail;
    private EditText etpassword;
    private Button emailButton;
    //EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailButton = (Button) findViewById(R.id.email_sign_in_button);
        etemail = (EditText) findViewById(R.id.email);
        etpassword = (EditText) findViewById(R.id.password);
        emailButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!etemail.getText().toString().isEmpty() && !etpassword.getText().toString().isEmpty()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("Matricula",etemail.getText().toString());
            startActivity(intent);

        }else{
            Toast.makeText(v.getContext(),getResources().getString(R.string.ErrorMessage),Toast.LENGTH_LONG).show();
        }
    }
}
