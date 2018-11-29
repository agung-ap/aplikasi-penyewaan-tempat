package id.developer.arrif.kosdankontrakan;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.model.Booking;
import id.developer.arrif.kosdankontrakan.model.Posting;
import id.developer.arrif.kosdankontrakan.model.Users;
import id.developer.arrif.kosdankontrakan.model.UsersValidation;

public class NotificationDetail extends AppCompatActivity {
    @BindView(R.id.nama_tempat_notif)
    TextView nama;
    @BindView(R.id.alamat_tempat_notif)
    TextView alamat;
    @BindView(R.id.harga_tempat_notif)
    TextView harga;
    @BindView(R.id.deskripsi_tempat_notif)
    TextView deskripsi;
    @BindView(R.id.terima)
    Button terima;
    @BindView(R.id.tolak)
    Button tolak;

    private ArrayList<Booking> getBookingList;
    private ArrayList<UsersValidation> getUsersValidations;

    private DatabaseReference databaseReference;
    private String message;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.bind(this);

        isAdmin = getIntent().getBooleanExtra("isAdmin", true);
        if (isAdmin) {

            if (savedInstanceState == null){
                Bundle getBundle = getIntent().getExtras();
                //get data from intent
                getBookingList = new ArrayList<>();
                getBookingList = getBundle
                        .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
            }

            getPlaceData(getBookingList.get(0).getPostingId());

            harga.setVisibility(View.VISIBLE);
            deskripsi.setVisibility(View.VISIBLE);
            terima.setVisibility(View.VISIBLE);
            tolak.setVisibility(View.VISIBLE);

            terima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message = "terima";
                    setUserValidation(data());
                }
            });

            tolak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message = "tolak";
                    setUserValidation(data());
                }
            });

        }else {
            if (savedInstanceState == null){
                Bundle getBundle = getIntent().getExtras();
                //get data from intent
                getUsersValidations = new ArrayList<>();
                getUsersValidations = getBundle
                        .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
            }

            showMessage();
        }
    }

    private void showMessage() {
        nama.setText(getUsersValidations.get(0).getUserName());
        alamat.setText(getUsersValidations.get(0).getMessage());

        harga.setVisibility(View.GONE);
        deskripsi.setVisibility(View.GONE);
        terima.setVisibility(View.GONE);
        tolak.setVisibility(View.GONE);

    }

    private UsersValidation data(){
        UsersValidation usersValidation = new UsersValidation();
        usersValidation.setUserName(getBookingList.get(0).getNamaUser());
        usersValidation.setMessage(message);

        return usersValidation;
    }

    private void setUserValidation(UsersValidation userValidation) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("user_validation");
        databaseReference.child(getBookingList.get(0).getPostingId())
                .child(getBookingList.get(0).getUserId())
                .setValue(userValidation);
    }

    private void getPlaceData(final String postingId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("posting");
        databaseReference.child(postingId)
                .child("posting_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Posting posting = dataSnapshot.getValue(Posting.class);
                        nama.setText(posting.getNamaTempat());
                        alamat.setText(posting.getAlamatTempat());
                        harga.setText("Rp "+posting.getHargaTempat());
                        deskripsi.setText(posting.getDeskripsiTempat());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
