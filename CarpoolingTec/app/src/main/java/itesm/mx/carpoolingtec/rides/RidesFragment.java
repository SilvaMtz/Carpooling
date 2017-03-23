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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.Ride;
import itesm.mx.carpoolingtec.model.User;

public class RidesFragment extends Fragment implements RidesView,
        SwipeRefreshLayout.OnRefreshListener, RideItemListener {

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_rides) RecyclerView recyclerView;

    private RidesAdapter ridesAdapter;
    private Unbinder unbinder;

    public RidesFragment() {
        // Required empty public constructor
    }

    public static RidesFragment newInstance() {
        return new RidesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        ridesAdapter = new RidesAdapter(getActivity(), getDummyRides(), this);
        recyclerView.setAdapter(ridesAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showRides(List<Ride> rides) {

    }

    @Override
    public void hideRides() {

    }

    @Override
    public void showNoRidesAvailableToast() {

    }

    @Override
    public void showErrorLoadingRidesToast() {

    }

    @Override
    public void onRefresh() {

    }

    private List<Ride> getDummyRides() {
        List<User> users = new ArrayList<>();
        users.add(new User("http://orig04.deviantart.net/aded/f/2013/066/c/2/profile_picture_by_naivety_stock-d5x8lbn.jpg", "Valle Primavera"));
        users.add(new User("http://orig10.deviantart.net/b1f3/f/2011/258/1/8/profile_picture_by_ff_stock-d49yyse.jpg", "Narvarte"));
        users.add(new User("http://skateparkoftampa.com/spot/headshots/2585.jpg", "Lomas de Rosales"));

        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride(users.get(0), new boolean[]{true, false, false, true, false, false, false}, "9:00"));
        rides.add(new Ride(users.get(1), new boolean[]{false, true, true, false, false, true, false}, "16:00"));
        rides.add(new Ride(users.get(0), new boolean[]{false, false, false, true, false, false, false}, "14:30"));
        rides.add(new Ride(users.get(2), new boolean[]{false, true, false, false, true, false, false}, "13:45"));
        rides.add(new Ride(users.get(2), new boolean[]{false, false, false, false, false, true, true}, "10:30"));
        rides.add(new Ride(users.get(1), new boolean[]{true, true, false, true, true, false, false}, "9:00"));
        rides.add(new Ride(users.get(0), new boolean[]{false, true, true, false, false, false, false}, "11:00"));

        return rides;
    }

    @Override
    public void onRideClick(Ride ride) {

    }
}
