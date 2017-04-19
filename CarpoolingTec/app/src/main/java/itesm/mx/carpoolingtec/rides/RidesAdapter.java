package itesm.mx.carpoolingtec.rides;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.Ride;
import itesm.mx.carpoolingtec.model.firebase.UserRide;
import itesm.mx.carpoolingtec.util.Utilities;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private Context context;
    private List<UserRide> userRides;
    private RideItemListener listener;
    private int rideType;

    public RidesAdapter(Context context, List<UserRide> userRides, RideItemListener listener,
                        int rideType) {
        this.context = context;
        this.userRides = userRides;
        this.listener = listener;
        this.rideType = rideType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_item_2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UserRide ride = userRides.get(position);

        Utilities.setRoundedPhoto(context, ride.getUser().getPhoto(), holder.ivPicture);
        holder.tvName.setText(ride.getUser().getName());

        if (rideType == RidesFragment.TO_TEC) {
            holder.tvDescription.setText("Salida 3 km de tu ubicación");
        } else {
            holder.tvDescription.setText("Destino 3 km de tu ubicación");
        }

        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRideClick(ride);
            }
        });

    }

    @Override
    public int getItemCount() {
        return null != userRides ? userRides.size() : 0;
    }

    public void setData(List<UserRide> data) {
        userRides.clear();
        userRides.addAll(data);
        notifyDataSetChanged();
    }

    public void addUserRide(UserRide userRide) {
        userRides.add(userRide);
        notifyDataSetChanged();
    }

    public void clearUserRides() {
        userRides.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container) RelativeLayout rlContainer;
        @BindView(R.id.image_user) ImageView ivPicture;
        @BindView(R.id.text_name) TextView tvName;
        @BindView(R.id.text_description) TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
