package com.example.shop.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {
    private String token;

    public String getToken() {
        return token;
    }
}