package itesm.mx.carpoolingtec.userinfo;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.main.MainActivity;
import itesm.mx.carpoolingtec.model.firebase.User;

public class PedirInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MATRICULA = "matricula";
    public static final String NOMBRE = "nombre";

    @BindView(R.id.FumarCheckPedirInfo) CheckBox cFumar;
    @BindView(R.id.HombreCheckPedirInfo) CheckBox cHombre;
    @BindView(R.id.MujerCheckPedirInfo) CheckBox cMujer;
    @BindView(R.id.CobrarCheckPedirInfo) CheckBox cPrecio;
    @BindView(R.id.MatPedirInfo) TextView tvMat;
    @BindView(R.id.NomPedirInfo) TextView tvNom;
    @BindView(R.id.NotaTextoPedirInfo) EditText etNota;
    @BindView(R.id.BottonPedirInfo) Button btGuardar;
    @BindView(R.id.radioGender) RadioGroup radioGroup;
    @BindView(R.id.radioFemale) RadioButton radioFemale;
    @BindView(R.id.radioMale) RadioButton radioMale;
    @BindView(R.id.edit_phone) EditText editPhone;

    private Repository repository;

    private String matricula;
    private String nombre;

    private static final String ORIGEN = "Tu Casa";
    private static final String TAG = "PedirInfoActivity";

    private boolean fumar = false;
    private boolean hombres = true;
    private boolean mujeres = true;
    private boolean precio = false;
    int gender = 0; // Hombre
    private int id; // 0 Login activity, 1 Perfil activity
    private User userPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_info);
        ButterKnife.bind(this);

        repository = AppRepository.getInstance(getSharedPreferences(
                MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Tus datos");

        cFumar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fumar = isChecked;
            }
        });

        cHombre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hombres = isChecked;
            }
        });

        cMujer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mujeres = isChecked;
            }
        });

        cPrecio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                precio = isChecked;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radioMale) {
                    gender = 0;
                } else {
                    gender = 1;
                }
            }
        });

        btGuardar.setOnClickListener(this);

        // 0 first time setup
        // 1 editing
        id = getIntent().getIntExtra("id", -1);

        if (id == 0) {
            matricula = getIntent().getStringExtra(MATRICULA);
            nombre = getIntent().getStringExtra(NOMBRE);
        } else {
            matricula = repository.getMyId();
            nombre = repository.getMyName();
        }

        tvMat.setText(tvMat.getText().toString() + " " + matricula);
        tvNom.setText(tvNom.getText().toString() + " " + nombre);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int passengerGender;
        String note = etNota.getText().toString();

        if (editPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Introduce tu número de teléfono", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hombres && !mujeres) {
            Toast.makeText(this, "Elige el tipo de pasajero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hombres && mujeres) {
            passengerGender = 2;
        } else if (hombres) {
            passengerGender = 0;
        } else {
            passengerGender = 1;
        }

        repository.getDatabase().child("users").child(matricula.toUpperCase()).child("notes").setValue(note);
        repository.getDatabase().child("users").child(matricula.toUpperCase()).child("gender").setValue(gender);
        repository.getDatabase().child("users").child(matricula.toUpperCase()).child("passenger_gender").setValue(passengerGender);
        repository.getDatabase().child("users").child(matricula.toUpperCase()).child("phone").setValue(editPhone.getText().toString());
        repository.getDatabase().child("users").child(matricula.toUpperCase()).child("price").setValue(precio);
        repository.getDatabase().child("users").child(matricula.toUpperCase()).child("smoking").setValue(fumar);
    }
}
