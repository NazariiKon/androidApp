package com.example.shop.dto;

import lombok.Data;

@Data
public class ValidationCreateProductDTO {
    public int status;
    public String title;
    public CreateProductErrorDTO errors;
}