package itesm.mx.carpoolingtec.rides;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.model.firebase.Ride;
import itesm.mx.carpoolingtec.model.firebase.User;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.util.Utilities;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

import static android.content.Context.MODE_PRIVATE;

public class RidesFragment extends Fragment implements RidesView,
        SwipeRefreshLayout.OnRefreshListener, RideItemListener {

    private static final String TAG = "RidesFragment";

    public static final String RIDE_TYPE = "rideType";
    public static final int TO_TEC = 0;
    public static final int FROM_TEC = 1;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_rides) RecyclerView recyclerView;

    private RidesAdapter ridesAdapter;
    private Unbinder unbinder;

    private RidesPresenter presenter;

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
        menu.findItem(R.id.action_sort).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rides, container, false);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ridesAdapter = new RidesAdapter(getActivity(), new ArrayList<UserRide>(), this, rideType);
        recyclerView.setAdapter(ridesAdapter);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);

        presenter = new RidesPresenter(this, AppRepository.getInstance(sharedPreferences),
                SchedulerProvider.getInstance(), rideType);
        presenter.start();
        presenter.loadRides();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.stop();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        swipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showUserRides() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUserRides() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void addUserRide(UserRide userRide) {
        ridesAdapter.addUserRide(userRide);
    }

    @Override
    public void clearUserRides() {
        ridesAdapter.clearUserRides();
    }


    @Override
    public void showErrorLoadingRidesToast() {
        Toast.makeText(getContext(), R.string.error_loading_rides, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openUserRideDetails(User user,
                                    List<String> ridesMonday,
                                    List<String> ridesTuesday,
                                    List<String> ridesWednesday,
                                    List<String> ridesThursday,
                                    List<String> ridesFriday,
                                    List<String> ridesSaturday,
                                    List<String> ridesSunday) {

        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.user_preview_card, true)
                .positiveText("Solicitar")
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
    public void onRefresh() {
        presenter.loadRides();
    }

    @Override
    public void onRideClick(UserRide ride) {
        presenter.onRideClick(ride);
    }
}
