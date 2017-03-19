package itesm.mx.carpoolingtec.rides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import itesm.mx.carpoolingtec.R;

public class RidesFragment extends Fragment implements RidesView{

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rides, container, false);
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
}
