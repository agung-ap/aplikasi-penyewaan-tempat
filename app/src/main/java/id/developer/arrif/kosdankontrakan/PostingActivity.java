package id.developer.arrif.kosdankontrakan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.arrif.kosdankontrakan.model.Posting;

public class PostingActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 71;

    @BindView(R.id.gambar_tempat_posting)
    ImageView gambarTempat;
    @BindView(R.id.nama_tempat)
    EditText namaTempat;
    @BindView(R.id.alamat_tempat)
    EditText alamatTempat;
    @BindView(R.id.harga_tempat)
    EditText hargaTempat;
    @BindView(R.id.deskripsi_tempat)
    EditText deskripsiTempat;
    @BindView(R.id.posting_button)
    Button posting;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private Uri filePath;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        ButterKnife.bind(this);

        id = FirebaseDatabase.getInstance().getReference("posting")
                .push().getKey();

        gambarTempat.setClickable(true);
        gambarTempat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseImage();
            }
        });
        posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = FirebaseDatabase.getInstance().getReference("posting")
                        .push().getKey();
                uploadingImage(filePath);
            }
        });
    }

    private void choseImage() {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }catch (Exception e){
            Toast.makeText(this, "Exception : " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private Posting data(){
        String inputNamaTempat = namaTempat.getText().toString().trim();
        String inputAlamat = alamatTempat.getText().toString().trim();
        String inputHarga = hargaTempat.getText().toString().trim();
        String inputDeskripsi = deskripsiTempat.getText().toString().trim();

        Posting posting = new Posting();
        posting.setNamaTempat(inputNamaTempat);
        posting.setAlamatTempat(inputAlamat);
        posting.setHargaTempat(inputHarga);
        posting.setDeskripsiTempat(inputDeskripsi);

        return posting;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                gambarTempat.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    private void uploadingImage(Uri filePath){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance()
                .getReference()
                .child("images/"+ UUID.randomUUID().toString());
        storageReference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                //save uri to database
                                setPosting(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
    }

    private void setPosting(String imageUrl){
        databaseReference = FirebaseDatabase.getInstance().getReference("posting");
        databaseReference.child(id)
                .child("imageUrl").setValue(imageUrl);
        databaseReference.child(id)
                .child("posting_data")
                .setValue(data())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(PostingActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
