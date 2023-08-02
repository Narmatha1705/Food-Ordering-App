package com.example.foodscout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddToCartActivity extends AppCompatActivity {
    TextView title, image, price,priceVal,text;
    Button proceed_checkout,update,cancel;
    ImageView back;
    Query query, merger;
    RecyclerView dataList;
    List<AddToCartModel> cartList;
    ArrayList<String>Value;
    AddToCartAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth user;
    String titles,pics,q;
    Double prices;
    int a,b,count=0;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cart);

        dataList = (RecyclerView) findViewById(R.id.dataListCart);
        priceVal=(TextView)findViewById(R.id.price_value);
        back=(ImageView) findViewById(R.id.back);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe);
        Value=new ArrayList<>();
        proceed_checkout=(Button)findViewById(R.id.checkout_proceed);

        //prices = getIntent().getDoubleExtra("Price",0.0d);
        firestore = FirebaseFirestore.getInstance();

        cartList=new ArrayList<AddToCartModel>();
        //adapter=new AddToCartAdapter();
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(AddToCartActivity.this, 1, GridLayoutManager.VERTICAL, false);
       // dataList.setLayoutManager(gridLayoutManager);
        //dataList.setAdapter(adapter);

        prices=0.0;
        user = FirebaseAuth.getInstance();


        if(user.getCurrentUser()!=null) {
            final FirebaseUser userSignedIn = user.getCurrentUser();
            String uid = user.getUid();
            query = firestore.collection("Users").document(uid).collection("Added_Items_On_Cart");

        //if (userSignedIn != null) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        for (QueryDocumentSnapshot querySnapshot : snapshot) {
                            cartList.add(new AddToCartModel(querySnapshot.getString("Image"), querySnapshot.getString("Item"),querySnapshot.getDouble("Price"),querySnapshot.getDouble("Quantity")));

                            prices=prices+(querySnapshot.getDouble("Price"));
                        }
                        adapter = new AddToCartAdapter(AddToCartActivity.this, cartList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddToCartActivity.this, 1, GridLayoutManager.VERTICAL, false);
                        dataList.setLayoutManager(gridLayoutManager);
                        dataList.setAdapter(adapter);
                        dataList.setHasFixedSize(true);
                        adapter.notifyDataSetChanged();
                        priceVal.setText(String.valueOf(prices));
                    }
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AddToCartActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        }else{
            Toast.makeText(AddToCartActivity.this, "You are required to first login", Toast.LENGTH_SHORT).show();
        }

        proceed_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddToCartActivity.this, UserCheckoutPayProcess.class );
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AddToCartActivity.this, HomePage.class );
               // startActivity(intent);
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(AddToCartActivity.this, AddToCartActivity.class );
                startActivity(intent);
                finish();
            }
        });

    }

    public  void showDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(AddToCartActivity.this);
        View view= getLayoutInflater().inflate(R.layout.update_qty_dialog,null);
        builder.setView(view);

        Button incc=(Button) view.findViewById(R.id.increment);
        Button decc=(Button) view.findViewById(R.id.decrement);
        final TextView textt=(TextView) view.findViewById(R.id.counter);
        Button cconfm=(Button) view.findViewById(R.id.confm);

        final AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        cconfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
}
}



