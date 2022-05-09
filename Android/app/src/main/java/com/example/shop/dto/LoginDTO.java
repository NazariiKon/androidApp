package com.example.shop.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;

    public void setEmail(String _email) {
     email = _email;
    }

    public void setPassword(String _password) {
        password = _password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}