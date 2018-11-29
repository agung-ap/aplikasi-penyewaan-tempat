package id.developer.arrif.kosdankontrakan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.R;
import id.developer.arrif.kosdankontrakan.model.Posting;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Posting> postingArrayList;
    private DataListener listener;

    public PostingAdapter(Context context,DataListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.posting, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.namaTempat.setText(postingArrayList.get(i).getNamaTempat());
        viewHolder.alamatTempat.setText(postingArrayList.get(i).getAlamatTempat());
        viewHolder.hargaTempat.setText(postingArrayList.get(i).getHargaTempat());
        Picasso.get().load(postingArrayList.get(i).getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(viewHolder.gambarTempat);
    }

    @Override
    public int getItemCount() {
        if (null == postingArrayList) return 0;
        return postingArrayList.size();
    }

    public void setPostingData(ArrayList<Posting> postingArrayList) {
        this.postingArrayList = postingArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.gambar_tempat)
        ImageView gambarTempat;
        @BindView(R.id.nama_tempat)
        TextView namaTempat;
        @BindView(R.id.alamat_tempat)
        TextView alamatTempat;
        @BindView(R.id.harga_tempat)
        TextView hargaTempat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onClick(postingArrayList.get(position));
        }
    }

    //Membuat Interface
    public interface DataListener {
        void onClick(Posting dataPosition);
    }

}
