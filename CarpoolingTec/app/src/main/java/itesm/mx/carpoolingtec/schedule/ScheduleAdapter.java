package itesm.mx.carpoolingtec.schedule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.model.ScheduleItem;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DRIVER_TYPE = 0;
    private static final int PASSENGER_TYPE = 1;
    private static final int DAY_TEXT_TYPE = 2;

    private Context context;
    private List<ScheduleItem> scheduleItems;

    public ScheduleAdapter(Context context, List<ScheduleItem> scheduleItems) {
        this.context = context;
        this.scheduleItems = scheduleItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (scheduleItems.get(position).getRideType() == ScheduleItem.RideType.DRIVER) {
            return DRIVER_TYPE;
        } else if (scheduleItems.get(position).getRideType() == ScheduleItem.RideType.PASSENGER) {
            return PASSENGER_TYPE;
        } else {
            return DAY_TEXT_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case DAY_TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.schedule_day_text_item, parent, false);
                return new ViewHolderDayText(view);
            case DRIVER_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.schedule_driver_item, parent, false);
                return new ViewHolderDriver(view);
            case PASSENGER_TYPE:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.schedule_passenger_item, parent, false);
                return new ViewHolderPassenger(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScheduleItem scheduleItem = scheduleItems.get(position);

        switch (holder.getItemViewType()) {
            case DRIVER_TYPE:
                ViewHolderDriver holderDriver = (ViewHolderDriver) holder;
                setHolderDriverViews(holderDriver, scheduleItem);
                break;
            case PASSENGER_TYPE:
                ViewHolderPassenger holderPassenger = (ViewHolderPassenger) holder;
                setHolderPassengerViews(holderPassenger, scheduleItem);
                break;
            case DAY_TEXT_TYPE:
                ViewHolderDayText holderDayText = (ViewHolderDayText) holder;
                setHolderDayTextViews(holderDayText, scheduleItem);
        }
    }

    @Override
    public int getItemCount() {
        return scheduleItems != null ? scheduleItems.size() : 0;
    }

    private void setHolderDriverViews(final ViewHolderDriver holder, ScheduleItem scheduleItem) {
        Picasso.with(context)
                .load(scheduleItem.getPassenger().getFoto())
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

        holder.tvArrival.setText(context.getString(R.string.arrival_tec, scheduleItem.getTime()));
        holder.tvDestination.setText(scheduleItem.getPassenger().getLocation());
        holder.tvTitle.setText(context.getString(R.string.driver_pick_up, scheduleItem.getPassenger().getNombre()));
    }

    private void setHolderPassengerViews(final ViewHolderPassenger holder, ScheduleItem scheduleItem) {
        Picasso.with(context)
                .load(scheduleItem.getPassenger().getFoto())
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

        holder.tvArrival.setText(context.getString(R.string.arrival_tec, scheduleItem.getTime()));
        holder.tvTitle.setText(context.getString(R.string.passenger_picked_up, scheduleItem.getPassenger().getNombre()));
    }

    private void setHolderDayTextViews(final ViewHolderDayText holder, ScheduleItem item) {
        ScheduleItem.DayOfWeek dayOfWeek = item.getDay();

        if (dayOfWeek == ScheduleItem.DayOfWeek.MONDAY) {
            holder.tvDayText.setText("Lunes, 27 de marzo");
        } else if (dayOfWeek == ScheduleItem.DayOfWeek.TUESDAY) {
            holder.tvDayText.setText("Martes, 28 de marzo");
        } else if (dayOfWeek == ScheduleItem.DayOfWeek.WEDNESDAY) {
            holder.tvDayText.setText("Miercoles, 29 de marzo");
        } else if (dayOfWeek == ScheduleItem.DayOfWeek.THRUSDAY) {
            holder.tvDayText.setText("Jueves, 30 de marzo");
        } else if (dayOfWeek == ScheduleItem.DayOfWeek.FRIDAY) {
            holder.tvDayText.setText("Viernes, 31 de marzo");
        } else if (dayOfWeek == ScheduleItem.DayOfWeek.SATURDAY) {
            holder.tvDayText.setText("Sábado, 1 de abril");
        } else {
            holder.tvDayText.setText("Domingo, 2 de abril");
        }
    }

    static class ViewHolderDriver extends RecyclerView.ViewHolder {

        @BindView(R.id.image_user) ImageView ivPicture;
        @BindView(R.id.button_call) ImageButton ibCall;
        @BindView(R.id.text_title) TextView tvTitle;
        @BindView(R.id.text_arrival) TextView tvArrival;
        @BindView(R.id.text_destination) TextView tvDestination;
        @BindView(R.id.button_directions) ImageButton ibDirections;

        public ViewHolderDriver(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderPassenger extends RecyclerView.ViewHolder {

        @BindView(R.id.image_user) ImageView ivPicture;
        @BindView(R.id.button_call) ImageButton ibCall;
        @BindView(R.id.text_title) TextView tvTitle;
        @BindView(R.id.text_arrival) TextView tvArrival;

        public ViewHolderPassenger(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderDayText extends RecyclerView.ViewHolder {

        @BindView(R.id.text_day) TextView tvDayText;

        public ViewHolderDayText(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
