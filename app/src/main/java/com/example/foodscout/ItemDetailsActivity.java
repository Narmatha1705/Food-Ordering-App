package com.example.foodscout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title,price,description,counter;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView img,cart,back;
    FirebaseFirestore firestore;
    FirebaseAuth user;
    StorageReference storageReference;
    Query query,merger,ratg;
    CollectionReference colRef,itemRef;
    DocumentReference ref;
    RecyclerView dataList;
    List<ItemsListModel> itemsList;
    ItemsListAdapter adapter;
    String titles,val,var;
    double prices;
    String pics;
    String descript,b,temp;
    Button addCart,viewcart,increment,decrement;
    List<String> desc;
    int count=0,qty,a;
    RatingBar ratingBar;
    float rateValue;

    //public void increment(View v){

    //}
    //public void decrement (View v){


    //}
     @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.item_details_activity);

        title = findViewById(R.id.items_name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        back=findViewById(R.id.back);
        shimmerFrameLayout=findViewById(R.id.shimmer_frame);
        addCart=findViewById(R.id.addtocart);
        increment=findViewById(R.id.increment);
        decrement=findViewById(R.id.decrement);
        counter=findViewById(R.id.counter);
        //viewcart=findViewById(R.id.view_cart);
        img = findViewById(R.id.item_details);
        cart = findViewById(R.id.cart);
        ratingBar=findViewById(R.id.rating_bar);
        user= FirebaseAuth.getInstance();
        final List<AddToCartModel>itemSame;
        //final FirebaseUser userSignedIn= user.getCurrentUser();
        firestore = (FirebaseFirestore) FirebaseFirestore.getInstance();


        toolbar = (Toolbar) findViewById(R.id.toolbar_items);
        setSupportActionBar(toolbar);
        titles = getIntent().getStringExtra("ItemName");
        prices = getIntent().getDoubleExtra("Price",0.0d);
        pics = getIntent().getStringExtra("Pic");
        descript = getIntent().getStringExtra("Desc");
        getSupportActionBar().setTitle(titles);
        title.setText(titles);
        price.setText("Rs. "+ String.valueOf(prices));
        Glide.with(ItemDetailsActivity.this).load(pics).into(img);
        description.setText(descript);


        //shimmerFrameLayout.stopShimmer();
        //shimmerFrameLayout.setShimmer(null);


        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                value(count);
                b=myVal();
            }

        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count <= 0) {
                    count = 0;
                } else {
                    value(count);
                }
                b=myVal();
            }
        });

         query=firestore.collection("Users");

         //var=titles;

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getCurrentUser()!=null) {
                    final FirebaseUser userSignedIn = user.getCurrentUser();
                    colRef = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");
                    merger = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");
                    int i=Integer.valueOf(b);
                    Double d=Double.valueOf(b);
                    String ch=null;
                    String tit=titles;
                    final int[] flag = {0};
                    if (i==1){
                      /*  merger.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshot = task.getResult();
                                if (snapshot != null) {
                                    for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                       String str;
                                       str=querySnapshot.getString("Item");
                                        if(str.equals (tit)) {
                                            //flag[0]=1;
                                            String id1 = querySnapshot.getId();
                                            ref = colRef.document(id1);
                                            Double a= querySnapshot.getDouble("Quantity")+d;
                                            ref.update("Quantity",a);
                                            ref.update("Price",a*prices);

                                            //cartItemSame(a);
                                            //var= querySnapshot.getString("Item");
                                            //cartItemDifferent(var);
                                         //   return;
                                        }else{
                                           // cartItemSame(d);
                                        }

                                    }
                                }
                            }
                        }
                    });*/
                      /*  if(flag[0]!=1){
                            cartItemSame(d);
                        }*/
                       String id=colRef.document().getId();
                        final DocumentReference df = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart").document(id);
                        final Map<String, Object> userItemsInfo = new HashMap<>();
                        userItemsInfo.put("Item", titles);
                        userItemsInfo.put("Price", prices);
                        userItemsInfo.put("Image", pics);
                        userItemsInfo.put("Quantity", Integer.valueOf(b));
                        df.set(userItemsInfo);

                    }
                    else{
                        prices=prices*i;
                        String id=colRef.document().getId();
                        final DocumentReference df = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart").document(id);
                        final Map<String, Object> userItemsInfo = new HashMap<>();
                        userItemsInfo.put("Item", titles);
                        userItemsInfo.put("Price", prices);
                        userItemsInfo.put("Image", pics);
                        userItemsInfo.put("Quantity", Integer.valueOf(b));
                        df.set(userItemsInfo);
                    }
                //if(userSignedIn != null){
                  /*  merger.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshot = task.getResult();
                                if (snapshot != null) {
                                    for (QueryDocumentSnapshot querySnapshot : snapshot) {

                                        if(!titles.equals(querySnapshot.getString("Item"))) {
                                            String id1 = querySnapshot.getId();
                                            ref = colRef.document(id1);
                                            Double a= querySnapshot.getDouble("Quantity")+Integer.valueOf(b);
                                            //cartItemSame(a);
                                            var= querySnapshot.getString("Item");
                                            cartItemDifferent(var);
                                        }

                                    }
                                }
                            }
                        }
                    }); */
                    //df.set(userItemsInfo);
                    String a=var;
                    //cartItemDifferent(a);
                    Toast.makeText(ItemDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(ItemDetailsActivity.this);
                    builder.setTitle("Do you want order more?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ItemDetailsActivity.this, CategoryFoodActivity.class);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ItemDetailsActivity.this, HomePage.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert=builder.create();
                    alert.show();
                }
                else{
                    Toast.makeText(ItemDetailsActivity.this, "You are required to first login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getCurrentUser()!=null) {
                    Intent intent = new Intent(ItemDetailsActivity.this, AddToCartActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ItemDetailsActivity.this, "You are required to first login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(user.getCurrentUser()!=null){
            ratg = firestore.collection("Items");
            itemRef = firestore.collection("Items");

            ratg.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null) {
                            for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                String str;
                                str=querySnapshot.getString("Item");
                                if(str.equals (titles)) {
                                    //flag[0]=1;
                                    String id = querySnapshot.getId();
                                    ref = itemRef.document(id);

                                    DocumentReference rate=firestore.collection("Items").document(id).collection("Users_Rating").document(user.getUid());

                                    rate.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot snapshot = task.getResult();
                                                //if (snapshot != null) {
                                                    if(snapshot.exists()) {
                                                        Double rateValue = snapshot.getDouble("Rating");
                                                        ratingBar.setRating(rateValue.floatValue());
                                                        shimmerFrameLayout.stopShimmer();
                                                        shimmerFrameLayout.setShimmer(null);
                                                    }else{
                                                        shimmerFrameLayout.stopShimmer();
                                                        shimmerFrameLayout.setShimmer(null);
                                                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                            @Override
                                                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                                rateValue = ratingBar.getRating();
                                                                String message = null;
                                                                final FirebaseUser userSignedIn= user.getCurrentUser();

                                                                switch ((int) rating) {
                                                                    case 1:
                                                                        message = "Sorry,we will consider your feedback for improvement";
                                                                        break;
                                                                    case 2:
                                                                        message = "Thanks";
                                                                        break;
                                                                    case 3:
                                                                        message = "Satisfactory";
                                                                        break;
                                                                    case 4:
                                                                        message = "Great";
                                                                        break;
                                                                    case 5:
                                                                        message = "Superb";
                                                                        break;
                                                                }
                                                                merger = firestore.collection("Items");
                                                                ratg = firestore.collection("Items");
                                                                //rating=firestore.collection("Items").document(id).collection("Users_Rating");
                                                                itemRef = firestore.collection("Items");
                                                                //String id=itemRef.document().getId();

                                                                merger.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            QuerySnapshot snapshot = task.getResult();
                                                                            if (snapshot != null) {
                                                                                for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                                                                    String str;
                                                                                    str=querySnapshot.getString("Item");
                                                                                    if(str.equals (titles)) {
                                                                                        //flag[0]=1;
                                                                                        String id = querySnapshot.getId();
                                                                                        ref = itemRef.document(id);
                                                                                        DocumentReference df=firestore.collection("Items").document(id).collection("Users_Rating").document(user.getUid());
                                                                                        Map<String,Object> userInfo =new HashMap<>();
                                                                                        userInfo.put("Rating",rating);
                                                                                        df.set(userInfo);

                                                                                    }

                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });

                                                                Toast.makeText(ItemDetailsActivity.this, message, Toast.LENGTH_SHORT).show();


                                                            }
                                                        });

                                                    }
                                                //}
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });

        }else{
            ratingBar.setNumStars(5);
        //Toast.makeText(ItemDetailsActivity.this, "You need to login first", Toast.LENGTH_SHORT).show();
    }

       /*  if(user.getCurrentUser()!=null) {

           }else{
            Toast.makeText(ItemDetailsActivity.this, "You need to login first", Toast.LENGTH_SHORT).show();
           }*/
    }

    public void value(int a){
        counter=findViewById(R.id.counter);
        counter.setText("" + a);
        //val=String.valueOf(counter);
    }
    public String myVal(){
        String counterVal=counter.getText().toString();
        //qty=Integer.parseInt(counter.toString());
        return counterVal;
    }

    public void cartItemSame(Double a){
        //Double c=a;
        //Integer.valueOf(querySnapshot.getString("Quantity"))+Integer.valueOf(b);
        //String d=String.valueOf(c);
        //ref.update("Quantity",a);
        colRef = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");
        merger = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");

        String id=colRef.document().getId();
        final DocumentReference df = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart").document(id);
        final Map<String, Object> userItemsInfo = new HashMap<>();
        userItemsInfo.put("Item", titles);
        userItemsInfo.put("Price", prices);
        userItemsInfo.put("Image", pics);
        userItemsInfo.put("Quantity", a);
        df.set(userItemsInfo);
    }

    public void cartItemDifferent(String a) {
        //if(titles.equals(a)){
        String uid = user.getUid();
        String id = firestore.collection("Users").document(uid).collection("Added_Items_On_Cart").document().getId();
        final DocumentReference df = firestore.collection("Users").document(uid).collection("Added_Items_On_Cart").document(id);
        final Map<String, Object> userItemsInfo = new HashMap<>();
        userItemsInfo.put("Item", titles);
        userItemsInfo.put("Price", prices);
        userItemsInfo.put("Image", pics);
        userItemsInfo.put("Quantity", Integer.valueOf(b));
        df.set(userItemsInfo);
    //}

    }


}
