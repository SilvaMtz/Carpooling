package itesm.mx.carpoolingtec.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;

public class ProfileRideHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.container) View view;
    @BindView(R.id.text_day) TextView tvDay;
    @BindView(R.id.text_time) TextView tvTime;
    @BindView(R.id.text_ride_type) TextView tvRideType;

    public ProfileRideHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
