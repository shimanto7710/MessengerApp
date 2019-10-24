package com.example.messenger.user_validation;

import android.app.ProgressDialog;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messenger.R;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.ServerResponse;
import com.example.messenger.retrofit.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private ApiInterface apiInterface;
    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
         progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
//                        onSignupSuccess();
                        insertUser(name,email,password);
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    public void insertUser(String name,String email,String passeord) {


//        progressBar.setVisibility(View.VISIBLE);
        Log.d("aaa",name+email+passeord);
        Call<ServerResponse> call = apiInterface.inserUser(name,email,passeord);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

//                progressBar.setVisibility(View.GONE);
                ServerResponse validity = response.body();
//                ipAddressTextView.setText(validity.getMessage());
//                Toast.makeText(getApplicationContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
                if (validity.isSuccess()) {
                    Toast.makeText(getBaseContext(), "Successful", Toast.LENGTH_LONG).show();
                    Log.d("aaa", "success: ");
                    onSignupFailed();
                } else {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
                    Log.d("aaa", "already inserted: ");
                    onSignupFailed();
                }
                progressDialog.dismiss();

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
    }


}