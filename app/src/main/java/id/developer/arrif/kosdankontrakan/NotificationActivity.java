package id.developer.arrif.kosdankontrakan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.developer.arrif.kosdankontrakan.adapter.NotificationAdapter;
import id.developer.arrif.kosdankontrakan.adapter.UserValidationAdapter;
import id.developer.arrif.kosdankontrakan.model.Booking;
import id.developer.arrif.kosdankontrakan.model.UsersValidation;

public class NotificationActivity extends AppCompatActivity
        implements NotificationAdapter.DataListener ,
        UserValidationAdapter.DataListener{

    private static final String TAG = NotificationActivity.class.getSimpleName();
    private boolean isAdmin;

    private RecyclerView recyclerView;
    private TextView emptyMessage;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private NotificationAdapter notificationAdapter;
    private UserValidationAdapter userValidationAdapter;

    private ArrayList<Booking> bookingArrayList;
    private ArrayList<UsersValidation> usersValidationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        auth = FirebaseAuth.getInstance();
        //is admin check
        isAdmin = getIntent().getBooleanExtra("isAdmin" , true);
        Log.i(TAG, "isAdmin = " + isAdmin);
        emptyMessage = (TextView)findViewById(R.id.empty_message_notification);
        recyclerView = (RecyclerView)findViewById(R.id.notification_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        bookingArrayList = new ArrayList<>();
        usersValidationArrayList = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(getApplicationContext(),  this);
        userValidationAdapter = new UserValidationAdapter(getApplicationContext(), this);
        //postingArrayList.clear();
        if (isAdmin){
            getData();
            //add Adapter to RecyclerView
            recyclerView.setAdapter(notificationAdapter);
        }else {
            getUserValidation();
            //add Adapter to RecyclerView
            recyclerView.setAdapter(userValidationAdapter);
        }

    }

    private void getUserValidation() {
        databaseReference = FirebaseDatabase.getInstance().getReference("user_validation");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersValidationArrayList.clear();

                UsersValidation usersValidation = new UsersValidation();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                    usersValidation = snapshot.child(auth.getUid())
                            .getValue(UsersValidation.class);
                    Log.i(TAG, "User Name = " + usersValidation.getUserName());
                    usersValidationArrayList.add(usersValidation);
                }

                //posting size
                if (usersValidationArrayList.size() == 0) {
                    emptyMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    //init list data to adapter
                    userValidationAdapter.setUserValidationData(usersValidationArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("booking");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingArrayList.clear();

                Booking booking = new Booking();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                    booking = snapshot.getValue(Booking.class);

                    bookingArrayList.add(booking);
                }

                //posting size
                if (bookingArrayList.size() == 0) {
                    emptyMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    //init list data to adapter
                    notificationAdapter.setBookingData(bookingArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(Booking dataPosition) {
        Bundle bundle = new Bundle();

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(dataPosition);
        //put parcelable
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), bookingList);
        //send data via intent
        Intent intent = new Intent(getApplicationContext(), NotificationDetail.class);
        intent.putExtras(bundle);
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }

    @Override
    public void onClick(UsersValidation dataPosition) {
        Bundle bundle = new Bundle();

        ArrayList<UsersValidation> usersValidationArrayList = new ArrayList<>();
        usersValidationArrayList.add(dataPosition);
        //put parcelable
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), usersValidationArrayList);
        //send data via intent
        Intent intent = new Intent(getApplicationContext(), NotificationDetail.class);
        intent.putExtras(bundle);
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }
}
