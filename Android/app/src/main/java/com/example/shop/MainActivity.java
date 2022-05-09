package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.shop.dto.CreateProductDTO;
import com.example.shop.dto.CreateProductResultDTO;
import com.example.shop.dto.ValidationCreateProductDTO;
import com.example.shop.network.ProductService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    TextView txtInfo;
    EditText editTextName;
    EditText editTextPrice;
    EditText editTextDescription;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    String sImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtInfo = findViewById(R.id.txtInfo);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
    }

    public void handleSelectImageClick(View view) {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri uri = data.getData();
                // update the preview image in the layout
                //IVPreviewImage.setImageURI(uri);
                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
            }
        }
    }

    public void handleClick(View view)
    {
        if(hasConnection(this))
            Toast.makeText(this, "Інтернет присутній", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(this, "Відсутнє з'єднання з інтернетом", Toast.LENGTH_LONG).show();
            return;
        }

            CreateProductDTO dto = new CreateProductDTO(
                editTextName.getText().toString(),
                editTextPrice.getText().toString(),
                editTextDescription.getText().toString(),
                sImage
        );
        ProductService
                .getInstance()
                .jsonApi()
                .create(dto)
                .enqueue(new Callback<CreateProductResultDTO>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<CreateProductResultDTO> call, Response<CreateProductResultDTO> response) {
                        if(response.isSuccessful()) {
                            CreateProductResultDTO result = response.body();
                            Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                            startActivity(intent);
                        }
                        else {
                            try {
                                Toast.makeText(MainActivity.this, "Problem "+response.code(), Toast.LENGTH_SHORT).show();
                                String json = response.errorBody().string();
                                Gson gson = new Gson();
                                ValidationCreateProductDTO serverError = gson.fromJson(json,
                                        ValidationCreateProductDTO.class);

//                                editTextName.setError(String.join("", serverError.errors.name));
//                                editTextPrice.setError(String.join("", serverError.errors.price));
//                                editTextImage.setError(String.join("", serverError.errors.image));
                            } catch(Exception ex) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateProductResultDTO> call, Throwable t) {

                    }
                });
        int n=5;
//        String text = editTextName.getText().toString();
//        txtInfo.setText(text);
    }

    // перевіряє з'єднання з інтернетом
    public boolean hasConnection(final Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        if (activeNW != null && activeNW.isConnected())
        {
            return true;
        }
        return false;
    }
}