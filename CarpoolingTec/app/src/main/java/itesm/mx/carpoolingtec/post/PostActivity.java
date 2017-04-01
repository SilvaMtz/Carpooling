package itesm.mx.carpoolingtec.post;

import android.content.SharedPreferences;
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
import android.widget.Spinner;

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
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "PostActivity";
    private static final String ORIGEN = "Origen";
    private static final String DESTINO = "Destino";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tipoSpinner) Spinner spinner;

    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleApiClient googleApiClient;
    private PostPresenter presenter;

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

        User user = new User("A00513176", "Juan Perez", "http://skateparkoftampa.com/spot/headshots/2585.jpg");
        Ride ride = new Ride("FROM_TEC", "11:30 am", "Miercoles", 12345678.0, 87654321.0);

        SharedPreferences sharedPreferences = getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);

        presenter = new PostPresenter(this, AppRepository.getInstance(sharedPreferences),
                SchedulerProvider.getInstance());
        presenter.start();
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
        } else {
            autocompleteFragment.setHint(DESTINO);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
