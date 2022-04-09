package com.example.shop;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shop.dto.CreateProductDTO;
import com.example.shop.dto.CreateProductResultDTO;
import com.example.shop.dto.ValidationCreateProductDTO;
import com.example.shop.network.ProductService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtInfo;
    EditText editTextName;
    EditText editTextPrice;
    EditText editTextDescription;
    EditText editTextImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtInfo = findViewById(R.id.txtInfo);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextImage = findViewById(R.id.editTextImage);
    }

    public void handleClick(View view)
    {
        CreateProductDTO dto = new CreateProductDTO(
                editTextName.getText().toString(),
                editTextPrice.getText().toString(),
                editTextDescription.getText().toString(),
                editTextImage.getText().toString()
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
                            Intent intent = new Intent(MainActivity.this, ActivityTwo.class);
                            startActivity(intent);
                        }
                        else {
                            try {
                                String json = response.errorBody().string();
                                Gson gson = new Gson();
                                ValidationCreateProductDTO serverError = gson.fromJson(json,
                                        ValidationCreateProductDTO.class);

                                editTextName.setError(String.join("", serverError.errors.name));
                                editTextPrice.setError(String.join("", serverError.errors.price));
                                editTextImage.setError(String.join("", serverError.errors.image));
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
}