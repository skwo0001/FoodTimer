package com.szeyankwok.foodtimer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper db;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            android.support.v4.app.Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new CategoryFragment();
                    break;
                case R.id.navigation_favourite:
                    fragment = new FavouriteFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new CategoryFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        db = new DataBaseHelper(this);

//        db.addFood("Spaghetti","Pasta","9",false);
//        db.addFood("Penne","Pasta","11",false);
//        db.addFood("Rigatoni", "Pasta","13",false);
//        db.addFood("Lasagna", "Pasta","13", false);
//        db.addFood("Shin Ramyun","Instant Noodles","5",true);
//        db.addFood("Cup Noodles","Instant Noodles","3",true);



    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            return true;
        }

        return false;
    }

    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

}
