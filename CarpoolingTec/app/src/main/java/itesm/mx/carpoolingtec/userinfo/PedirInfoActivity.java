package itesm.mx.carpoolingtec.userinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.main.MainActivity;

public class PedirInfoActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener,
        View.OnClickListener {

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

    private String Matricula;
    private String nombre;
    private boolean dir = false;
    private double lat = -1;
    private double longi = -1;
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteFragment autocompleteFragment;

    private static final String ORIGEN = "Tu Casa";
    private static final String TAG = "PedirInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_info);
        ButterKnife.bind(this);
        Matricula = getIntent().getStringExtra(MATRICULA);
        nombre = getIntent().getStringExtra(NOMBRE);

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(25.596967, 100.488252),
                new LatLng(25.816600, 100.098237)
        ));
        autocompleteFragment.setOnPlaceSelectedListener(this);
        // Set default hint to ORIGEN
        autocompleteFragment.setHint(ORIGEN);
        tvMat.setText(tvMat.getText().toString()+" "+Matricula);
        tvNom.setText(tvNom.getText().toString()+" "+nombre);
        btGuardar.setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
        lat = place.getLatLng().latitude;
        longi = place.getLatLng().longitude;
        dir = true;
        Log.i(TAG, "Place: " + place.getName());
    }

    @Override
    public void onError(Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
    }

    @Override
    public void onClick(View v) {
        if(dir){
            String note = etNota.getText().toString();
            //Todo guadar datos en firbase
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(v.getContext(),getResources().getString(R.string.ErrorMessage2),Toast.LENGTH_LONG).show();
        }
    }
}
