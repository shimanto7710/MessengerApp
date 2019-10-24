package com.example.messenger.splash_screen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.MainActivity;
import com.example.messenger.R;
import com.example.messenger.SharedPref.MyPreferences;
import com.example.messenger.user_validation.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    Boolean finishFlag=false;
    private MyPreferences myPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
//        final String PREF_NAME="login_check";
        myPreferences = MyPreferences.getPreferences(this);
//        myPreferences.setUserName(PREF_NAME);
        splashWithBackgroundThread();

        finishFlag=false;

        // background thread
//        new BackgroundProcess(this).execute();
    }

    /**
     * run in background but the UI can not be changed from it
     */
    private void splashWithBackgroundThread() {
        /****** Create Thread that will sleep for 5 seconds****/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(1 * 1000);

                    // After 5 seconds redirect to another intent
                    if (!myPreferences.getOneTimeUse()) {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    //Remove activity
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }

    /**
     * run in background but UI can be change from this
     */
    private void splashWithoutBackgroundThread(){
        Handler handler;
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    /**
     * Background thread to do some background work
     * it will not hamper UI
     * even it works fine if the current activity is destroyed
     */
    public class BackgroundProcess extends AsyncTask<String, Boolean, Void> {

        Context context;

        public BackgroundProcess(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {

//            myDbHelper = new DatabaseHelper(getApplicationContext());
//            initializeDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("dipbanik","complete");
//            Toast.makeText(getApplicationContext(),"Database Copied",Toast.LENGTH_SHORT).show();
            finishFlag=true;
//            btnNext.setVisibility();
        }

    }


}
