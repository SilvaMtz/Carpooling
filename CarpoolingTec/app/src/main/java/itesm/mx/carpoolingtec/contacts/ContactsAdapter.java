package itesm.mx.carpoolingtec.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import itesm.mx.carpoolingtec.model.User;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    private Context context;
    private List<User> contacts;
    private ContactItemListener listener;

    public ContactsAdapter(Context context, List<User> contacts, ContactItemListener listener) {
        this.context = context;
        this.contacts = contacts;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final User contact = contacts.get(position);

        Picasso.with(context)
                .load(contact.getPhoto())
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

        holder.tvName.setText(contact.getName());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != contacts ? contacts.size() : 0;
    }

    public void setData(List<User> data) {
        contacts.clear();
        contacts.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contactLayout) RelativeLayout rlContainer;
        @BindView(R.id.image_user) ImageView ivPicture;
        @BindView(R.id.text_name) TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
