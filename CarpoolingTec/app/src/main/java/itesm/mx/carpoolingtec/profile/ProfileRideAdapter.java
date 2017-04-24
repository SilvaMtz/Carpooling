package itesm.mx.carpoolingtec.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.firebase.Ride;

public class ProfileRideAdapter extends RecyclerView.Adapter<ProfileRideAdapter.ProfileRideHolder> {

    private Context context;
    private List<Ride> rides;
    private PublishSubject<Ride> viewLongClickSubject = PublishSubject.create();

    public ProfileRideAdapter(Context context, List<Ride> rides) {
        this.context = context;
        this.rides = rides;
    }

    @Override
    public ProfileRideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ride_item, parent, false);

        return new ProfileRideHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileRideHolder holder, int position) {
        Ride ride = rides.get(position);

        holder.bindData(ride, viewLongClickSubject);
    }

    @Override
    public int getItemCount() {
        return null != rides ? rides.size() : 0;
    }

    public void addRide(Ride ride) {
        rides.add(ride);
        notifyDataSetChanged();
    }

    public void removeRide(Ride ride) {
        rides.remove(ride);
        notifyDataSetChanged();
    }

    public Observable<Ride> getLongClickObservable() {
        return viewLongClickSubject;
    }

    public static class ProfileRideHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container) View view;
        @BindView(R.id.text_day) TextView tvDay;
        @BindView(R.id.text_time) TextView tvTime;
        @BindView(R.id.text_ride_type) TextView tvRideType;

        public ProfileRideHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final Ride ride, final PublishSubject<Ride> viewLongCLickSubject) {
            tvDay.setText(ride.getWeekday());
            tvTime.setText(ride.getTime());

            if (ride.getRide_type().equals("FROM_TEC")) {
                tvRideType.setText("Desde el Tec");
            } else {
                tvRideType.setText("Hacia el Tec");
            }

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    viewLongCLickSubject.onNext(ride);
                    return false;
                }
            });
        }
    }
}
