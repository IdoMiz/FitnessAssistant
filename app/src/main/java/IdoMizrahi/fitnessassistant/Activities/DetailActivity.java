package IdoMizrahi.fitnessassistant.Activities;


import static java.security.AccessController.getContext;

import android.os.Bundle;

import com.example.model.Ingredients;
import com.example.model.Meal;
import com.example.model.RecipeResponse;
import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.MealViewModel;
import com.example.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import IdoMizrahi.fitnessassistant.Adapters.IngredientAdapter;
import IdoMizrahi.fitnessassistant.R;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class DetailActivity extends BaseActivity {
    private ScrollView scrollView;
    private ImageView foodImage, favImage,backArrow;
//    private ImageButton favImage,backArrow;
    private IngredientAdapter ingredientAdapter;
    private RecyclerView recyclerView;
    private TextView summaryTV, Summary, makingTimeTV, makingTime, servingTv, serving, caloriesTv, proteinTV, carbsTV, fatsTV, calories, protein, carbs, fats, foodName;
    private MealViewModel mealViewModel;
    private RecipeResponse recipeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeViews();

    }

    @Override
    protected void initializeViews() {
        scrollView = findViewById(R.id.scrollView2);
        foodImage = findViewById(R.id.foodImage);
        backArrow = findViewById(R.id.backArrow);
        summaryTV = findViewById(R.id.summaryTV);
        Summary = findViewById(R.id.Summary);
        makingTimeTV = findViewById(R.id.makingTimeTV);
        makingTime = findViewById(R.id.makingTime);
        servingTv = findViewById(R.id.servingTv);
        serving = findViewById(R.id.serving);
        caloriesTv = findViewById(R.id.caloriesTv);
        proteinTV = findViewById(R.id.proteinTV);
        carbsTV = findViewById(R.id.carbsTV);
        fatsTV = findViewById(R.id.fatsTV);
        calories = findViewById(R.id.calories);
        protein = findViewById(R.id.protein);
        carbs = findViewById(R.id.carbs);
        fats = findViewById(R.id.fats);
        recyclerView = findViewById(R.id.recyclerViewIngredients);
        foodName = findViewById(R.id.foodName);
        favImage = findViewById(R.id.favImage);

        GenericViewModelFactory<MealViewModel> factory = new GenericViewModelFactory<>(getApplication(), MealViewModel::new);
        mealViewModel = new ViewModelProvider(this, factory).get(MealViewModel.class);

        recipeResponse = (RecipeResponse) getIntent().getSerializableExtra("Recipe");
        Ingredients ingredients = new Ingredients();
        ingredients.addAll(recipeResponse.getExtendedIngredients());
        ingredientAdapter = new IngredientAdapter(this, ingredients, R.layout.single_ingredient);
        recyclerView.setAdapter(ingredientAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        loadToScreen(recipeResponse);
        setListeners();
    }

    @Override
    protected void setListeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        favImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(DetailActivity.this, "Favorite clicked!", Toast.LENGTH_SHORT).show();
//                mealViewModel.getMeal(recipeResponse.getId());
//                mealViewModel.getMealMutableLiveData().observe(DetailActivity.this, new Observer<Meal>() {
//                    @Override
//                    public void onChanged(Meal meal) {
//                        mealViewModel.getMealMutableLiveData().removeObservers(DetailActivity.this); // Remove observer after it's triggered once
//                        if(meal == null){
//
//                            // a place for errors so needs to be checked
//                            // when selecting a food in a mealPlan it needs to assign the meal
//                            Meal meal1 = new Meal(recipeResponse.getId(), recipeResponse.getTitle(), recipeResponse.getReadyInMinutes(), recipeResponse.getServings(), recipeResponse.getImage(), BaseActivity.getLoggedInUser().getIdFs(), false);
//
//                            mealViewModel.add(meal1);
//                        }
//                        else{
//                            mealViewModel.delete(recipeResponse.getId());
//                        }
//                    }
//                });
//            }
//        });

//        favImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(DetailActivity.this, "Favorite clicked!", Toast.LENGTH_SHORT).show();
//                // Remove any previous observers to prevent multiple observations
//                mealViewModel.getMealMutableLiveData().removeObservers(DetailActivity.this);
//
//                mealViewModel.getMeal(recipeResponse.getId());
//                mealViewModel.observeOnce(DetailActivity.this, new Observer<Meal>() {
//                    @Override
//                    public void onChanged(Meal meal) {
//                        if (meal == null) {
//                            // Meal doesn't exist, add it
//                            Meal meal1 = new Meal(recipeResponse.getId(), recipeResponse.getTitle(), recipeResponse.getReadyInMinutes(), recipeResponse.getServings(), recipeResponse.getImage(), BaseActivity.getLoggedInUser().getIdFs(), false);
//                            mealViewModel.add(meal1);
//                        } else {
//                            // Meal exists, delete it
//                            mealViewModel.delete(recipeResponse.getId());
//                        }
//                    }
//                });
//            }
//        });
        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "Favorite clicked!", Toast.LENGTH_SHORT).show();
                mealViewModel.getMeal(recipeResponse.getId());
                final Observer<Meal> observer = new Observer<Meal>() {
                    @Override
                    public void onChanged(Meal meal) {
                        mealViewModel.getMealMutableLiveData().removeObserver(this); // Remove observer after it's triggered once
                        if (meal == null) {
                            Meal meal1 = new Meal(recipeResponse.getId(), recipeResponse.getTitle(), recipeResponse.getReadyInMinutes(), recipeResponse.getServings(), recipeResponse.getImage(), BaseActivity.getLoggedInUser().getIdFs(), false);
                            meal1.setCalories(Double.parseDouble(calories.getText().toString()));
                            mealViewModel.add(meal1);
                        } else {
                            mealViewModel.delete(recipeResponse.getId(), false);
                        }
                    }
                };
                mealViewModel.getMealMutableLiveData().observe(DetailActivity.this, observer);
            }
        });
    }


    public void loadToScreen(RecipeResponse recipeResponse) {

        if (recipeResponse != null) {
            Document doc = Jsoup.parse(recipeResponse.getSummary());
            String textContent = doc.text();
            Summary.setText(textContent);

            foodName.setText((String) recipeResponse.getTitle());
            makingTime.setText(String.valueOf(recipeResponse.getReadyInMinutes()));
            serving.setText(String.valueOf(recipeResponse.getServings()));
            calories.setText(String.valueOf(recipeResponse.getCalories()));
            protein.setText(String.valueOf(recipeResponse.getProteins()));
            carbs.setText(String.valueOf(recipeResponse.getCarbs()));
            fats.setText(String.valueOf(recipeResponse.getFats()));

            Picasso.get().load(recipeResponse.getImage()).into(foodImage);
        }
    }
}