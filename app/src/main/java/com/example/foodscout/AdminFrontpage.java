package com.example.foodscout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminFrontpage  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth user;
    private Toolbar toolbar;
    private Button ins_catg,ins_item,del_catg,del_item,upd_catg,upd_item;
    private FirebaseFirestore fStoreRef;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_frontpage);

        ins_catg=(Button) findViewById(R.id.ins_category);
        ins_item=(Button) findViewById(R.id.ins_item);
        del_catg=(Button) findViewById(R.id.del_category);
        del_item=(Button) findViewById(R.id.del_item);
        upd_catg=(Button) findViewById(R.id.update_category);
        upd_item=(Button) findViewById(R.id.update_item);
        toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_home);
        navigationView=findViewById(R.id.nav_view);
        //toolbar.showOverflowMenu();
        fStoreRef = (FirebaseFirestore) FirebaseFirestore.getInstance();


        Menu menu1=navigationView.getMenu();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_draw_open,R.string.nav_draw_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ins_catg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminFrontpage.this,InsertCategory.class);
                startActivity(intent);
            }
        });

        ins_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminFrontpage.this,InsertItem.class);
                startActivity(intent);
            }
        });

    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_frontpage_adminstatus, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        //MenuItem menuItem=menu.findItem(R.id.account);
        //invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
                case R.id.action_login:
                Toast.makeText(this,"You are Logged in",Toast.LENGTH_SHORT).show();
                return true;
                case R.id.action_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(AdminFrontpage.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "You are Logged out", Toast.LENGTH_SHORT).show();
                    return true;

        }
        return super.onOptionsItemSelected(item);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(AdminFrontpage.this, HomePage.class);
                startActivity(intent);
                break;
          /*  case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomePage.this, "You are logged out now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;

           */
            case R.id.nav_category:
                Intent intentCategory = new Intent(AdminFrontpage.this, CategoryFoodActivity.class);
                startActivity(intentCategory);
                break;
            case R.id.nav_add_admin:
                Intent intentAdmin=new Intent(AdminFrontpage.this, AddSubAdmin.class);
                startActivity(intentAdmin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        user = FirebaseAuth.getInstance();
        final FirebaseUser userSignedIn = user.getCurrentUser();
            if(userSignedIn!=null){
                menu.removeItem(R.id.action_login);
            }else {
                menu.removeItem(R.id.action_logout);
            }
        return super.onPrepareOptionsMenu(menu);
    }

   */
}
