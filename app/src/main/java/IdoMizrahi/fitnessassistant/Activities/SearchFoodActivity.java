package IdoMizrahi.fitnessassistant.Activities;

import static android.app.PendingIntent.getActivity;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Food;
import com.example.model.FoodApiResponse;
import com.example.model.FoodApiService;
import com.example.model.FoodItem;
import com.example.model.Foods;

import java.time.LocalTime;

import IdoMizrahi.fitnessassistant.Adapters.FoodAdapter;
import IdoMizrahi.fitnessassistant.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFoodActivity extends BaseActivity {

    private static final int ADD_FOOD_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private TextView textView;
    private ImageView arrowBtn;
    private SearchView searchView;
    private FoodAdapter foodAdapter;
    private Foods foodList;
    private Button finishBtn;
    private Foods returnedFoods;
    private ConstraintLayout mainLayout;
    private Retrofit retrofit;
    private FoodApiService apiService;
    private final String baseUrl ="https://api.edamam.com";
    private static final String APP_ID = "e1dea647";
    private static final String apiKey = "d532f50b8254a960f3976215b21327a3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        initializeViews();

    }
    @Override
    protected void initializeViews() {
        arrowBtn = findViewById(R.id.arrowBtn);
        textView = findViewById(R.id.searchFoodTitle);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        finishBtn = findViewById(R.id.finishBtn);
        mainLayout = findViewById(R.id.mainLayout);


        // Initialize RecyclerView and its adapter
        foodList = new Foods();
        returnedFoods = new Foods();

        setRecyclerView();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(FoodApiService.class);
        setSearchView();
        setListeners();

    }
    @Override
    protected void setListeners() {
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to pass the returned food list back to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("returnedFoods", returnedFoods);
                setResult(Activity.RESULT_OK, resultIntent);
                finish(); // Finish the current activity and return to the previous one
            }
        });
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });
    }


    private void setRecyclerView() {
        foodAdapter = new FoodAdapter(this, foodList, R.layout.food_card, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Food food) {

                // Intent to start AddFoodActivity
                Intent intent = new Intent(SearchFoodActivity.this, AddFoodActivity.class);
                intent.putExtra("FoodItem", food);
                startActivityForResult(intent, ADD_FOOD_REQUEST_CODE);

            }
        }, new FoodAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Food food) {
                boolean success = false;
                if (returnedFoods.contains(food)) {
                    success = returnedFoods.remove(food);
                    Toast.makeText(SearchFoodActivity.this, String.valueOf(food.getLabel() + ", was removed from the adding list"), Toast.LENGTH_SHORT).show();
                }
                return success;
            }
        }, new FoodAdapter.OnPlusButtonClickListener() {
            @Override
            public void onPlusButtonClick(Food food) {

            }
        }, new FoodAdapter.OnMinusButtonClickListener() {
            @Override
            public void onMinusButtonClick(Food food) {

            }
        });
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    // the function gets a name and performs a search for foods with that name
    private void performSearch(String desiredFoodName) {
        Call<FoodApiResponse> call = apiService.searchFoods(desiredFoodName, APP_ID, apiKey);

        call.enqueue(new Callback<FoodApiResponse>() {
            @Override
            public void onResponse(Call<FoodApiResponse> call, Response<FoodApiResponse> response) {
                if (response.isSuccessful()) {
                    FoodApiResponse foodApiResponse = response.body();
                    if (foodApiResponse != null && foodApiResponse.getHints() != null && !foodApiResponse.getHints().isEmpty()) {
                        Foods foods = new Foods();

                        for(FoodItem food : foodApiResponse.getHints()){
                            food.getFood().setServing(1);
                            foods.add(food.getFood());
                        }

                        foodAdapter.setFoods(foods);
                    } else {
                        Log.e("API", "No ingredients found in response");
                    }
                } else {
                    Log.e("API", "Failed to search foods. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FoodApiResponse> call, Throwable t) {
                Log.e("API", "Failed to search foods", t);
            }
        });
    }
    // Sets the search view listener
    public void setSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits query
                if(!query.isEmpty())
                    performSearch(query);
                else {
                    foodAdapter.clearFood();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as text changes
                if(!newText.isEmpty())
                    performSearch(newText);
                else {
                    foodAdapter.clearFood();
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FOOD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Food newFood = (Food) data.getSerializableExtra("NewFood");
                    newFood.setTime(LocalTime.now());
                    returnedFoods.add(newFood);

                    if(newFood != null){
                        Toast.makeText(this, "Added to the list: " + newFood.getLabel(), Toast.LENGTH_SHORT).show();

                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Couldn't add to the list", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Function to hide the keyboard
    private void hideKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {

    }
}
