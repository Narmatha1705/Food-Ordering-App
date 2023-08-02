package com.example.foodscout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemsListActivity extends AppCompatActivity implements RecyclerViewClickInterface, PopupMenu.OnMenuItemClickListener {
    Toolbar toolbar;
    TextView title;
    ImageView back;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    Query query,merger,itemQuery;
    private MaterialSearchBar materialSearchBar;
    ItemsListAdapter itemAdapter;
    List<ItemsListModel> itemsListModel;
    RecyclerView dataList;
    List<ItemsListModel> itemsList;
    ItemsListAdapter adapter;
    String titles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);

        title = findViewById(R.id.items_name);
        dataList = (RecyclerView) findViewById(R.id.dataListSub);
        back = (ImageView) findViewById(R.id.back);
        firestore = (FirebaseFirestore) FirebaseFirestore.getInstance();
        itemsList = new ArrayList<ItemsListModel>();
        materialSearchBar=findViewById(R.id.searchBar);
        itemsListModel=new ArrayList<ItemsListModel>();


        toolbar = (Toolbar) findViewById(R.id.toolbar_items);
        setSupportActionBar(toolbar);
        titles = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(titles);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(titles);

        //merger = firestore.collection("Category");

        itemQuery = firestore
                .collection("Items");


        query = firestore
                .collection("Items");

      query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
              if (task.isSuccessful()) {
                  QuerySnapshot snapshot = task.getResult();
                  if (snapshot != null) {
                      for (QueryDocumentSnapshot querySnapshot : snapshot) {
                          String var=querySnapshot.getString("Category");
                          assert var != null;
                          if(var.equals(titles)) {
                              itemsList.add(new ItemsListModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"), querySnapshot.getDouble("Price"),querySnapshot.getString("Description")));
                          }
                      }
                      adapter = new ItemsListAdapter(ItemsListActivity.this, itemsList,  ItemsListActivity.this,ItemsListActivity.this);
                      GridLayoutManager gridLayoutManager = new GridLayoutManager(ItemsListActivity.this, 1, GridLayoutManager.VERTICAL, false);
                      dataList.setLayoutManager(gridLayoutManager);
                      dataList.setHasFixedSize(true);
                      dataList.setAdapter(adapter);
                     // adapter.notifyDataSetChanged();

                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              adapter.showShimmer=false;
                              adapter.notifyDataSetChanged();
                          }
                      },1000);

                  }
              } else {
                  String error = task.getException().getMessage();
                  Toast.makeText(ItemsListActivity.this, error, Toast.LENGTH_SHORT).show();
              }
          }
      });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text.toString(),true,null,true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

               /* itemQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null) {
                                for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                    // String var = querySnapshot.getString("Category");
                                    // assert var != null;

                                    itemsListModel.add(new ItemsListModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"), querySnapshot.getDouble("Price"), querySnapshot.getString("Description")));
                                }*/
                                //itemAdapter = new ItemsListAdapter(ItemsListActivity.this, itemsListModel, ItemsListActivity.this, ItemsListActivity.this);
                                adapter.getFilter().filter(s);

                          // }
                       // }
                  // }
              // });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void showPopup(View v){
        PopupMenu popupMenu= new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu_popup_sort);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sort_name:
                //Toast.makeText(this, "Name", Toast.LENGTH_SHORT).show();
                dataList = (RecyclerView) findViewById(R.id.dataListSub);
                query = firestore
                        .collection("Items");

                removeItems();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null) {
                                for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                    String var=querySnapshot.getString("Category");
                                    assert var != null;
                                    if(var.equals(titles)) {
                                        itemsList.add(new ItemsListModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"), querySnapshot.getDouble("Price"),querySnapshot.getString("Description")));
                                    }
                                }
                                Collections.sort(itemsList,ItemsListModel.nameSort);
                                adapter = new ItemsListAdapter(ItemsListActivity.this, itemsList,  ItemsListActivity.this,ItemsListActivity.this);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(ItemsListActivity.this, 1, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setHasFixedSize(true);
                                dataList.setAdapter(adapter);
                                // adapter.notifyDataSetChanged();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.showShimmer=false;
                                        adapter.notifyDataSetChanged();
                                    }
                                },1000);

                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ItemsListActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //Collections.sort(itemsList);
                return true;

            case R.id.sort_inc_pri:

                dataList = (RecyclerView) findViewById(R.id.dataListSub);
                query = firestore
                        .collection("Items");

                removeItems();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null) {
                                for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                    String var=querySnapshot.getString("Category");
                                    assert var != null;
                                    if(var.equals(titles)) {
                                        itemsList.add(new ItemsListModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"), querySnapshot.getDouble("Price"),querySnapshot.getString("Description")));
                                    }
                                }
                                Collections.sort(itemsList,ItemsListModel.priceSort);
                                adapter = new ItemsListAdapter(ItemsListActivity.this, itemsList,  ItemsListActivity.this,ItemsListActivity.this);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(ItemsListActivity.this, 1, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setHasFixedSize(true);
                                dataList.setAdapter(adapter);
                                // adapter.notifyDataSetChanged();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.showShimmer=false;
                                        adapter.notifyDataSetChanged();
                                    }
                                },1000);

                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ItemsListActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

               // Toast.makeText(this, "Inc", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.sort_dec_pri:

                dataList = (RecyclerView) findViewById(R.id.dataListSub);
                query = firestore
                        .collection("Items");

                removeItems();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            if (snapshot != null) {
                                for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                    String var=querySnapshot.getString("Category");
                                    assert var != null;
                                    if(var.equals(titles)) {
                                        itemsList.add(new ItemsListModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"), querySnapshot.getDouble("Price"),querySnapshot.getString("Description")));
                                    }
                                }
                                Collections.sort(itemsList,ItemsListModel.DecPriceSort);
                                adapter = new ItemsListAdapter(ItemsListActivity.this, itemsList,  ItemsListActivity.this,ItemsListActivity.this);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(ItemsListActivity.this, 1, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setHasFixedSize(true);
                                dataList.setAdapter(adapter);
                                // adapter.notifyDataSetChanged();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.showShimmer=false;
                                        adapter.notifyDataSetChanged();
                                    }
                                },1000);

                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(ItemsListActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Toast.makeText(this, "Dec", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;

        }

    }

    public void removeItems() {
        itemsList.clear();
        //dataList.getAdapter().notifyDatasetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(ItemsListModel position) {

    }

    @Override
    public void onButtonClick(ItemsListModel position) {

        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
    }


}
