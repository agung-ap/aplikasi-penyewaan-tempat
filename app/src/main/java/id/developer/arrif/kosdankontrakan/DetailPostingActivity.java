package id.developer.arrif.kosdankontrakan;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.model.Booking;
import id.developer.arrif.kosdankontrakan.model.Posting;
import id.developer.arrif.kosdankontrakan.model.Users;

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
    @BindView(R.id.booking)
    Button booking;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference databaseReference;

    private ArrayList<Posting> getPostingList;
    private Boolean isAdmin;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_posting);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //init firebase auth
        auth = FirebaseAuth.getInstance();
        getUserName();
        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();
            //get data from intent
            getPostingList = new ArrayList<>();
            getPostingList = getBundle
                    .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        }
        //add collapsing toolbar name
        collapsingToolbarLayout.setTitle(getPostingList.get(0).getNamaTempat());
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        if (isAdmin){
            booking.setVisibility(View.GONE);
        }

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBooking(data());
            }
        });

        getPlaceInfo();
    }

    private Booking data(){
        Booking booking = new Booking();
        booking.setNamaTempat(getPostingList.get(0).getNamaTempat());
        booking.setNamaUser(username);
        booking.setPostingId(getPostingList.get(0).getKey());
        booking.setUserId(auth.getUid());

        return booking;
    }

    private void setBooking(Booking booking) {
        databaseReference = FirebaseDatabase.getInstance().getReference("booking");
        databaseReference.child(databaseReference.push().getKey()).setValue(booking);
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

    private void getUserName(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(auth.getUid())
                .child("users_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        username = dataSnapshot.child("nama").getValue(String.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
