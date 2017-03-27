package itesm.mx.carpoolingtec.rides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.CarpoolingService;
import itesm.mx.carpoolingtec.data.FakeCarpoolingService;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://carpooling-tec.firebaseio.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CarpoolingService service = retrofit.create(CarpoolingService.class);
        FakeCarpoolingService fakeService = new FakeCarpoolingService();

        presenter = new RidesPresenter(this, AppRepository.getInstance(), SchedulerProvider.getInstance(), rideType);
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
    public void onRefresh() {
        presenter.loadRides();
    }

    @Override
    public void onRideClick(UserRide ride) {

    }
}
