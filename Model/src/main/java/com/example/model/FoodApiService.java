package com.example.model;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;

public interface FoodApiService {

    // Get a list of all available foods
    @GET("foods/list")
    Call<List<Food>> getAllFoods(@Query("api_key") String apiKey);

    // Get information about a specific food by its ID
    @GET("food/{id}")
    Call<Food> getFoodById(@Path("id") int foodId, @Query("api_key") String apiKey);


    // Search for foods based on a query string
    @GET("foods/search")
    Call<List<Food>> searchFoods(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("query")
    Call<List<Food>> searchFoods2(@Query("query") String query);

}
