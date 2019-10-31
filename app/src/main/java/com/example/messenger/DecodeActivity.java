package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.example.messenger.SharedPref.MyPreferences;
import com.example.messenger.messenger.MessageItem;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DecodeActivity extends AppCompatActivity implements TextDecodingCallback {
    private ImageSteganography imageSteganography;
    //    private TextDecoding textDecoding;
    TextView tvDecodeMsg;
    ImageView imgDecodeImage;
    private ApiInterface apiInterface;
    private MyPreferences myPreferences;
    int fId, sId, msgId;
    List<MessageItem> dataList;
    Button decodeBtn;
    EditText etDecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        tvDecodeMsg = (TextView) findViewById(R.id.text_decode);
        imgDecodeImage = (ImageView) findViewById(R.id.img_decode);
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        myPreferences = MyPreferences.getPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Decode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dataList = new ArrayList<>();
        Intent intent = getIntent();
        msgId = intent.getIntExtra("imageId", -1);
        fId = intent.getIntExtra("friendId", -1);
        sId = myPreferences.getId();

        etDecode=(EditText)findViewById(R.id.decod_key);
        decodeBtn=(Button)findViewById(R.id.decode_btn);

        getMsgFromServer(sId, fId);



//        ImageSteganography imageSteganography = new ImageSteganography("123",
//                bitmap);
//        //TextEncoding object Instantiation
//        TextDecoding textDecoding = new TextDecoding(DecodeActivity.this,DecodeActivity.this);
//        //Executing the encoding
//        textDecoding.execute(imageSteganography);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        Log.d("ggg","result "+result.getMessage());
        if (result != null) {
            if (!result.isDecoded())
                tvDecodeMsg.setText("No message found");
            else {
                if (!result.isSecretKeyWrong()) {
                    tvDecodeMsg.setText("Decoded");
                    tvDecodeMsg.setText("" + result.getMessage());
                } else {
                    tvDecodeMsg.setText("Wrong secret key");
                }
            }
        } else {
            tvDecodeMsg.setText("Select Image First");
        }

    }

    public void getMsgFromServer(final int self, int friend) {

        Log.d("bbb", "self " + self);
        Log.d("bbb", "Friend " + friend);

//        progressDialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        Call<List<DatabaseMessageModel>> call = apiInterface.getMessage(self, friend);

        call.enqueue(new Callback<List<DatabaseMessageModel>>() {

            @Override
            public void onResponse(Call<List<DatabaseMessageModel>> call, Response<List<DatabaseMessageModel>> response) {

//                progressBar.setVisibility(View.GONE);
                List<DatabaseMessageModel> validity = response.body();
//                ipAddressTextView.setText(validity.getMessage());


                Bitmap bMap;
                if (validity.size() != 0) {

                    for (int i = 0; i < validity.size(); i++) {


                        if (validity.get(i).getId() != -1) {
                            dataList.add(new MessageItem(validity.get(i).getId(), validity.get(i).getMsg(), new User("abc"), false, validity.get(i).getImage()));

                            if (msgId == validity.get(i).getId()) {
                                Log.d("ggg","msg Id :"+msgId+"");

                                byte[] decodedString = Base64.decode(validity.get(i).getImage(), Base64.DEFAULT);
                                final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                imgDecodeImage.setImageBitmap(decodedByte);

                                decodeBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!etDecode.getText().toString().equals(null)){
                                            ImageSteganography imageSteganography = new ImageSteganography(etDecode.getText().toString(),
                                                    decodedByte);
                                            //TextEncoding object Instantiation
                                            TextDecoding textDecoding = new TextDecoding(DecodeActivity.this, DecodeActivity.this);
                                            //Executing the encoding
                                            textDecoding.execute(imageSteganography);

                                        }
                                    }
                                });


                            }
                        }
                    }


                }

                Toast.makeText(getApplicationContext(), "Data Loaded", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Log.e(TAG, t.toString());
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                Log.d("bbb", "onFailure: " + t.getMessage());
                Log.d("bbb", "onFailure: ");

            }
        });


    }
}
