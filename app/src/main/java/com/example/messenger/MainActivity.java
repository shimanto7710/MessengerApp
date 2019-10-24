package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.messenger.SharedPref.MyPreferences;
import com.example.messenger.fragments.FirstFragment;
import com.example.messenger.fragments.SecondFragment;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.ServerResponse;
import com.example.messenger.retrofit.User;
import com.example.messenger.user_validation.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private MyPreferences myPreferences;
    private ApiInterface apiInterface;
    int selfId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Messenger");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
//        final String PREF_NAME="login_check";
        myPreferences = MyPreferences.getPreferences(this);
//        myPreferences.setUserName(PREF_NAME);

        selfId=getSelfId(myPreferences.getUserName());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        final androidx.fragment.app.FragmentTransaction[] ft = new androidx.fragment.app.FragmentTransaction[1];

        ft[0] = getSupportFragmentManager().beginTransaction();
        ft[0].replace(R.id.frameLayout, new FirstFragment(myPreferences.getUserName()));
        ft[0].commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:

                        ft[0] = getSupportFragmentManager().beginTransaction();
                        ft[0].replace(R.id.frameLayout, new FirstFragment(myPreferences.getUserName()));
                        ft[0].commit();

                        Toast.makeText(MainActivity.this, "Recent", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites:

                        ft[0] = getSupportFragmentManager().beginTransaction();
                        ft[0].replace(R.id.frameLayout, new SecondFragment(myPreferences.getUserName()));
                        ft[0].commit();

                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
//                    case R.id.action_nearby:
//                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
//                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                myPreferences.setOneTimeUse(false);
                myPreferences.setUserName("none");
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                finish();
                return false;
            }
        });

        return true;
    }

    public int getSelfId(String email) {

        final int[] id = new int[1];

//        progressBar.setVisibility(View.VISIBLE);
        Call<ServerResponse> call = apiInterface.getSelfId(email);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

//                progressBar.setVisibility(View.GONE);
                ServerResponse validity = response.body();
//                ipAddressTextView.setText(validity.getMessage());
                Toast.makeText(getApplicationContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("aaa", "success: " + validity.getMessage());

                id[0] = Integer.parseInt(validity.getMessage());


            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Log.e(TAG, t.toString());
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("aaa", "onFailure: " + t.getMessage());
                Log.d("aaa", "onFailure: ");

            }
        });

        return id[0];

    }


}