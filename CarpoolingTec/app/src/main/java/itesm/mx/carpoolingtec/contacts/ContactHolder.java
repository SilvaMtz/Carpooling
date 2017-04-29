package itesm.mx.carpoolingtec.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;

public class ContactHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.contactLayout) RelativeLayout rlContainer;
    @BindView(R.id.image_user) ImageView ivPicture;
    @BindView(R.id.text_name) TextView tvName;

    public ContactHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
