package itesm.mx.carpoolingtec.contacts;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.model.firebase.Contact;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;


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
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactsAdapter = new ContactsAdapter(getActivity(), new ArrayList<Contact>(), this);
        recyclerView.setAdapter(contactsAdapter);

        presenter = new ContactsPresenter(this, AppRepository.getInstance(),
                SchedulerProvider.getInstance());
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

    @Override
    public void onContactClick(Contact contact) {

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
    public void showContacts() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContacts() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void clearContacts() {
        contactsAdapter.clearData();
    }

    @Override
    public void showErrorMessageToast() {
        Toast.makeText(getContext(), R.string.error_loading_contacts, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addContact(Contact contact) {
        contactsAdapter.addContact(contact);
    }
}
