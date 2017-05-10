package itesm.mx.carpoolingtec.rides;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;

public class RideUserDetailHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.text_day) TextView tvDay;
    public @BindView(R.id.text_time) TextView tvTime;
    public @BindView(R.id.text_km) TextView tvKm;

    public RideUserDetailHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
