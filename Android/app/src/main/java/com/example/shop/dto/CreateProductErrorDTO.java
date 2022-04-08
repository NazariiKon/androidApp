package com.example.shop.dto;
import lombok.Data;

@Data
public class CreateProductErrorDTO {
    public String [] name;
    public String [] price;
    public String [] image;
}