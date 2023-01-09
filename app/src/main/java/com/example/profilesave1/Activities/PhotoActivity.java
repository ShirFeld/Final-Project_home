package com.example.profilesave1.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.profilesave1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PhotoActivity extends AppCompatActivity {

    /*
    This activity gives the options to upload a picture from user device to the app.
     */

    // request code for a method (onActivityResult)
    private final int REQUEST_CODE = 22;
    // views for button
    private Button btnSelect, btnUpload ;
    // view for image view
    private ImageView imageView;
    // Uri indicates, where the image will be picked from
    private Uri imageUri;

    // instance for firebase storage and database
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference users;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // initialise views
        mAuth = FirebaseAuth.getInstance(); // the accesses to the user
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }
    // Select Image method
    private void SelectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser( intent,"Select Image from here..."), REQUEST_CODE);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        // checking request code and result code
        // if request code is REQUEST_CODE (22) and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Get the Uri of data
            imageUri = data.getData();          // here we get the image
            imageView.setImageURI(imageUri);    // here we put the image in our var

        }
    }

    // UploadImage method
    private void uploadImage(){
        if (imageUri != null) {
            uploadToFirebase(imageUri);
        }
        else
            Toast.makeText(PhotoActivity.this , "Please select an image" , Toast.LENGTH_SHORT).show();
    }

    private void uploadToFirebase(Uri uri) {
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        FirebaseUser currentUser = mAuth.getCurrentUser(); // gives the user

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       users.child(currentUser.getUid()).child("url").setValue(uri.toString()); // put the link from storage to realtime db
                       // progressBar.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                        Toast.makeText(PhotoActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();  // Returns us to profile page
                    }
                });
            }
            // shows the percentage of the uploading od the picture
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(
                    UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress  = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "  + (int)progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // progressBar.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
                Toast.makeText(PhotoActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // related to the ending of the image
    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }


}