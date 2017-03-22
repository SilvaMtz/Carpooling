package itesm.mx.carpoolingtec.rides;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.Ride;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private Context context;
    private List<Ride> rides;
    private RideItemListener listener;

    public RidesAdapter(Context context, List<Ride> rides, RideItemListener listener) {
        this.context = context;
        this.rides = rides;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ride ride = rides.get(position);

        //TODO: set view data.

        // Make image view round.
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gates);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                .create(context.getResources(), bitmap);
        drawable.setCornerRadius(1000f);
        holder.ivPicture.setImageDrawable(drawable);

        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRideClick(ride);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != rides ? rides.size() : 0;
    }

    public void setData(List<Ride> data) {
        rides.clear();
        rides.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container) RelativeLayout rlContainer;
        @BindView(R.id.image_user) ImageView ivPicture;
        @BindView(R.id.text_neighborhood) TextView tvNeighborhood;
        @BindView(R.id.text_monday) TextView tvMonday;
        @BindView(R.id.text_tuesday) TextView tvTuesday;
        @BindView(R.id.text_wednesday) TextView tvWednesday;
        @BindView(R.id.text_thursday) TextView tvThursday;
        @BindView(R.id.text_friday) TextView tvFriday;
        @BindView(R.id.text_saturday) TextView tvSaturday;
        @BindView(R.id.text_sunday) TextView tvSunday;
        @BindView(R.id.text_distance) TextView tvDistance;
        @BindView(R.id.text_arrival) TextView tvArrival;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
