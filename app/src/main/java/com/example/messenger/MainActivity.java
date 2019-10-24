package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.messenger.fragments.FirstFragment;
import com.example.messenger.fragments.SecondFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        final androidx.fragment.app.FragmentTransaction[] ft = new androidx.fragment.app.FragmentTransaction[1];

        ft[0] = getSupportFragmentManager().beginTransaction();
        ft[0].replace(R.id.frameLayout, new FirstFragment());
        ft[0].commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:

                        ft[0] = getSupportFragmentManager().beginTransaction();
                        ft[0].replace(R.id.frameLayout, new FirstFragment());
                        ft[0].commit();

                        Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites:

                        ft[0] = getSupportFragmentManager().beginTransaction();
                        ft[0].replace(R.id.frameLayout, new SecondFragment());
                        ft[0].commit();

                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_nearby:
                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }



}