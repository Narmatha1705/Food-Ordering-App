package com.example.foodscout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InsertItem extends AppCompatActivity {
    Button upload, choose, logout;
    EditText itmname, categname,price,description,exclusive;
    ImageView imgCatg;
    StorageReference storageRef;
    FirebaseFirestore fStoreRef;
    Collection colRef;
    //FirebaseAuth fAuth;
    private Uri imageUrl;
    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_items);

        itmname = (EditText) findViewById(R.id.Itemnm);
        imgCatg = (ImageView) findViewById(R.id.itemImg);
        categname = (EditText) findViewById(R.id.inx);
        description = (EditText) findViewById(R.id.description);
        exclusive = (EditText) findViewById(R.id.exclusive);
        price = (EditText) findViewById(R.id.price);
        upload = (Button) findViewById(R.id.itStore);
        choose = (Button) findViewById(R.id.seltimg);
        logout = (Button) findViewById(R.id.logout);
        storageRef = FirebaseStorage.getInstance().getReference();
        fStoreRef = (FirebaseFirestore) FirebaseFirestore.getInstance();
      //  String Catg = categname.getText().toString();

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
            imageUrl = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUrl);
                imgCatg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (imageUrl != null) {
            final ProgressDialog progressDialog = new ProgressDialog(InsertItem.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageRef.child("items/"+ UUID.randomUUID().toString());
            ref.putFile(imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(InsertItem.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // StorageReference imageUri =ref;
                                    Uri downloadUrl = uri;
                                    String fileUrl = downloadUrl.toString();
                                    final String titleValue = itmname.getText().toString();
                                    final String categoryValue = categname.getText().toString();
                                    final Double priceValue = Double.parseDouble(String.valueOf(price.getText()));
                                    final String descriptionValue = description.getText().toString();
                                    final int exclusiveValue = Integer.parseInt(exclusive.getText().toString());
                                    String id=fStoreRef.collection("Items").document().getId();
                                    DocumentReference df=fStoreRef.collection("Items").document(id);
                                    Map<String,Object> categoryInfo =new HashMap<>();
                                    categoryInfo.put("Item Image",fileUrl);
                                    categoryInfo.put("Item",titleValue);
                                    categoryInfo.put("Category",categoryValue);
                                    categoryInfo.put("Price",priceValue);
                                    categoryInfo.put("Description",descriptionValue);
                                    categoryInfo.put("isExclusive",exclusiveValue);
                                    df.set(categoryInfo);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(InsertItem.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    }

}


