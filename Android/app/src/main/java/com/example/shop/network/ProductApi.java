package com.example.shop.network;

import com.example.shop.dto.CreateProductDTO;
import com.example.shop.dto.CreateProductResultDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProductApi {
    @POST("/api/products/create")
    public Call<CreateProductResultDTO> create(@Body CreateProductDTO model);
}
