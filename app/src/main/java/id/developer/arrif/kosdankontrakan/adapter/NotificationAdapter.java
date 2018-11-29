package id.developer.arrif.kosdankontrakan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.R;
import id.developer.arrif.kosdankontrakan.model.Booking;
import id.developer.arrif.kosdankontrakan.model.Posting;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Booking> bookingArrayList;
    private DataListener listener;

    public NotificationAdapter(Context context, DataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, int i) {
        viewHolder.placeName.setText(bookingArrayList.get(i).getNamaTempat());
        viewHolder.userName.setText("User Name : "+bookingArrayList.get(i).getNamaUser());
    }

    @Override
    public int getItemCount() {
        if (null == bookingArrayList)return 0;
        return bookingArrayList.size();
    }

    public void setBookingData(ArrayList<Booking> bookingArrayList) {
        this.bookingArrayList = bookingArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @BindView(R.id.nama_user_notification)
        TextView userName;
        @BindView(R.id.nama_tempat_notification)
        TextView placeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onClick(bookingArrayList.get(position));
        }
    }

    //Membuat Interface
    public interface DataListener {
        void onClick(Booking dataPosition);
    }
}
