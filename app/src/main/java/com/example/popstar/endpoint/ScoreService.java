package com.example.popstar.endpoint;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ScoreService {

    @GET("/scores")
    Call<ScoreResponse> getScores();

    @GET("/scores/best")
    Call<ScoreResponse> getBestScore(@Header("Authorization") String token);

    @POST("/scores")
    Call<ScoreResponse> commitScore(@Body ScoreRequest body, @Header("Authorization") String token);

    class ScoreRequest {
        final String score;

        public ScoreRequest(String score) {
            this.score = score;
        }
    }
}