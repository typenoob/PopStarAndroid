package com.example.popstar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;

import com.example.popstar.endpoint.ScoreResponse;
import com.example.popstar.endpoint.ScoreService;
import com.example.popstar.endpoint.UserResponse;
import com.example.popstar.endpoint.UserService;
import com.example.popstar.io.FileIO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private Stars stars;
    private Score score;
    private String token;
    private Boolean is_login = false;

    public UserService getUserService() {
        return userService;
    }
    public ScoreService getScoreService() {
        return scoreService;
    }

    private UserService userService;
    private ScoreService scoreService;

    public String getToken() {
        return token;
    }

    public Boolean getIsLogin() {
        return is_login;
    }

    public Boolean getIs_login() {
        return is_login;
    }

    public void setIs_login(Boolean is_login) {
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit()
                .putBoolean("is_login", is_login)
                .apply();
        this.is_login = is_login;
    }

    public void setToken(String token) {
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit()
                .putString("token", token)
                .apply();
        this.token = token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        is_login = sp.getBoolean("is_login", false);
        token = sp.getString("token", null);
        stars = (Stars) FileIO.readObject(getApplicationContext(), "stars.obj");
        score = (Score) FileIO.readObject(getApplicationContext(), "score.obj");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.133:7001/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create(gson)) //设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        // 创建 网络请求接口 的实例
        userService = retrofit.create(UserService.class);
        scoreService = retrofit.create(ScoreService.class);
    }

    public Stars getStars() {
        return stars;
    }

    public void setStars(Stars stars) {
        this.stars = stars;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}