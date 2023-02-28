package com.example.popstar.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("/users/{id}")
    Call<UserResponse> getUserById(@Path("id") String id, @Header("Authorization") String token);

    @GET("/users/current")
    Call<UserResponse> getCurrentUser(@Header("Authorization") String token);

    @POST("/login")
    Call<UserResponse> login(@Body UserRequest body);   //讲解一

    @POST("/register")
    Call<UserResponse> register(@Body UserRequest body);

    class UserRequest {
        final String username;
        final String password;
        private String email_address;

        public UserRequest(String username, String password, String email_address) {
            this(username, password);
            this.email_address = email_address;
        }

        public UserRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

    }
}