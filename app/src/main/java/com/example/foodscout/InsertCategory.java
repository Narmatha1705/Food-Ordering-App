package com.example.foodscout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InsertCategory extends AppCompatActivity {

   private Button upload, choose, logout;
   private EditText catg,id;
   private ImageView imgCatg;
   private StorageReference storageRef;
   private FirebaseFirestore fStoreRef;
   private FirebaseAuth fAuth;
   private Uri imageUri;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_category);


        catg = (EditText) findViewById(R.id.name);
        imgCatg = (ImageView) findViewById(R.id.img);
        id = (EditText) findViewById(R.id.catgid);
        upload = (Button) findViewById(R.id.Store);
        choose = (Button) findViewById(R.id.imgSelect);
        logout = (Button) findViewById(R.id.logout);
        storageRef = FirebaseStorage.getInstance().getReference();
        fStoreRef = (FirebaseFirestore) FirebaseFirestore.getInstance();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgCatg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(InsertCategory.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(InsertCategory.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // StorageReference imageUri =ref;
                                    Uri downloadUrl = uri;
                                    String fileUrl = downloadUrl.toString();
                                    final String titleValue = catg.getText().toString();
                                    final String idValue = id.getText().toString();
                                    String id=fStoreRef.collection("Category").document().getId();
                                    DocumentReference df=fStoreRef.collection("Category").document(id);
                                    Map<String,Object> categoryInfo =new HashMap<>();
                                    categoryInfo.put("Category Image",fileUrl);
                                    categoryInfo.put("Category",titleValue);
                                    categoryInfo.put("index",idValue);
                                    df.set(categoryInfo);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(InsertCategory.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(InsertCategory.this, InsertItem.class);
        startActivity(intent);
        finish();

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(InsertCategory.this, "You are logged out now", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}