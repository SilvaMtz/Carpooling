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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.CarpoolingService;
import itesm.mx.carpoolingtec.data.FakeCarpoolingService;
import itesm.mx.carpoolingtec.model.Ride;
import itesm.mx.carpoolingtec.model.User;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RidesFragment extends Fragment implements RidesView,
        SwipeRefreshLayout.OnRefreshListener, RideItemListener, ChildEventListener {

    private static final String TAG = "RidesFragment";

    public static final String RIDE_TYPE = "rideType";
    public static final int TO_TEC = 0;
    public static final int FROM_TEC = 1;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_rides) RecyclerView recyclerView;

    private RidesAdapter ridesAdapter;
    private Unbinder unbinder;

    private DatabaseReference databaseReference;
    private RidesPresenter presenter;

    private int rideType;
    private List<User> users;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ridesAdapter = new RidesAdapter(getActivity(), new ArrayList<Ride>(), this, rideType);
        recyclerView.setAdapter(ridesAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://carpooling-tec.firebaseio.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CarpoolingService service = retrofit.create(CarpoolingService.class);
        FakeCarpoolingService fakeService = new FakeCarpoolingService();

        presenter = new RidesPresenter(this, fakeService, SchedulerProvider.getInstance());
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
    public void showRides(List<Ride> rides) {
        ridesAdapter.setData(rides);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRides() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoRidesAvailableToast() {

    }

    @Override
    public void showErrorLoadingRidesToast() {

    }

    @Override
    public void onRefresh() {
        presenter.loadRides();
    }

    @Override
    public void onRideClick(Ride ride) {

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
