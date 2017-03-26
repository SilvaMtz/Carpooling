package itesm.mx.carpoolingtec.contacts;

import android.content.Context;
import android.net.Uri;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.CarpoolingService;
import itesm.mx.carpoolingtec.model.User;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ContactsFragment extends Fragment implements ContactsView,
        SwipeRefreshLayout.OnRefreshListener, ContactItemListener {

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_contacts) RecyclerView recyclerView;

    private ContactsAdapter contactsAdapter;
    private Unbinder unbinder;

    private ContactsPresenter presenter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.action_sort).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsAdapter = new ContactsAdapter(getActivity(), new ArrayList<User>(), this);
        recyclerView.setAdapter(contactsAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://carpooling-tec.firebaseio.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CarpoolingService service = retrofit.create(CarpoolingService.class);

        presenter = new ContactsPresenter(this, service, SchedulerProvider.getInstance());
        presenter.start();
        presenter.loadContacts();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.stop();
    }

    private List<User> getDummyContacts() {
        List<User> contacts = new ArrayList<>();
        contacts.add(new User("Mariel Palacios","http://orig04.deviantart.net/aded/f/2013/066/c/2/profile_picture_by_naivety_stock-d5x8lbn.jpg"));
        contacts.add(new User("Kenan Zertuche","http://orig10.deviantart.net/b1f3/f/2011/258/1/8/profile_picture_by_ff_stock-d49yyse.jpg"));
        contacts.add(new User("Kel Zertuche","http://skateparkoftampa.com/spot/headshots/2585.jpg"));

        return contacts;
    }

    @Override
    public void onContactClick(User contact) {

    }

    @Override
    public void onRefresh() {
        presenter.loadContacts();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        swipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showContacts(List<User> users) {
        contactsAdapter.setData(users);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContacts() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessageToast() {
        Toast.makeText(getContext(), "Error cargando contactos", Toast.LENGTH_SHORT).show();
    }
}
