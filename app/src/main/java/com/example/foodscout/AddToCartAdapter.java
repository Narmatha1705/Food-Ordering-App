package com.example.foodscout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AddToCartAdapter  extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {
    List<AddToCartModel> cartList;
    LayoutInflater inflater;
    Context context;
    AddToCartActivity val;
    int count,a;
    CollectionReference colRef;
    DocumentReference ref;
    Query merger,q;
    FirebaseFirestore firestore;
    FirebaseAuth user;
    String counterVal;

    public AddToCartAdapter() {
    }

    public AddToCartAdapter(Context ctx, List<AddToCartModel> cartList) {
        this.cartList = cartList;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public AddToCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.add_to_cart_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String img= cartList.get(position).getImage();
        String name=cartList.get(position).getName();
        Double price=cartList.get(position).getPrice();
        Double qtyy=cartList.get(position).getQty();
        holder.setName(name,position);
        holder.setImage(img,position);
        holder.setPrice(price,position);
        holder.setQty(qtyy,position);

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                View view= LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.update_qty_dialog,null);
                builder.setView(view);

                Button inc=(Button) view.findViewById(R.id.increment);
                Button dec=(Button) view.findViewById(R.id.decrement);
                final TextView text=(TextView) view.findViewById(R.id.counter);
                Button confm=(Button) view.findViewById(R.id.confm);
                user= FirebaseAuth.getInstance();
                firestore = (FirebaseFirestore) FirebaseFirestore.getInstance();

                final AlertDialog alertDialog=builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                text.setText("" + count);
                counterVal=text.getText().toString();
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count <= 0) {
                    count = 0;
                } else {
                    text.setText("" + count);
                }
                counterVal=text.getText().toString();
            }
        });



                confm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user.getCurrentUser()!=null) {
                            colRef = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");
                            merger = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");
                            q=firestore.collection("Items");
                            double d= Double.valueOf(counterVal);

                            merger.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot snapshot = task.getResult();
                                if (snapshot != null) {
                                    for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                       String str;
                                       str=querySnapshot.getString("Item");
                                        if(str.equals (name)) {
                                            //flag[0]=1;
                                            String id1 = querySnapshot.getId();
                                            ref = colRef.document(id1);

                                            q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot snapshot = task.getResult();
                                                        for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                                            String s=querySnapshot.getString("Item");
                                                            if(s.equals(str)){
                                                                Double p=querySnapshot.getDouble("Price");
                                                                Double a= d;
                                                                ref.update("Quantity",a);
                                                                ref.update("Price",a*p);
                                                                Toast.makeText(view.getContext(), "Quantity Updated", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(view.getContext(), AddToCartActivity.class );
                                                                view.getContext().startActivity(intent);
                                                                ((Activity)view.getContext()).finish();
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        }

                                    }
                                }
                            }
                        }
                    });
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= FirebaseAuth.getInstance();
                firestore = (FirebaseFirestore) FirebaseFirestore.getInstance();
                colRef = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");
                merger = firestore.collection("Users").document(user.getUid()).collection("Added_Items_On_Cart");

                merger.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null) {
                                for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                    String str;
                                    str=querySnapshot.getString("Item");
                                    if(str.equals (name)) {
                                        //flag[0]=1;
                                        String id1 = querySnapshot.getId();
                                        ref = colRef.document(id1);

                                        ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(v.getContext(), "Removed from cart", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(v.getContext(), AddToCartActivity.class );
                                                    v.getContext().startActivity(intent);
                                                    ((Activity)v.getContext()).finish();

                                                }else {
                                                    Toast.makeText(v.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }

                                }
                            }
                        }
                    }
                });

        }

        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itm_name,itm_price,qty1;
        ImageView itm_img;
        View v;
        Button update,cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itm_name=(TextView) itemView.findViewById(R.id.itemName);
            itm_price=(TextView)itemView.findViewById(R.id.itemPrice);
            itm_img=(ImageView)itemView.findViewById(R.id.itemImage);
            qty1=(TextView)itemView.findViewById(R.id.qty);
            update=(Button) itemView.findViewById(R.id.update);
            cancel=(Button) itemView.findViewById(R.id.cancel);
        }
        private void setImage(String iconUrl, int position) {
            if(!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).into(itm_img);
            }
        }

        private void setName(final String name, final int position) {
            itm_name.setText(name);
        }
        private void  setPrice(final Double price, final int position) {
            itm_price.setText("Rs. "+String.valueOf(price));
            int totalItems=0;
            double totalItemsPrice=0;
            for(int x=0;x<cartList.size();x++){
                totalItems++;
                totalItemsPrice=totalItemsPrice+price;
            }

        }
        private void setQty(final Double qty, final int position) {
            String s=String.valueOf(qty);
            qty1.setText("Qty. " + String.valueOf(qty));

        }
    }

}
