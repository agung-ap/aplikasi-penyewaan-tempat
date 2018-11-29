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
import id.developer.arrif.kosdankontrakan.model.UsersValidation;

public class UserValidationAdapter  extends RecyclerView.Adapter<UserValidationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UsersValidation> usersValidationArrayList;

    public UserValidationAdapter(Context context, DataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    private DataListener listener;



    @NonNull
    @Override
    public UserValidationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.notification_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserValidationAdapter.ViewHolder viewHolder, int i) {
        viewHolder.message.setText(usersValidationArrayList.get(i).getMessage());
        viewHolder.userName.setText("User Name : "+usersValidationArrayList.get(i).getUserName());
    }

    @Override
    public int getItemCount() {
        if (null == usersValidationArrayList)return 0;
        return usersValidationArrayList.size();
    }

    public void setUserValidationData(ArrayList<UsersValidation> usersValidationArrayList){
        this.usersValidationArrayList = usersValidationArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.username)
        TextView userName;
        @BindView(R.id.message_for_user)
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(usersValidationArrayList.get(getAdapterPosition()));
        }
    }

    //Membuat Interface
    public interface DataListener {
        void onClick(UsersValidation dataPosition);
    }
}
