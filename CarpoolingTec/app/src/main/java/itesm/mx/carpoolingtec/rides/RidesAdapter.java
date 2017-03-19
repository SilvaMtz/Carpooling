package itesm.mx.carpoolingtec.rides;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import itesm.mx.carpoolingtec.model.Ride;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private List<Ride> rides;

    public RidesAdapter(List<Ride> rides) {
        this.rides = rides;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: inflate layout with ride item and return new ViewHolder.
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ride ride = rides.get(position);

        //TODO: set view data.
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

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
