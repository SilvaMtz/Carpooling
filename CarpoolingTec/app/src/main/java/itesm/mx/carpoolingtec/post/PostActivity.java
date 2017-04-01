package itesm.mx.carpoolingtec.post;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class PostActivity extends AppCompatActivity implements PostView {

    private static final String TAG = "PostActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;

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

        Spinner spinner = (Spinner) findViewById(R.id.tipoSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.viaje_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
}
