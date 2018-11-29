package id.developer.arrif.kosdankontrakan;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.model.Posting;

public class DetailPostingActivity extends AppCompatActivity {
    private static final String TAG = DetailPostingActivity.class.getSimpleName();

    @BindView(R.id.toolbarImage)
    ImageView toolImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    //place info
    @BindView(R.id.alamat_tempat_detail)
    TextView alamat;
    @BindView(R.id.harga_tempat_detail)
    TextView harga;
    @BindView(R.id.deskripsi_tempat_detail)
    TextView deskripsi;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference databaseReference;

    private ArrayList<Posting> getPostingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_posting);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //init firebase auth
        auth = FirebaseAuth.getInstance();
        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();
            //get data from intent
            getPostingList = new ArrayList<>();
            getPostingList = getBundle
                    .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        }
        //add collapsing toolbar name
        collapsingToolbarLayout.setTitle(getPostingList.get(0).getNamaTempat());


        getPlaceInfo();
    }

    private void getPlaceInfo() {
        Picasso.get().load(getPostingList.get(0).getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(toolImage);

        alamat.setText(getPostingList.get(0).getAlamatTempat());
        harga.setText("Rp "+getPostingList.get(0).getHargaTempat());
        deskripsi.setText(getPostingList.get(0).getDeskripsiTempat());
    }
}
