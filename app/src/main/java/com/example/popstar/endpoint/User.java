package com.example.popstar.endpoint;

public class User {
    private String name,password,email_address,token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    User(String name, String password, String email_address){
        this.name=name;
        this.password=password;
        this.email_address=email_address;
    }

}
