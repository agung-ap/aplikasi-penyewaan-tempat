package id.developer.arrif.kosdankontrakan;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private final int splash_display_length = 3000;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            //splash screen in 3 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentUserLogin(auth);
                }
            },splash_display_length);

        }else {
            //splash screen in 3 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },splash_display_length);
        }
    }

    private void getCurrentUserLogin(final FirebaseAuth auth) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("users")
                        .child(auth.getUid())
                        .child("users_data")
                        .child("status").getValue(String.class);
                if (status.equals("admin")){
                    Log.i("tag", "info : " + status);

                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(new Intent(getApplicationContext(), HomeActivity.class));
                    intent.putExtra("isAdmin", true);
                    startActivity(intent);
                    finish();
                }else {

                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(new Intent(getApplicationContext(), HomeActivity.class));
                    intent.putExtra("isAdmin", false);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
