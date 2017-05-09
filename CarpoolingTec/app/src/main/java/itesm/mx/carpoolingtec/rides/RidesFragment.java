package itesm.mx.carpoolingtec.rides;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.util.Utilities;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

import static android.content.Context.MODE_PRIVATE;

public class RidesFragment extends Fragment implements RidesView, RideItemListener {

    private static final String TAG = "RidesFragment";

    public static final String RIDE_TYPE = "rideType";
    public static final int TO_TEC = 0;
    public static final int FROM_TEC = 1;

    @BindView(R.id.rv_rides) RecyclerView recyclerView;

    private Unbinder unbinder;

    private RidesPresenter presenter;
    private FirebaseRecyclerAdapter<UserRide, UserRideHolder> ridesAdapter;
    private Repository repository;

    private int rideType;

    public RidesFragment() {
        // Required empty public constructor
    }

    public static RidesFragment newInstance(int type) {
        RidesFragment fragment = new RidesFragment();
        Bundle args = new Bundle();
        args.putInt(RIDE_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        rideType = getArguments().getInt(RIDE_TYPE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.action_sort).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rides, container, false);
        unbinder = ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);
        repository = AppRepository.getInstance(sharedPreferences);

        presenter = new RidesPresenter(this, repository, SchedulerProvider.getInstance(), rideType);
        presenter.start();

        String childKey;
        if (rideType == TO_TEC) {
            childKey = "rides_to_tec";
        } else {
            childKey = "rides_from_tec";
        }
        DatabaseReference databaseRef = repository.getDatabase().child(childKey);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ridesAdapter = new FirebaseRecyclerAdapter<UserRide, UserRideHolder>(UserRide.class,
                R.layout.ride_item_2, UserRideHolder.class, databaseRef) {
            @Override
            protected void populateViewHolder(UserRideHolder holder, final UserRide ride, int position) {
                Utilities.setRoundedPhoto(getActivity(), ride.getUser().getPhoto(), holder.ivPicture);
                holder.tvName.setText(ride.getUser().getName());

                Location userLocation = new Location("");
                userLocation.setLatitude(repository.getMyLatitude());
                userLocation.setLongitude(repository.getMyLongitude());

                Location tecLocation = new Location("");
                tecLocation.setLatitude(25.650699);
                tecLocation.setLongitude(-100.289432);

                float distanceInKm = userLocation.distanceTo(tecLocation) / 1000;
                DecimalFormat decimalFormat = new DecimalFormat("#.#");

                if (rideType == RidesFragment.TO_TEC) {
                    holder.tvDescription.setText("Salida " + decimalFormat.format(distanceInKm) + " km de tu ubicación");
                } else {
                    holder.tvDescription.setText("Destino " + decimalFormat.format(distanceInKm) + " km de tu ubicación");
                }

                holder.rlContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRideClick(ride);
                    }
                });
            }
        };

        recyclerView.setAdapter(ridesAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.stop();
    }

    @Override
    public void openUserRideDetails(final User user,
                                    List<String> ridesMonday,
                                    List<String> ridesTuesday,
                                    List<String> ridesWednesday,
                                    List<String> ridesThursday,
                                    List<String> ridesFriday,
                                    List<String> ridesSaturday,
                                    List<String> ridesSunday) {

        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.user_preview_card, true)
                .positiveText("Solicitar Contacto")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        repository.addRequest(user);
                    }
                })
                .build();

        View view = dialog.getCustomView();
        if (view == null) {
            return;
        }

        // Set user name, photo and description.
        ImageView ivPhoto = (ImageView) view.findViewById(R.id.image_user);
        TextView tvName = (TextView) view.findViewById(R.id.text_name);
        TextView tvNotes = (TextView) view.findViewById(R.id.text_notes);
        Utilities.setRoundedPhoto(getActivity(), user.getPhoto(), ivPhoto);
        tvName.setText(user.getName());
        tvNotes.setText(user.getNotes());

        // Set Driver preferences
        ImageView ivFumar = (ImageView) view.findViewById(R.id.iv_fumar);
        ImageView ivPrecio = (ImageView) view.findViewById(R.id.iv_precio);
        ImageView ivGender = (ImageView) view.findViewById(R.id.iv_gender);


        if (user.isSmoking()) {
            ivFumar.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_smoking_rooms_black_24dp));
        } else {
            ivFumar.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_smoke_free_black_24dp));
        }

        if (user.isPrice()) {
            ivPrecio.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_attach_money_black_24dp));
        } else {
            ivPrecio.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_money_off_black_24dp));
        }

        if (user.getGender() == 0) {
            ivGender.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.man));
        } else if (user.getGender() == 1) {
            ivGender.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.woman));
        } else {
            ivGender.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_wc_black_24dp));
        }

        // Populate lists with rides for each day.
        RecyclerView rvMonday = (RecyclerView) view.findViewById(R.id.rv_lunes);
        rvMonday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterMonday = new RidesTimeAdapter(ridesMonday);
        rvMonday.setAdapter(adapterMonday);

        RecyclerView rvTuesday = (RecyclerView) view.findViewById(R.id.rv_martes);
        rvTuesday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterTuesday = new RidesTimeAdapter(ridesTuesday);
        rvTuesday.setAdapter(adapterTuesday);

        RecyclerView rvWednesday = (RecyclerView) view.findViewById(R.id.rv_miercoles);
        rvWednesday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterWednesday = new RidesTimeAdapter(ridesWednesday);
        rvWednesday.setAdapter(adapterWednesday);

        RecyclerView rvThursday = (RecyclerView) view.findViewById(R.id.rv_jueves);
        rvThursday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterThursday = new RidesTimeAdapter(ridesThursday);
        rvThursday.setAdapter(adapterThursday);

        RecyclerView rvFriday = (RecyclerView) view.findViewById(R.id.rv_viernes);
        rvFriday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterFriday = new RidesTimeAdapter(ridesFriday);
        rvFriday.setAdapter(adapterFriday);

        RecyclerView rvSaturday = (RecyclerView) view.findViewById(R.id.rv_sabado);
        rvSaturday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterSaturday = new RidesTimeAdapter(ridesSaturday);
        rvSaturday.setAdapter(adapterSaturday);

        RecyclerView rvSunday = (RecyclerView) view.findViewById(R.id.rv_domingo);
        rvSunday.setLayoutManager(new LinearLayoutManager(getActivity()));
        RidesTimeAdapter adapterSunday = new RidesTimeAdapter(ridesSunday);
        rvSunday.setAdapter(adapterSunday);

        dialog.show();
    }

    @Override
    public void onRideClick(UserRide ride) {
        presenter.onRideClick(ride);
    }
}
