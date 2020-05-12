package com.lovejazz.webjump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    static final String TAG = "MainPage";
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_activity);
        // BottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.library);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
        Library libraryFragment = new Library();
        Books booksFragment = new Books();
        Profile profileFragment = new Profile();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i(TAG,"onNavigationItemSelected: ");
        switch(item.getItemId()){
            case R.id.library:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,libraryFragment).commit();
                return true;
            case R.id.book:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, booksFragment).commit();
                return true;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment   ).commit();
                return true;
        }
        return false;
    }
}
