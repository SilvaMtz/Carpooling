package itesm.mx.carpoolingtec.rides;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.firebase.User;

public class UserRideHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.container)
    RelativeLayout rlContainer;
    @BindView(R.id.image_user)
    ImageView ivPicture;
    @BindView(R.id.text_name)
    TextView tvName;
    @BindView(R.id.text_description) TextView tvDescription;

    public UserRideHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}