package itesm.mx.carpoolingtec.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.model.firebase.Request;
import itesm.mx.carpoolingtec.util.Utilities;

public class RequestActivity extends AppCompatActivity implements RequestListener {

    @BindView(R.id.rv_solicitudes) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Context context;
    private FirebaseRecyclerAdapter<Request, SolicitudHolder> solicitudAdapter;
    private AppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);

        context = this;

        setSupportActionBar(toolbar);

        setTitle("Solicitudes");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(
                MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE);
        repository = AppRepository.getInstance(sharedPreferences);
        DatabaseReference databaseRef = repository.getDatabase().child("solicitudes").child(repository.getMyId());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        solicitudAdapter = new FirebaseRecyclerAdapter<Request, SolicitudHolder>(Request.class,
                R.layout.request_item, SolicitudHolder.class, databaseRef) {
            @Override
            protected void populateViewHolder(SolicitudHolder holder, final Request request, int position) {
                final String key = getRef(position).getKey();

                Utilities.setRoundedPhoto(context, request.getSolicitante().getPhoto(), holder.ivPicture);
                holder.tvName.setText(request.getSolicitante().getName());

                holder.btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAcceptClick(request, key);
                    }
                });

                holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeclineClick(key);
                    }
                });
            }
        };

        recyclerView.setAdapter(solicitudAdapter);
    }

    @Override
    public void onAcceptClick(Request request, String requestKey) {
        repository.addContact(request.getSolicitante());
        repository.removeRequest(requestKey);
        Toast.makeText(this, "Solicitud aceptada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineClick(String requestKey) {
        repository.removeRequest(requestKey);
        Toast.makeText(this, "Solicitud rechazada", Toast.LENGTH_SHORT).show();
    }
}
