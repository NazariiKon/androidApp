package com.example.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.dto.ProductDTO;
import com.example.shop.network.ProductService;
import com.example.shop.productcard.ProductsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView rcvProducts;
    private ProductsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        rcvProducts = findViewById(R.id.rcvProducts);
        rcvProducts.setHasFixedSize(true);
        rcvProducts.setLayoutManager(new GridLayoutManager(this, 2,
                RecyclerView.VERTICAL, false));

        ProductService.getInstance()
                .jsonApi()
                .list()
                .enqueue(new Callback<List<ProductDTO>>() {
                    @Override
                    public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                        if(response.isSuccessful())
                        {
                            adapter=new ProductsAdapter(response.body());
                            rcvProducts.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductDTO>> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.m_create:
                try {
                    intent = new Intent( ProductsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                catch(Exception ex) {
                    System.out.println("Problem "+ ex.getMessage());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}