package itesm.mx.carpoolingtec.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.data.Repository;
import itesm.mx.carpoolingtec.model.firebase.Contact;
import itesm.mx.carpoolingtec.util.Utilities;
import itesm.mx.carpoolingtec.util.schedulers.SchedulerProvider;

import static android.content.Context.MODE_PRIVATE;


public class ContactsFragment extends Fragment implements ContactsView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int CALL_PHONE_PERMISSION_REQUEST = 1;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_contacts) RecyclerView recyclerView;
    private Unbinder unbinder;

    private FirebaseRecyclerAdapter<Contact, ContactHolder> contactsAdapter;
    private ContactsPresenter presenter;
    private String phoneNumber;

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
        //menu.findItem(R.id.action_sort).setVisible(true);
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

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);

        Repository repository = AppRepository.getInstance(sharedPreferences);
        DatabaseReference contactsRef = repository.getDatabase().child("users").child(repository.getMyId()).child("contacts");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        contactsAdapter = new FirebaseRecyclerAdapter<Contact, ContactHolder>(Contact.class, R.layout.contact_item, ContactHolder.class, contactsRef) {
            @Override
            protected void populateViewHolder(final ContactHolder holder, final Contact contact, int position) {
                Utilities.setRoundedPhoto(getActivity(), contact.getPhoto(), holder.ivPicture);

                holder.tvName.setText(contact.getName());
                holder.rlContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onContactClick(contact);
                    }
                });
            }
        };

        recyclerView.setAdapter(contactsAdapter);

        presenter = new ContactsPresenter(this, AppRepository.getInstance(sharedPreferences),
                SchedulerProvider.getInstance());
        presenter.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_PERMISSION_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall(phoneNumber);
                } else {
                    Toast.makeText(getActivity(), "Se necesita permiso para realizar una llamada",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        swipeRefreshLayout.setRefreshing(active);
    }


    @Override
    public void showErrorMessageToast() {
        Toast.makeText(getContext(), R.string.error_loading_contacts, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openContactDetails(final Contact contact) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.contactinfo_item, true)
                .build();

        View view = dialog.getCustomView();
        if (view == null) {
            return;
        }

        ImageView ivPhoto = (ImageView) view.findViewById(R.id.image_user);
        TextView tvName = (TextView) view.findViewById(R.id.text_name);
        TextView tvPhone = (TextView) view.findViewById(R.id.text_phone);
        LinearLayout layoutCall = (LinearLayout) view.findViewById(R.id.layout_call);
        LinearLayout layoutMessage = (LinearLayout) view.findViewById(R.id.layout_message);
        LinearLayout layoutAdd = (LinearLayout) view.findViewById(R.id.layout_add);

        Utilities.setRoundedPhoto(getActivity(), contact.getPhoto(), ivPhoto);
        tvName.setText(contact.getName());
        tvPhone.setText(contact.getPhone());

        layoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.CALL_PHONE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    phoneNumber = contact.getPhone();
                    requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE},
                            CALL_PHONE_PERMISSION_REQUEST);

                } else {
                    makePhoneCall(contact.getPhone());
                }
            }
        });

        layoutMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contact.getPhone()));
                startActivity(intent);
            }
        });

        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhone())
                        .putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());

                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void makePhoneCall(String number) {
        if (number != null){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "No se puede realizar la llamada",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
