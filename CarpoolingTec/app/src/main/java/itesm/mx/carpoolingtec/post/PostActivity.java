package itesm.mx.carpoolingtec.post;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

public class PostActivity extends AppCompatActivity implements PostView, PlaceSelectionListener,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener, View.OnClickListener, OnTimeSetListener {

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
    @BindView(R.id.text_clock) TextView tvClock;

    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleApiClient googleApiClient;
    private PostPresenter presenter;
    private boolean dir = false;
    private String dirName;
    private double lat = -1;
    private double longi = -1;
    private int pos;
    private String sOrigen = "TO_TEC";
    private String[] dias = new String[] {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
    private int id; //Represante de donde viene la actividad
    private Ride ride;
    private String key = null;
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
        pos = -1;
        clean(0);

        id = getIntent().getIntExtra("id",0);
        if(id == 1){
            ride = (Ride) getIntent().getSerializableExtra("ride");
            key = getIntent().getStringExtra("key");

            sOrigen = ride.getRide_type();
            longi = ride.getLongitude();
            lat = ride.getLatitude();
            dirName = ride.getDirName();
            dir = true;

            pos = GivePosAndMakeGreen(ride.getWeekday());
            tvClock.setText(ride.getTime());
            if(sOrigen.equals("TO_TEC")){
                spinner.setSelection(0);
                autocompleteFragment.setHint(ORIGEN);
            }else{
                spinner.setSelection(1);
                autocompleteFragment.setHint(DESTINO);
            }
            autocompleteFragment.setText(dirName);
        }
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
                if(lat<0 || !dir || pos == -1){
                    //Toast.makeText(getApplicationContext(),"Datos Incompletos " + Integer.toString(pos) +" " + Double.toString(longi) +" " + Double.toString(lat),Toast.LENGTH_LONG).show();
                    return true;
                }
                if(id==0){ // Si es post ride
                    Ride ride = new Ride(sOrigen, tvClock.getText().toString(), dias[pos], longi, lat,dirName);
                    presenter.saveRide(ride);
                    finish();
                } else{ // si es update ride
                    Ride ride = new Ride(sOrigen, tvClock.getText().toString(), dias[pos], longi, lat,dirName);
                    presenter.updateRide(ride, key, this.ride.getRide_type());
                    finish();
                }
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
        lat = place.getLatLng().latitude;
        longi = place.getLatLng().longitude;
        dir = true;
        dirName = place.getName().toString();
        Log.i(TAG, "Place: " + place.getName());
    }

    @Override
    public void onError(Status status) {
        Log.i(TAG, "An error occurred: " + status);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            autocompleteFragment.setHint(ORIGEN);
            sOrigen = "TO_TEC";
        } else {
            autocompleteFragment.setHint(DESTINO);
            sOrigen = "FROM_TEC";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clean(int pos){
        switch (pos){
            case 0:
                BM.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
            case 1:
                BT.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
            case 2:
                BW.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
            case 3:
                BTT.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
            case 4:
                BF.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
            case 5:
                BS.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
            case 6:
                BSU.setTextColor(getResources().getColor(R.color.textPrimary));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Post_monday:
                if(pos<0){
                    pos = 0;
                    BM.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 0;
                    BM.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case R.id.Post_Tuesday:
                if(pos<0){
                    pos = 1;
                    BT.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 1;
                    BT.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case R.id.Post_Wednesday:
                if(pos<0){
                    pos = 2;
                    BW.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 2;
                    BW.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case R.id.Post_thursday:
                if(pos<0){
                    pos = 3;
                    BTT.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 3;
                    BTT.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case R.id.Post_friday:
                if(pos<0){
                    pos = 4;
                    BF.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 4;
                    BF.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case R.id.Post_saturday:
                if(pos<0){
                    pos = 5;
                    BS.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 5;
                    BS.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case R.id.Post_sunday:
                if(pos<0){
                    pos = 6;
                    BSU.setTextColor(getResources().getColor(R.color.colorAccent));
                }else{
                    clean(pos);
                    pos = 6;
                    BSU.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
        }
    }

    private int GivePosAndMakeGreen(String weekday) {
        switch (weekday){
            case "Lunes":
                    BM.setTextColor(getResources().getColor(R.color.colorAccent));
                    return  0;
            case "Martes":
                    BT.setTextColor(getResources().getColor(R.color.colorAccent));
                    return 1;
            case "Miercoles":
                    BW.setTextColor(getResources().getColor(R.color.colorAccent));
                    return 2;
            case "Jueves":
                    BTT.setTextColor(getResources().getColor(R.color.colorAccent));
                    return 3;
            case "Viernes":
                    BF.setTextColor(getResources().getColor(R.color.colorAccent));
                    return  4;
            case "Sabado":
                    BS.setTextColor(getResources().getColor(R.color.colorAccent));
                    return  5;
            case "Domingo":
                    BSU.setTextColor(getResources().getColor(R.color.colorAccent));
                    return  6;
        }
        return -1;
    }

    @OnClick(R.id.text_clock)
    public void onClockClick() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        if (tvClock != null) {
            String hours = hourOfDay > 9 ? String.valueOf(hourOfDay) : "0" + String.valueOf(hourOfDay);
            String minutes = minute > 9 ? String.valueOf(minute) : "0" + String.valueOf(minute);
            tvClock.setText(hours + ":" + minutes);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private OnTimeSetListener listener;

        public void setListener(OnTimeSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (listener != null) {
                listener.onTimeSet(hourOfDay, minute);
            }
        }
    }
}
