package itesm.mx.carpoolingtec.request;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;

public class SolicitudHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.container) RelativeLayout rlContainer;
    @BindView(R.id.image_user) ImageView ivPicture;
    @BindView(R.id.text_name) TextView tvName;
    @BindView(R.id.text_timestamp) TextView tvTimestamp;
    @BindView(R.id.button_aceptar) Button btnAceptar;
    @BindView(R.id.button_rechazar) Button btnRechazar;

    public SolicitudHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}