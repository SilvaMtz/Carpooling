package itesm.mx.carpoolingtec.request;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.firebase.Request;
import itesm.mx.carpoolingtec.util.Utilities;

/**
 * Created by DavidMartinez on 4/23/17.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{

    private Context context;
    private List<Request> requests;
    private RequestListener listener;

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Request request = requests.get(position);

        Utilities.setRoundedPhoto(context, request.getUser().getPhoto(), holder.ivPicture);
        holder.tvName.setText(request.getUser().getName());

        holder.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAcceptClick(request);
            }
        });

        holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeclineClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container) RelativeLayout rlContainer;
        @BindView(R.id.image_user) ImageView ivPicture;
        @BindView(R.id.text_name) TextView tvName;
        @BindView(R.id.text_timestamp) TextView tvTimestamp;
        @BindView(R.id.button_aceptar) Button btnAceptar;
        @BindView(R.id.button_rechazar) Button btnRechazar;

        public ViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
