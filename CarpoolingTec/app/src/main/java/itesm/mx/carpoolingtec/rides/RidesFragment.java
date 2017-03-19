package itesm.mx.carpoolingtec.rides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;

public class RidesFragment extends Fragment implements RidesView,
        SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_rides) RecyclerView recyclerView;

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
    public void showRides() {

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
}
