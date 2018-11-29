package id.developer.arrif.kosdankontrakan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.model.Users;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.nama)
    EditText nama;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.telp)
    EditText telp;
    @BindView(R.id.registrasi)
    Button registrasi;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //init firebase auth
        auth = FirebaseAuth.getInstance();
        //register button
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle register process
                handleRegister();
            }
        });
    }

    private void handleRegister() {
        final String inputNama = nama.getText().toString().trim();
        final String inputTelp = telp.getText().toString().trim();
        final String inputEmail = email.getText().toString().trim();
        final String inputPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail)) {
            email.setError("Enter email address!");
            return;
        }
        if (TextUtils.isEmpty(inputPassword)) {
            password.setError("Password Tidak boleh kosong");
            return;
        }
        if (inputPassword.length() < 6) {
            password.setError("password kurang dari 6 karakter");
            return;
        }

        //show progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        //start register on firebase
        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //when failed on register
                            //hide progressbar
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Failed : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //when success on register
                            String inputUid = task.getResult().getUser().getUid();
                            Users users = new Users();
                            users.setNama(inputNama);
                            users.setTelp(inputTelp);
                            users.setStatus("user");
                            //input user data to database users
                            createUser(inputUid, inputEmail, inputPassword, users);
                        }
                    }
                });
    }

    private void createUser(String inputUid, String inputEmail, String inputPassword, Users users) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("users");
        databaseReference.child(inputUid).child("email").setValue(inputEmail);
        databaseReference.child(inputUid).child("password").setValue(inputPassword);
        databaseReference.child(inputUid).child("users_data").setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String status = dataSnapshot.child("users")
                                    .child(auth.getUid())
                                    .child("users_data")
                                    .child("status").getValue(String.class);
                            if (status.equals("admin")){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(new Intent(getApplicationContext(), HomeActivity.class));
                                intent.putExtra("isAdmin", true);
                                startActivity(intent);
                                finish();
                            }else {
                                progressDialog.dismiss();
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
        });
    }
}
