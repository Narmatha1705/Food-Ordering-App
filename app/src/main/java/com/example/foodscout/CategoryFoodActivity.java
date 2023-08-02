package com.example.foodscout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryFoodActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    ImageView back;
    SearchView search;
    private List<CategoryModel> lastSearches;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    Query query,itemQuery;
    RecyclerView dataList;
    List<ItemsListModel> itemsListModel;
    List<CategoryModel>categoryList;
    ItemsListAdapter itemAdapter;
    List<CategoryModel> titles;
    List<CategoryModel> images;
    private MaterialSearchBar materialSearchBar;
    CategoryAdapter adapter;
    String name;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_food);

        itemsListModel=new ArrayList<ItemsListModel>();
        list=new ArrayList<>();
        //search=findViewById(R.id.search);
        back= (ImageView) findViewById(R.id.back);
        dataList=(RecyclerView)findViewById(R.id.dataList);
        firestore=(FirebaseFirestore)FirebaseFirestore.getInstance();
        categoryList=new ArrayList<CategoryModel>();
        final AutocompleteSessionToken token=AutocompleteSessionToken.newInstance();
        materialSearchBar=findViewById(R.id.searchBar);


        itemQuery = firestore
                .collection("Items");

        query = firestore
                .collection("Category")
                .orderBy("Category");
              //.limit(50);
       // FirestoreRecyclerOptions<CategoryModel> options= new FirestoreRecyclerOptions.Builder<CategoryModel>().setQuery(query,CategoryModel.class).build();

       // storageReference= (StorageReference) FirebaseStorage.getInstance().getReference();

        //titles=new ArrayList<CategoryModel>();
        //images=new ArrayList<CategoryModel>();

       //GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
       //dataList.setLayoutManager(gridLayoutManager);
       // dataList.setHasFixedSize(true);
        //dataList.setAdapter(adapter);

        //adapter=new CategoryAdapter(categoryList,this);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null) {

                        for (QueryDocumentSnapshot querySnapshot : snapshot) {
                            categoryList.add(new CategoryModel(querySnapshot.getString("Category Image"), querySnapshot.getString("Category")));
                        }
                        adapter = new CategoryAdapter(CategoryFoodActivity.this, categoryList,CategoryFoodActivity.this);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryFoodActivity.this, 2, GridLayoutManager.VERTICAL, false);
                        dataList.setLayoutManager(gridLayoutManager);
                        dataList.setHasFixedSize(true);
                        dataList.setAdapter(adapter);

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
                        Toast.makeText(CategoryFoodActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
      //  loadFromFirestore();




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

                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryFoodActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });




    }

    @Override
    public void onItemClick(ItemsListModel position) {
        //Intent categoryIntent = new Intent(this, ItemsListActivity.class);
      //  categoryIntent.putExtra("CategoryName",name);
      //  startActivity(categoryIntent);
    }

    @Override
    public void onButtonClick(ItemsListModel position) {

    }

    public void getItemName(String name){
        this.name=name;
    }
}
