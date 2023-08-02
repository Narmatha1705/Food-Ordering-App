package com.example.foodscout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity; import
        androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel; import android.app.NotificationManager; import
        android.media.RingtoneManager; import android.os.Build;
import android.os.Bundle; import android.view.View; import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserCheckoutPayProcess extends AppCompatActivity {
    private static final String TAG = "UserCheckoutPayProcess";
    TextView paymentheader, address;
    ImageView imgtry,back;
    ImageButton onlinePay;
    Button viewMap,order;
    FirebaseFirestore fs;
    FirebaseAuth user;
    FirebaseUser userSignedIn;
    DocumentReference ref;
    Query data;
    Double price=0.0;
    String Location,Address,Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_checkout_pay_process);

        //paymentheader=(TextView) findViewById(R.id.payment_header);
        address=(TextView) findViewById(R.id.address_input);
        viewMap=(Button) findViewById(R.id.address_set);
        //onlinePay=(ImageButton) findViewById(R.id.cashOnDeliveryButton);
        back=(ImageView)findViewById(R.id.back);
        order=(Button)findViewById(R.id.order);
        create();


        //imgtry=(ImageView) findViewById(R.id.trialimg);
        fs=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();
        userSignedIn = user.getCurrentUser();
        String uid = user.getUid();

        ref= fs.collection("Users").document(uid).collection("User_Delivery_Address").document("Delivery_Address");

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        Location = snapshot.getString("Location");
                        Address = snapshot.getString("AddressLine1");
                        Area = snapshot.getString("Area");

                    }
                    address.setText(Location+","+Address+","+Area);

                } else {
                    Toast.makeText(UserCheckoutPayProcess.this, "No Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
       onlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View view= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.online_payment_dialog,null);

                builder.setView(view);

               EditText cardNumber=view.findViewById(R.id.cardNumberInput);
               String cardNumStr=cardNumber.getText().toString();
               Button pay=view.findViewById(R.id.pay);


                final AlertDialog alertDialog=builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

              /*  if (TextUtils.isEmpty(cardNumStr)) {
                    cardNumber.setError("Card Number is Required");
                    return;
                }

                if(cardNumStr.length()<5){
                    cardNumber.setError("Card number must be >= 5.");
                    return;
                }

                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String BASE_URL="http://192.168.0.107/credit_retrieve/credit_card_retrieve.php";

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array=new JSONArray(response);
                                    for(int i=0;i<array.length();i++){
                                        JSONObject object=array.getJSONObject(i);
                                        int id=object.getInt("id");
                                        String card_num=object.getString("Card Number");
                                        Double amount=object.getDouble("Amount");

                                        if(card_num.equals(cardNumStr)){
                                            Toast.makeText(view.getContext(), "Valid Number", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(view.getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }catch (Exception e){
                                    Toast.makeText(view.getContext(), "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                                
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(view.getContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                        requestQueue.add(stringRequest);
                    }
                });

                alertDialog.show();

            }

        });

*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCheckoutPayProcess.this, PermissionMapActivity.class );
                startActivity(intent);
            }
        });
//        order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserCheckoutPayProcess.this, OrderDone.class );
//                startActivity(intent);
//
//            }
//        });

       /* data.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        for (QueryDocumentSnapshot querySnapshot : snapshot) {
                            //String trynameValue=trynam.toString();
                           // String trycatValue=trycat.toString();
                            //String imgtryValue=imgtry.toString();
                            //trynameValue.add(new String(querySnapshot.getString()))
                            Log.d(TAG, querySnapshot.getId() + " => " + querySnapshot.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                        }

                    }
                });*/
            }
            public void create(){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "ch1";
                        String description = "cd1";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel("123", name, importance);
                        channel.setDescription(description);
                        NotificationManager notificationManager =
                                getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

            }
    public void show(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123");
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Message from Notification App")
                .setContentText("Hi it's me Deepak!")
                .setSound(RingtoneManager.getActualDefaultRingtoneUri(this,
                        RingtoneManager.TYPE_NOTIFICATION));
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(123, builder.build());
        Toast.makeText(getApplicationContext(), "Notification sended",
                Toast.LENGTH_LONG).show();
    }
}




