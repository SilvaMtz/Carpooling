package itesm.mx.carpoolingtec.request;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public RequestAdapter(Context context, List<Request> requests, RequestListener listener) {
        this.context = context;
        this.requests = requests;
        this.listener = listener;
    }


    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);

        return new ViewHolder(view);
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

    public void setData(List<Request> data) {
        requests.clear();
        requests.addAll(data);
        notifyDataSetChanged();
    }

    public void addRequest(Request request) {
        requests.add(request);
        notifyDataSetChanged();
    }

    public void clearRequests() {
        requests.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null != requests ? requests.size() : 0;
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
