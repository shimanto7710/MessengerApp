package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;
import com.example.messenger.SharedPref.MyPreferences;
import com.example.messenger.messenger.MessageAdapter;
import com.example.messenger.messenger.MessageItem;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.ServerResponse;
import com.example.messenger.retrofit.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessengerActivity extends AppCompatActivity implements TextDecodingCallback {
    private ApiInterface apiInterface;
    Bitmap bitmap;
    private MyPreferences myPreferences;
    public static final int IMG_REQUEST = 777;
    MessageAdapter customAdapter;
    int selfId;
    int friendId;
    String name;
    ListView list;
    ImageButton imageButton;
    EditText editText;
    private Handler mHandler;
    List<MessageItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        myPreferences = MyPreferences.getPreferences(this);
        final Intent intent = getIntent();
        name = intent.getStringExtra("name");
        editText = (EditText) findViewById(R.id.editText);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextDecoding textDecoding = new TextDecoding(MessengerActivity.this, MessengerActivity.this);

        mHandler = new Handler(Looper.getMainLooper());

        friendId = intent.getIntExtra("friendId", -1);
        selfId = getSelfId(myPreferences.getUserName());


        list = findViewById(R.id.messages_view);
        customAdapter = new MessageAdapter(this,textDecoding);
        list.setAdapter(customAdapter);
        dataList = new ArrayList<>();
//        dataList.add(new MessageItem("Hey", new User(name),false));
//        dataList.add(new MessageItem("Hi", new User("Chintu"),true));
//        dataList.add(new MessageItem("Wassup", new User(name),false));

//        customAdapter.add(new MessageItem(-100, "Hey", new User(name), false, "none@image"));
//
//        customAdapter.add(new MessageItem(-101, "Hi", new User("Chintu"), true, "none@image"));
//        customAdapter.add(new MessageItem(-102, "Wassup??", new User(name), false, "none@image"));


        imageButton = (ImageButton) findViewById(R.id.send_msg);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                customAdapter.add(new MessageItem("Wassup??", new User(name), true,"none@image"));
                uploadImage(editText.getText().toString(), "none@image");
                getSelfId(myPreferences.getUserName());
                editText.getText().clear();
                InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                customAdapter.notifyDataSetChanged();


            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (dataList.get(i).getMessage().equals("none@msg")){

                    customAdapter.refreshAdapter();
                    Log.d("ggg",dataList.size()+"");
                    Intent intent1=new Intent(MessengerActivity.this,DecodeActivity.class);
//                Log.d("ggg",dataList.get(i).getImage());
                    intent1.putExtra("imageId",dataList.get(i).getId());
                    intent1.putExtra("friendId",friendId);
//                intent1.putExtra("img",dataList.get(i).getImage());
                    startActivity(intent1);
                }
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

        getMenuInflater().inflate(R.menu.messenger_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.upload_icon);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                myPreferences.setOneTimeUse(false);
//                myPreferences.setUserName("none");
//                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//
//                finish();
//                selectImage();
                Intent intent=new Intent(getApplicationContext(), EncodeActivity.class);
                intent.putExtra("friendId",friendId);
                intent.putExtra("name",name);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }

    public void selectImage() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, IMG_REQUEST);

    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            final Uri path = data.getData();
//            Log.d("uuu","found URI path "+path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                String imgString = imageToString();
                uploadImage("none@msg", imgString);

                getSelfId(myPreferences.getUserName());

                customAdapter.add(new MessageItem(-1050, "none@msg", new User(name), true, imgString));
//                customAdapter.deleteItem(-1050);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
//            Toast.makeText(PostImage.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public String imageToString() {
        Log.d("nnn", "bitmap " + bitmap);
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
        byte[] imgByte = byteArrayInputStream.toByteArray();

        Log.d("nnn", "after conversion " + Base64.encodeToString(imgByte, Base64.DEFAULT));
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }


    public void uploadImage(String msg, String img) {
//        Log.d("uuu", "upload Function Triggered ");
        Log.d("nnn", "get image after conver " + img);

//        Log.d("uuu", "bitmap String  " + imgString);
        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000);
        int rand_int2 = rand.nextInt(1000);
        int rand_int3 = rand.nextInt(1000);
        String t = rand_int3 + rand_int1 + rand_int2 + "";
        Log.d("uuu", "" + t);

        Log.d("lll", "self " + selfId);
        Log.d("lll", "friend " + friendId);

        Call<ServerResponse> call = apiInterface.uploadImageToServer(myPreferences.getId(), friendId, msg, t, img);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                ServerResponse validity = response.body();
//                Toast.makeText(getApplicationContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("uuu", "SuccessFully image uploaded " + validity.getMessage());
//                if (validity.isSuccess()) {
////                    Toast.makeText(getBaseContext(), "Image Successfully Uploaded", Toast.LENGTH_LONG).show();
//
//                    Log.d("aaa", "success: ");
//                } else {
//                    Toast.makeText(getBaseContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
////                    onLoginFailed();
//                }
//                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
//                Log.e(TAG, t.toString());
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("uuu", "Failed to upload " + t.getMessage());

            }
        });
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
                        Log.d("kkk","self "+validity.get(i).id1);
                        Log.d("kkk","friend "+validity.get(i).id2);
//                        Log.d("kkk", "path " + validity.get(i).getImage());
//                        Log.d("bbb", "onResponse: size" + validity.size());
//                        Log.d("nnn", "get from server " + validity.get(i).getImage());
//                        Log.d("lll", "get msg :" + validity.get(i).getMsg());

                        if (validity.get(i).getId() != -1) {
                            dataList.add(new MessageItem(validity.get(i).getId(), validity.get(i).getMsg(), new User(name), false, validity.get(i).getImage()));

                            if (validity.get(i).getId1() == self) {
                                if (!customAdapter.findMsg(validity.get(i).getId())) {
                                    customAdapter.add(new MessageItem(validity.get(i).getId(), validity.get(i).getMsg(), new User("Special"), true, validity.get(i).getImage()));
                                }


                            } else {

                                if (!customAdapter.findMsg(validity.get(i).getId())) {

                                    customAdapter.add(new MessageItem(validity.get(i).getId(), validity.get(i).getMsg(), new User(name), false, validity.get(i).getImage()));
                                }
                            }

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
//                                    customAdapter.add(new MessageItem("Wassup??", new User(name), false, "none@image"));
                                    customAdapter.notifyDataSetChanged();
//                                    customAdapter.refreshAdapter();
                                    list.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Select the last row so it will scroll into view...
                                            list.setSelection(customAdapter.getCount() - 1);
                                        }
                                    });
                                }
                            });

//                            refreshAdapter();
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

                getMsgFromServer(id[0], friendId);


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


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography imageSteganography) {

    }
}
