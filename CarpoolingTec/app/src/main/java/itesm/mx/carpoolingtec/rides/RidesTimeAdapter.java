package itesm.mx.carpoolingtec.rides;

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
import itesm.mx.carpoolingtec.R;

public class RidesTimeAdapter extends RecyclerView.Adapter<RidesTimeAdapter.ViewHolder> {

    private List<String> times;

    public RidesTimeAdapter(List<String> times) {
        this.times = times;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_time_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String time = times.get(position);

        Log.d("Adapter", time);

        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return null != times ? times.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_time) TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
