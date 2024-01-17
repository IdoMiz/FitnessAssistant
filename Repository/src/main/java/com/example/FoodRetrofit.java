package com.example;

import com.example.model.FoodApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodRetrofit {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.nal.usda.gov/fdc/v1/";  // Replace with your base URL

    private FoodRetrofit() {
        // Private constructor to prevent instantiation
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static FoodApiService getFoodApiService() {
        return getRetrofitInstance().create(FoodApiService.class);
    }
}
