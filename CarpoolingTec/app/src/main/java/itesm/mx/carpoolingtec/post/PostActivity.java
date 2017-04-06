package itesm.mx.carpoolingtec.post;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.util.schedulers.BaseSchedulerProvider;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

public class PostActivity extends AppCompatActivity implements PostView, PlaceSelectionListener,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener,View.OnClickListener {

    private static final String TAG = "PostActivity";
    private static final String ORIGEN = "Origen";
    private static final String DESTINO = "Destino";


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tipoSpinner) Spinner spinner;
    @BindView(R.id.Post_monday) Button BM;
    @BindView(R.id.Post_Tuesday) Button BT;
    @BindView(R.id.Post_Wednesday) Button BW;
    @BindView(R.id.Post_thursday) Button BTT;
    @BindView(R.id.Post_friday) Button BF;
    @BindView(R.id.Post_saturday) Button BS;
    @BindView(R.id.Post_sunday) Button BSU;

    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleApiClient googleApiClient;
    private PostPresenter presenter;
    private boolean dir = false;
    private double lat = -1;
    private double longi = -1;
    private int pos = -1;
    private String sOrigen = "FROM_TEC";;
    private String[] dias = new String[] {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }

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

        spinner.setOnItemSelectedListener(this);

        // User user = new User("A00513176", "Juan Perez", "http://skateparkoftampa.com/spot/headshots/2585.jpg");
        // Ride ride = new Ride("FROM_TEC", "11:30 am", "Miercoles", 12345678.0, 87654321.0);

        SharedPreferences sharedPreferences = getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);

        presenter = new PostPresenter(this, AppRepository.getInstance(sharedPreferences),
                SchedulerProvider.getInstance());
        presenter.start();
        BM.setOnClickListener(this);
        BT.setOnClickListener(this);
        BTT.setOnClickListener(this);
        BW.setOnClickListener(this);
        BF.setOnClickListener(this);
        BS.setOnClickListener(this);
        BSU.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:
                //Dummy
                if(lat<0 || !dir){
                    //Toast.makeText(getApplicationContext(),"Datos Incompletos " + Integer.toString(pos) +" " + Double.toString(longi) +" " + Double.toString(lat),Toast.LENGTH_LONG).show();
                    return true;
                }
                Ride ride = new Ride(sOrigen, "11:30 am", dias[pos], longi, lat);
                User user = new User("A00513176", "Juan Perez", "http://skateparkoftampa.com/spot/headshots/2585.jpg");
                presenter.saveRide(user,ride);
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, parent.getItemAtPosition(position).toString() + " selected");
        if (position == 0) {
            autocompleteFragment.setHint(ORIGEN);
            sOrigen = "FROM_TEC";
        } else {
            autocompleteFragment.setHint(DESTINO);
            sOrigen = "TO_TEC";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clean(int pos){
        switch (pos){
            case 0:
                BM.setTextColor(Color.parseColor("#E6000000"));
                break;
            case 1:
                BT.setTextColor(Color.parseColor("#E6000000"));
                break;
            case 2:
                BW.setTextColor(Color.parseColor("#E6000000"));
                break;
            case 3:
                BTT.setTextColor(Color.parseColor("#E6000000"));
                break;
            case 4:
                BF.setTextColor(Color.parseColor("#E6000000"));
                break;
            case 5:
                BS.setTextColor(Color.parseColor("#E6000000"));
                break;
            case 6:
                BSU.setTextColor(Color.parseColor("#E6000000"));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Post_monday:
                if(pos<0){
                    pos = 0;
                    BM.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 0;
                    BM.setTextColor(Color.GREEN);
                }
                break;
            case R.id.Post_Tuesday:
                if(pos<0){
                    pos = 1;
                    BT.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 1;
                    BT.setTextColor(Color.GREEN);
                }
                break;
            case R.id.Post_Wednesday:
                if(pos<0){
                    pos = 2;
                    BW.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 2;
                    BW.setTextColor(Color.GREEN);
                }
                break;
            case R.id.Post_thursday:
                if(pos<0){
                    pos = 3;
                    BTT.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 3;
                    BTT.setTextColor(Color.GREEN);
                }
                break;
            case R.id.Post_friday:
                if(pos<0){
                    pos = 4;
                    BF.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 4;
                    BF.setTextColor(Color.GREEN);
                }
                break;
            case R.id.Post_saturday:
                if(pos<0){
                    pos = 5;
                    BS.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 5;
                    BS.setTextColor(Color.GREEN);
                }
                break;
            case R.id.Post_sunday:
                if(pos<0){
                    pos = 6;
                    BSU.setTextColor(Color.GREEN);
                }else{
                    clean(pos);
                    pos = 6;
                    BSU.setTextColor(Color.GREEN);
                }
                break;
        }
    }
}
