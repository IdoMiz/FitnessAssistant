package com.example.model;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeService {

    @GET("mealplanner/generate")
    Call<MealPlanResponse> searchMealPlanByCalories(
            @Query("apiKey") String apiKey,
            @Query("timeFrame") String timeFrame,
            @Query("targetCalories") int targetCalories,
            @Query("exclude") String ListOfExcludes,
            @Query("diet") String diet
    );

    @GET("recipes/{recipeId}/information")
    Call<RecipeResponse> getRecipeInformation(
            @Path("recipeId") int recipeId,
            @Query("apiKey") String apiKey,
            @Query("includeNutrition") boolean includeNutrition
    );


}
