package com.example.model;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;

public interface FoodApiService {
    @GET("/api/food-database/v2/parser")
    Call<FoodApiResponse> searchFoods(@Query("ingr") String query, @Query("app_id") String appId, @Query("app_key") String appKey);
}
