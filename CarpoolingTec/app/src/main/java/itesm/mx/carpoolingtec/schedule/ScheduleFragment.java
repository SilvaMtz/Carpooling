package itesm.mx.carpoolingtec.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.ScheduleItem;
import itesm.mx.carpoolingtec.model.User;

public class ScheduleFragment extends Fragment implements ScheduleView,
        SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_schedule) RecyclerView recyclerView;

    private ScheduleAdapter scheduleAdapter;
    private Unbinder unbinder;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scheduleAdapter = new ScheduleAdapter(getActivity(), getDummyScheduleItems());
        recyclerView.setAdapter(scheduleAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showItems() {

    }

    @Override
    public void hideItems() {

    }

    @Override
    public void showNoItemsToast() {

    }

    @Override
    public void showErrorLoadingItemsToast() {

    }

    private List<ScheduleItem> getDummyScheduleItems() {
        List<User> users = new ArrayList<>();
        users.add(new User("Jorge", "http://orig04.deviantart.net/aded/f/2013/066/c/2/profile_picture_by_naivety_stock-d5x8lbn.jpg"));
        users.add(new User("Karen", "http://orig10.deviantart.net/b1f3/f/2011/258/1/8/profile_picture_by_ff_stock-d49yyse.jpg"));
        users.add(new User("Raúl" ,"http://skateparkoftampa.com/spot/headshots/2585.jpg"));

        List<ScheduleItem> scheduleItems = new ArrayList<>();
        scheduleItems.add(new ScheduleItem(ScheduleItem.RideType.DAY_TEXT, ScheduleItem.DayOfWeek.TUESDAY));
        scheduleItems.add(new ScheduleItem(users.get(0), users.get(1), ScheduleItem.RideType.DRIVER,
                "12:00", ScheduleItem.DayOfWeek.TUESDAY));
        scheduleItems.add(new ScheduleItem(ScheduleItem.RideType.DAY_TEXT, ScheduleItem.DayOfWeek.WEDNESDAY));
        scheduleItems.add(new ScheduleItem(users.get(2), users.get(0), ScheduleItem.RideType.PASSENGER,
                "9:00", ScheduleItem.DayOfWeek.WEDNESDAY));
        scheduleItems.add(new ScheduleItem(users.get(0), users.get(2), ScheduleItem.RideType.DRIVER,
                "14:30", ScheduleItem.DayOfWeek.WEDNESDAY));

        return scheduleItems;
    }
}
