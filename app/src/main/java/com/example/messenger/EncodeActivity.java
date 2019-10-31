package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.example.messenger.SharedPref.MyPreferences;
import com.example.messenger.network.ApiInterface;
import com.example.messenger.network.RetrofitApiClient;
import com.example.messenger.retrofit.ServerResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EncodeActivity extends AppCompatActivity implements TextEncodingCallback {
    public static final int IMG_REQUEST = 777;
    EditText etHideText, keyEt;
    ImageView imageView;
    private ApiInterface apiInterface;
    private MyPreferences myPreferences;
    Button doneBtn, decodeBtn;
    Bitmap bitmap, encoded_image;
    Uri path;
    private ImageSteganography imageSteganography;
    private TextEncoding textEncoding;
    private int friendId, selfId;
    private String name;
    private ProgressDialog save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Encode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        myPreferences = MyPreferences.getPreferences(this);
        Intent intent = getIntent();

        friendId = intent.getIntExtra("friendId", -1);
        selfId = myPreferences.getId();
        name = intent.getStringExtra("name");


        imageView = (ImageView) findViewById(R.id.img_view);
        etHideText = (EditText) findViewById(R.id.hide_text_view);
        keyEt = (EditText) findViewById(R.id.encode_key_et);
        doneBtn = (Button) findViewById(R.id.hide_btn);
//        decodeBtn = (Button) findViewById(R.id.decode_btn);

        selectImage();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etHideText.getText().toString().equals(null) && !keyEt.getText().toString().equals(null)) {

                    if (path != null) {
                        imageSteganography = new ImageSteganography(etHideText.getText().toString(),
                                keyEt.getText().toString(),
                                bitmap);
                        //TextEncoding object Instantiation
                        textEncoding = new TextEncoding(EncodeActivity.this, EncodeActivity.this);

                        textEncoding.execute(imageSteganography);
                    }
                }
            }
        });

//        decodeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Bitmap imgToSave = encoded_image;
//                Thread PerformEncoding = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        saveToInternalStorage(imgToSave);
//                    }
//                });
//                save = new ProgressDialog(EncodeActivity.this);
//                save.setMessage("Saving, Please Wait...");
//                save.setTitle("Saving Image");
//                save.setIndeterminate(false);
//                save.setCancelable(false);
//                save.show();
//                PerformEncoding.start();
//
//            }
//        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
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

            path = data.getData();
//            Log.d("uuu","found URI path "+path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
//                String imgString = imageToString();
                imageView.setImageBitmap(bitmap);
//                uploadImage("none@msg", imgString);

//                getSelfId(myPreferences.getUserName());

//                customAdapter.add(new MessageItem(-1050, "none@msg", new User(name), true, imgString));
//                customAdapter.deleteItem(-1050);

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
//            Toast.makeText(PostImage.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

//    public String imageToString() {
////        Log.d("nnn", "bitmap " + bitmap);
//        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
//        byte[] imgByte = byteArrayInputStream.toByteArray();
//
//        Log.d("nnn", "after conversion " + Base64.encodeToString(imgByte, Base64.DEFAULT));
//        return Base64.encodeToString(imgByte, Base64.DEFAULT);
//    }

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            Log.d("ggg", "Encoded image " + encoded_image);
//            Toast.makeText(EncodeActivity.this, "Encoded", Toast.LENGTH_LONG).show();


            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            encoded_image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayInputStream);
            byte[] imgByte = byteArrayInputStream.toByteArray();

            Log.d("nnn", "after conversion " + Base64.encodeToString(imgByte, Base64.DEFAULT));
            String imgString= Base64.encodeToString(imgByte, Base64.DEFAULT);


//            imageView.setImageBitmap(encoded_image);

            Log.d("ggg","encoded");
            uploadImage("none@msg", imgString);


//            whether_encoded.setText("Encoded");
//            imageView.setImageBitmap(encoded_image);
        }
    }

    public void uploadImage(String msg, String img) {

        Random rand = new Random();
        int rand_int1 = rand.nextInt(1000);
        int rand_int2 = rand.nextInt(1000);
        int rand_int3 = rand.nextInt(1000);
        String t = rand_int3 + rand_int1 + rand_int2 + "";


        Call<ServerResponse> call = apiInterface.uploadImageToServer(myPreferences.getId(), friendId, msg, t, img);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {


                ServerResponse validity = response.body();
//                Toast.makeText(getApplicationContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("uuu", "SuccessFully image uploaded " + validity.getMessage());
                if (validity.isSuccess()) {
                    Toast.makeText(getBaseContext(), "Image Successfully Uploaded", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MessengerActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("friendId", friendId);
                    startActivity(intent);
                    Log.d("aaa", "success: ");
                } else {
                    Toast.makeText(getBaseContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
//                    onLoginFailed();
                }
//                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("uuu", "Failed to upload " + t.getMessage());

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".PNG"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            save.dismiss();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
