package itesm.mx.carpoolingtec.rides;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Ride ride = rides.get(position);

        Picasso.with(context)
                .load(ride.getDriver().getFoto())
                .into(holder.ivPicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) holder.ivPicture.getDrawable())
                                .getBitmap();
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(context.getResources(), imageBitmap);
                        drawable.setCircular(true);
                        drawable.setCornerRadius(Math.max(imageBitmap.getWidth(),
                                imageBitmap.getHeight()) / 2.0f);
                        holder.ivPicture.setImageDrawable(drawable);
                    }

                    @Override
                    public void onError() {
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gates);
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(context.getResources(), bitmap);
                        drawable.setCircular(true);
                        drawable.setCornerRadius(Math.max(bitmap.getWidth(),
                                bitmap.getHeight()) / 2.0f);

                        holder.ivPicture.setImageDrawable(drawable);
                    }
                });

        holder.tvNeighborhood.setText(ride.getDriver().getLocation());
        holder.tvDistance.setText("2 km");
        holder.tvArrival.setText(context.getString(R.string.arrival, ride.getTime()));

        setWeekdaysStyle(holder, ride.getWeekdays());

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

    private void setWeekdaysStyle(ViewHolder holder, boolean[] weekdays) {
        final int colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
        final int colorNormal = ContextCompat.getColor(context, R.color.textSecondary);

        if (weekdays[0]) {
            holder.tvMonday.setTextColor(colorAccent);
            holder.tvMonday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvMonday.setTextColor(colorNormal);
            holder.tvMonday.setTypeface(null, Typeface.NORMAL);
        }

        if (weekdays[1]) {
            holder.tvTuesday.setTextColor(colorAccent);
            holder.tvTuesday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvTuesday.setTextColor(colorNormal);
            holder.tvTuesday.setTypeface(null, Typeface.NORMAL);
        }

        if (weekdays[2]) {
            holder.tvWednesday.setTextColor(colorAccent);
            holder.tvWednesday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvWednesday.setTextColor(colorNormal);
            holder.tvWednesday.setTypeface(null, Typeface.NORMAL);
        }

        if (weekdays[3]) {
            holder.tvThursday.setTextColor(colorAccent);
            holder.tvThursday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvThursday.setTextColor(colorNormal);
            holder.tvThursday.setTypeface(null, Typeface.NORMAL);
        }

        if (weekdays[4]) {
            holder.tvFriday.setTextColor(colorAccent);
            holder.tvFriday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvFriday.setTextColor(colorNormal);
            holder.tvFriday.setTypeface(null, Typeface.NORMAL);
        }

        if (weekdays[5]) {
            holder.tvSaturday.setTextColor(colorAccent);
            holder.tvSaturday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvSaturday.setTextColor(colorNormal);
            holder.tvSaturday.setTypeface(null, Typeface.NORMAL);
        }

        if (weekdays[6]) {
            holder.tvSunday.setTextColor(colorAccent);
            holder.tvSunday.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvSunday.setTextColor(colorNormal);
            holder.tvSunday.setTypeface(null, Typeface.NORMAL);
        }
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
