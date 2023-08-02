package com.example.foodscout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements RecyclerViewClickInterface, NavigationView.OnNavigationItemSelectedListener {

    SliderView sliderView;
    List<ImageSliderModel> imageSliderModelList;
    ImageSliderAdapter adapt;
    ShimmerFrameLayout shimmerFrameLayout;

    //MaterialSearchBar materialSearchBar;
    // HomeExclusiveAdapter customSuggestionsAdapter;

    LayoutInflater inflater ;
    Toolbar toolbar;

    List<ItemsListModel> suggestions;
    List<String>list;
    List<Double>listPrice;
    List<String>listDescription;
    List<String>listPic;
    ArrayAdapter<String>adapter;
    ItemsListAdapter itemAdapter;

    List<HomeExclusiveModel>listExclusive;
    HomeExclusiveAdapter homeExclusiveAdapter;
    RecyclerView lstExclusiveRecycle;

    AutoCompleteTextView autoCompleteTextView;

    String img,des,item;
    Double p ;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth user;
    Query query,exclusiveQuery;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Button logout,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        imageSliderModelList=new ArrayList<>();
        shimmerFrameLayout=findViewById(R.id.shimmer_frame);
        logout = (Button) findViewById(R.id.logout);
        //materialSearchBar=findViewById(R.id.searchBar);
        //autoCompleteTextView=findViewById(R.id.autoComplete);
        category = (Button) findViewById(R.id.category) ;
        list=new ArrayList<>();
        listPrice=new ArrayList<>();
        listDescription=new ArrayList<>();
        listPic=new ArrayList<>();



        sliderView=(SliderView) findViewById(R.id.imageSlider);

        listExclusive=new ArrayList<HomeExclusiveModel>();
        lstExclusiveRecycle=(RecyclerView)findViewById(R.id.recycle_exclusive);

        firebaseFirestore=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();
        final FirebaseUser userSignedIn = user.getCurrentUser();
        String uid = user.getUid();

        inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        suggestions=new ArrayList<>();

        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_home);
        navigationView=findViewById(R.id.nav_view);



        /*materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(),true,null,true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode==MaterialSearchBar.BUTTON_NAVIGATION){

                }else if(buttonCode==MaterialSearchBar.BUTTON_BACK){
                    materialSearchBar.disableSearch();
                }
            }
        });*/


        //MenuSearchQuery
        query = firebaseFirestore
                .collection("Items");


        //ExclusiveItemQuery
        exclusiveQuery=firebaseFirestore.collection("Items");

        /*materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //materialSearchBar.showSuggestionsList();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


        //NavigationDrawer
        Menu menu1=navigationView.getMenu();
       if (userSignedIn != null) {
            menu1.findItem(R.id.nav_login).setVisible(false);
            menu1.findItem(R.id.nav_register).setVisible(false);
        }else {
        menu1.findItem(R.id.nav_logout).setVisible(false);
        menu1.findItem(R.id.nav_profile).setVisible(false);
        }

       //Admin Panel
        if(userSignedIn!=null) {
            DocumentReference df = firebaseFirestore.collection("Users").document(uid);
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Menu menu1 = navigationView.getMenu();
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.get("isAdmin") != null) {
                                menu1.findItem(R.id.nav_cart).setVisible(false);
                                menu1.findItem(R.id.nav_orders).setVisible(false);
                            } else {
                                menu1.findItem(R.id.nav_admin).setVisible(false);
                            }
                        }
                    }
                }
            });
        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(HomePage.this,drawerLayout,toolbar,R.string.nav_draw_open,R.string.nav_draw_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //Slider

        adapt=new ImageSliderAdapter(HomePage.this,imageSliderModelList);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setShimmer(null);

                imageSliderModelList.add(new ImageSliderModel(R.mipmap.biryani1));
                imageSliderModelList.add(new ImageSliderModel(R.mipmap.burger1));
                imageSliderModelList.add(new ImageSliderModel(R.mipmap.desert1));
                imageSliderModelList.add(new ImageSliderModel(R.mipmap.chowmein1));

                sliderView.setSliderAdapter(new ImageSliderAdapter(HomePage.this,imageSliderModelList));
            }
        },3000);

        //ExclusiveItems
        exclusiveQuery.whereEqualTo("isExclusive",1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        for (QueryDocumentSnapshot querySnapshot : snapshot) {
                                listExclusive.add(new HomeExclusiveModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"),querySnapshot.getDouble("Price"),querySnapshot.getString("Description")));
                        }
                        homeExclusiveAdapter=new HomeExclusiveAdapter(HomePage.this,listExclusive);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
                        lstExclusiveRecycle.setLayoutManager(layoutManager);
                        lstExclusiveRecycle.setHasFixedSize(true);
                        lstExclusiveRecycle.setAdapter(homeExclusiveAdapter);
                        //homeExclusiveAdapter.notifyDataSetChanged();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                homeExclusiveAdapter.showShimmer=false;
                                homeExclusiveAdapter.notifyDataSetChanged();
                            }
                        },2000);


                    }
                }
            }
        });


    }
/*private void searchView(){
        SearchView searchView=(SearchView)findViewById(R.id.searchViewHome);

}*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        autoCompleteTextView= (AutoCompleteTextView) menuItem.getActionView();
        autoCompleteTextView.setHint("Search Items");
        autoCompleteTextView.setDropDownWidth(600);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot != null) {
                        for (QueryDocumentSnapshot querySnapshot : snapshot) {
                           // suggestions.add(new ItemsListModel(querySnapshot.getString("Item"), querySnapshot.getString("Item Image"), querySnapshot.getDouble("Price"),querySnapshot.getString("Description")));
                            //String s=suggestions.add(new HomeExclusiveModel(querySnapshot.getString("Item")));
                            list.add( querySnapshot.getString("Item"));
                            listPic.add(querySnapshot.getString("Item Image"));
                            listPrice.add(querySnapshot.getDouble("Price"));
                            listDescription.add(querySnapshot.getString("Description"));
                        }
                        //customSuggestionsAdapter = new HomeExclusiveAdapter(HomePage.this, suggestions,  inflater);
                        adapter=new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1,list);
                        autoCompleteTextView.setAdapter(adapter);
                        //itemAdapter = new ItemsListAdapter(HomePage.this, suggestions,  HomePage.this,HomePage.this);
                        //autoCompleteTextView.setThreshold(1);
                        //customSuggestionsAdapter.setSuggestions(suggestions);
                        //materialSearchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);

                    }
                }
            }
        });

    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // final Double p ;
        //String img,d;

        int i=adapter.getPosition(adapter.getItem(position));
       // intent.putExtra("ItemName",adapter.getItem(position));
        Query sug=firebaseFirestore.collection("Items");
        sug.whereEqualTo("Item",adapter.getItem(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot querySnapshot : snapshot) {
                        item=querySnapshot.getString("Item");
                        p = querySnapshot.getDouble("Price");
                       img = querySnapshot.getString("Item Image");
                       des = querySnapshot.getString("Description");
                       getIntent(item,img,p,des);
                    }
                }
            }
        });

    }
});

        return true;
    }
    public void getIntent(String n,String i,Double p,String d){
        final Intent intent = new Intent(autoCompleteTextView.getContext(),ItemDetailsActivity.class );
        intent.putExtra("ItemName",n);
        intent.putExtra("Pic", i);
        intent.putExtra("Price", p);
        intent.putExtra("Desc", d);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }



    @Override
    public void onItemClick(ItemsListModel position) {

    }

    @Override
    public void onButtonClick(ItemsListModel position) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_category:
                Intent intent = new Intent(HomePage.this, CategoryFoodActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomePage.this, "You are logged out now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
            case R.id.nav_register:
                Intent intentRegister = new Intent(HomePage.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.nav_cart:
                user = FirebaseAuth.getInstance();
                final FirebaseUser userSignedIn = user.getCurrentUser();
                if (userSignedIn != null) {
                    Intent intentCart = new Intent(HomePage.this, AddToCartActivity.class );
                    startActivity(intentCart);
                }else{
                    Toast.makeText(HomePage.this, "You are not logged in", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_login:
                Intent intentLogin = new Intent(HomePage.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.nav_admin:
                Intent intentAdmin = new Intent(HomePage.this, AdminFrontpage.class);
                startActivity(intentAdmin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}