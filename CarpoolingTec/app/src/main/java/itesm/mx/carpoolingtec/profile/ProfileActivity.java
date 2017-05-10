package itesm.mx.carpoolingtec.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.post.PostActivity;
import itesm.mx.carpoolingtec.userinfo.PedirInfoActivity;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

public class ProfileActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, ProfileView {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;

    @BindView(R.id.appBarLayout) AppBarLayout appBarLayout;
    @BindView(R.id.profile_image) ImageView ivProfile;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) Toolbar toolbarTitle;
    @BindView(R.id.rv_profile_rides_to_tec) RecyclerView rvProfileRidesTo;
    @BindView(R.id.rv_profile_rides_from_tec) RecyclerView rvProfileRidesFrom;
    @BindView(R.id.text_name) TextView tvName;
    @BindView(R.id.text_phone) TextView tvPhone;

    private int maxScrollSize;
    private boolean isAvatorShown;
    private ProfilePresenter presenter;
    private FirebaseRecyclerAdapter<Ride, ProfileRideHolder> ridesAdapterTo;
    private FirebaseRecyclerAdapter<Ride, ProfileRideHolder> ridesAdapterFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        toolbarTitle.setTitle("Tus rides");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appBarLayout.addOnOffsetChangedListener(this);
        maxScrollSize = appBarLayout.getTotalScrollRange();

        SharedPreferences sharedPreferences = getSharedPreferences(
                MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);

        Repository repository = AppRepository.getInstance(sharedPreferences);
        DatabaseReference ridesRefTo = repository.getDatabase().child("rides_to_tec")
                .child(repository.getMyId()).child("rides");
        DatabaseReference ridesRefFrom = repository.getDatabase().child("rides_to_tec")
                .child(repository.getMyId()).child("rides");

        rvProfileRidesTo.setLayoutManager(new LinearLayoutManager(this));
        rvProfileRidesFrom.setLayoutManager(new LinearLayoutManager(this));

        presenter = new ProfilePresenter(AppRepository.getInstance(sharedPreferences),
                SchedulerProvider.getInstance());
        presenter.attachView(this);
        presenter.loadUserData();

        ridesAdapterTo = new FirebaseRecyclerAdapter<Ride, ProfileRideHolder>(Ride.class,
                R.layout.ride_item, ProfileRideHolder.class, ridesRefTo) {
            @Override
            protected void populateViewHolder(ProfileRideHolder viewHolder, final Ride ride, final int position) {
                viewHolder.tvDay.setText(ride.getWeekday());
                viewHolder.tvTime.setText(ride.getTime());
                viewHolder.tvRideType.setText("Hacia el Tec");
                viewHolder.ibLapiz.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        String key = getRef(position).getKey();
                        openEditRideOptions(ride, key);
                    }
                });
            }
        };
        rvProfileRidesTo.setAdapter(ridesAdapterTo);

        ridesAdapterFrom = new FirebaseRecyclerAdapter<Ride, ProfileRideHolder>(Ride.class,
                R.layout.ride_item, ProfileRideHolder.class, ridesRefFrom) {
            @Override
            protected void populateViewHolder(ProfileRideHolder viewHolder, final Ride ride, final int position) {
                viewHolder.tvDay.setText(ride.getWeekday());
                viewHolder.tvTime.setText(ride.getTime());
                viewHolder.tvRideType.setText("Desde el Tec");
                viewHolder.ibLapiz.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        String key = getRef(position).getKey();
                        openEditRideOptions(ride, key);
                    }
                });
            }
        };
        rvProfileRidesFrom.setAdapter(ridesAdapterFrom);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        // Set edit icon color to white.
        Drawable editIcon = menu.findItem(R.id.action_edit).getIcon();
        if (editIcon != null) {
            editIcon.mutate();
            editIcon.setColorFilter(ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit:
                SharedPreferences sharedPreferences = getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);
                Repository repository = AppRepository.getInstance(sharedPreferences);
                repository.getDatabase().child("users").child(repository.getMyId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setId(dataSnapshot.getKey());
                        openPedirInfo(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / maxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatorShown) {
            isAvatorShown = false;

            ivProfile.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatorShown) {
            isAvatorShown = true;

            ivProfile.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    @Override
    public void openEditRideOptions(final Ride ride, final String key) {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.edit_ride_options_layout, true)
                .build();

        View view = dialog.getCustomView();
        if (view == null) {
            return;
        }

        TextView tvModificar = (TextView) view.findViewById(R.id.text_modificar);
        TextView tvEliminar = (TextView) view.findViewById(R.id.text_eliminar);

        tvModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteRide(ride, key);
                presenter.onEditRide(ride);
                dialog.dismiss();
            }
        });

        tvEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteRide(ride, key);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void openPostActivity(final Ride ride){
        Intent intent = new Intent(this,PostActivity.class);
        intent.putExtra("id",1); // 1 representa Perfil
        intent.putExtra("ride",ride);
        startActivity(intent);

    }

    @Override
    public void openPedirInfo(User user){
        Intent intent = new Intent(this, PedirInfoActivity.class);
        intent.putExtra("id",1);
        intent.putExtra("user", user);
        startActivity(intent);

    }

    @Override
    public void showRemovedRideToast() {
    }

    @Override
    public void showUserData(User user) {
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());
        Picasso.with(this).load(user.getPhoto()).into(ivProfile);
    }
}


