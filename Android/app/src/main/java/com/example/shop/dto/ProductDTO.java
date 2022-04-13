package com.example.shop.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String image;

    public String getName() {
        return  name;
    }

    public String getImage() {
        return image;
    }
}
