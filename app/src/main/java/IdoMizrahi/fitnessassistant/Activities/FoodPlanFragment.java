package IdoMizrahi.fitnessassistant.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.DateUtil;
import com.example.model.DietEnum;
import com.example.model.Food;
import com.example.model.Meal;
import com.example.model.MealEnum;
import com.example.model.MealPlanResponse;
import com.example.model.Meals;
import com.example.model.Nutrients;
import com.example.model.RecipeResponse;
import com.example.model.RecipeService;
import com.example.model.User;
import com.example.viewmodel.FoodViewModel;
import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.MealViewModel;
import com.example.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import IdoMizrahi.fitnessassistant.Adapters.FoodAdapter;
import IdoMizrahi.fitnessassistant.Adapters.ItemAdapter;
import IdoMizrahi.fitnessassistant.Adapters.MealAdapter;
import IdoMizrahi.fitnessassistant.R;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodPlanFragment extends Fragment {
    private RecipeService recipeService;
    private TextView caloriesValue, carbsValue, proteinValue, fatValue;
    private TextView titleTextView;
    private View nutritionCard;
    private ImageView breakfastMealImageView;
    private TextView breakfastMealTitleTV;
    private TextView breakfastMealReadyInMinutesTV;
    private TextView breakfastMealServingsTV;
    private TextView breakfastMealCaloriesTV;
    private ImageView lunchMealImageView;
    private TextView lunchMealTitleTV;
    private TextView lunchMealReadyInMinutesTV;
    private TextView lunchMealServingsTV;
    private TextView lunchMealCaloriesTV;
    private ImageView dinnerMealImageView;
    private TextView dinnerMealTitleTV;
    private TextView dinnerMealReadyInMinutesTV;
    private TextView dinnerMealServingsTV;
    private TextView dinnerMealCaloriesTV;
    private Button reGenerateButton;
    private UserViewModel userViewModel;
    private MealViewModel mealViewModel;
    private FoodViewModel foodViewModel;
    private CardView breakfastCardView;
    private CardView lunchCardView;
    private CardView dinnerCardView;
    private RecipeResponse recipeResponse;
    private boolean switchScreen;
    private DrawerLayout drawerLayout;
    private ImageView settingImage;
    private ImageView loadMealImageView;

    // dialog things
    private RecyclerView mealDialogRecyclerView;
    private Spinner mealDialogSpinner;
    private ArrayAdapter<MealEnum> spinnerDialogAdapter;
    private MealAdapter mealAdapter;
    private MealEnum selectedMealType;
    private Meal selectedMeal;

    // Second Screen
    private EditText foodNameToRemove;
    private Button addFoodNameToRemove;
    private RecyclerView itemsRecyclerView;
    private Spinner dietPlan;
    private Button cancelBtn;
    private Button saveBtn;
    private ImageView resetParameters;
    private ItemAdapter itemAdapter;
    private TextView foodDescription;
    private ArrayAdapter<DietEnum> spinnerAdapter;
    private final String Api_Key = "6a019b2ddd29479e9501d9cb32e53677";
//    private final String Api_Key = "c90b5a6c33414eef9bf9ec9db1943f78";
//    private final String Api_Key = "1f00fe2096104aa9a3379dcd7b8e728a";
    private static final String BASE_URL = "https://api.spoonacular.com/";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_plan, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
    }

    public void initializeViews(View view){
        switchScreen = false;
        drawerLayout = view.findViewById(R.id.drawerLayout);
        settingImage = view.findViewById(R.id.settingImage);
        breakfastCardView = view.findViewById(R.id.breakfastCardView);
        lunchCardView = view.findViewById(R.id.lunchCardView);
        dinnerCardView = view.findViewById(R.id.dinnerCardView);
        titleTextView = view.findViewById(R.id.title);
        nutritionCard = view.findViewById(R.id.nutritionCard);
        breakfastMealImageView = view.findViewById(R.id.breakfastMealImageView);
        breakfastMealTitleTV = view.findViewById(R.id.breakfastMealTitleTV);
        breakfastMealReadyInMinutesTV = view.findViewById(R.id.breakfastMealReadyInMinutesTV);
        breakfastMealServingsTV = view.findViewById(R.id.breakfastMealServingsTV);
        breakfastMealCaloriesTV = view.findViewById(R.id.breakfastMealCaloriesTV);
        lunchMealImageView = view.findViewById(R.id.lunchMealImageView);
        lunchMealTitleTV = view.findViewById(R.id.lunchMealTitleTV);
        lunchMealReadyInMinutesTV = view.findViewById(R.id.lunchMealReadyInMinutesTV);
        lunchMealServingsTV = view.findViewById(R.id.lunchMealServingsTV);
        lunchMealCaloriesTV = view.findViewById(R.id.lunchMealCaloriesTV);
        dinnerMealImageView = view.findViewById(R.id.dinnerMealImageView);
        dinnerMealTitleTV = view.findViewById(R.id.dinnerMealTitleTV);
        dinnerMealReadyInMinutesTV = view.findViewById(R.id.dinnerMealReadyInMinutesTV);
        dinnerMealServingsTV = view.findViewById(R.id.dinnerMealServingsTV);
        dinnerMealCaloriesTV = view.findViewById(R.id.dinnerMealCaloriesTV);
        reGenerateButton = view.findViewById(R.id.button);
        loadMealImageView = view.findViewById(R.id.loadMealImageView);

        foodNameToRemove = view.findViewById(R.id.foodNameToRemove);
        addFoodNameToRemove = view.findViewById(R.id.addFoodNameToRemove);
        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        dietPlan = view.findViewById(R.id.dietPlan);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        saveBtn = view.findViewById(R.id.saveBtn);
        resetParameters = view.findViewById(R.id.resetPic);
        foodDescription = view.findViewById(R.id.dietDescription);

        // nutrient card view
        caloriesValue = view.findViewById(R.id.caloriesValue);
        carbsValue = view.findViewById(R.id.carbsValue);
        proteinValue = view.findViewById(R.id.proteinValue);
        fatValue = view.findViewById(R.id.fatsValue);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recipeService = retrofit.create(RecipeService.class);

        GenericViewModelFactory<UserViewModel> factory1 = new GenericViewModelFactory<>(getActivity().getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory1).get(UserViewModel.class);

        GenericViewModelFactory<MealViewModel> factory2 = new GenericViewModelFactory<>(getActivity().getApplication(), MealViewModel::new);
        mealViewModel = new ViewModelProvider(this, factory2).get(MealViewModel.class);

        GenericViewModelFactory<FoodViewModel> factory3 = new GenericViewModelFactory<>(getActivity().getApplication(), FoodViewModel::new);
        foodViewModel = new ViewModelProvider(this, factory3).get(FoodViewModel.class);

        User user = BaseActivity.getLoggedInUser();

        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, DietEnum.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietPlan.setAdapter(spinnerAdapter);
        dietPlan.setSelection(BaseActivity.getLoggedInUser().getDiet().ordinal());


        // setting recycler view
        itemAdapter = new ItemAdapter(new ItemAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(String item) {

                checkIfDelete(item);
                return false;
            }
        }, getContext(), user.getAllergies(), R.layout.item_card);

        itemsRecyclerView.setAdapter(itemAdapter);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if(user.getTodaysMealsIds() != null){
            if(user.getTodaysMealsIds().isEmpty()){
                searchRecipes(user.getRecommendedCalories());
            }
            else{
                getSavedMealsFromStorage();
                //loadMealsToScreen(); // old function
            }
        }
        else{
            user.setTodaysMealsIds(new ArrayList<>());
            searchRecipes(user.getRecommendedCalories());
        }
        setListeners();
    }
    public void displayPlanData(RecipeResponse recipeResponse){
        caloriesValue.setText(String.valueOf((int) (recipeResponse.getCalories() + Double.parseDouble(String.valueOf(caloriesValue.getText())))));
        carbsValue.setText(String.valueOf((int) (recipeResponse.getCarbs() + Double.parseDouble(String.valueOf(carbsValue.getText())))));
        proteinValue.setText(String.valueOf((int) (recipeResponse.getProteins() + Double.parseDouble(String.valueOf(proteinValue.getText())))));
        fatValue.setText(String.valueOf((int) (recipeResponse.getFats() + Double.parseDouble(String.valueOf(fatValue.getText())))));
    }

    private void checkIfDelete(String item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to proceed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemAdapter.removeItem(item);
                BaseActivity.getLoggedInUser().getAllergies().remove(item);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public boolean updateNutritionValuesAfterChangingMeals(Meals meals, String mealField){
        User user = BaseActivity.getLoggedInUser();
        if(meals != null) {

            for (int i = 0; i < meals.size(); i++) {
                if (mealField.equals(String.valueOf(meals.get(i).getCalories()))) {
                    user.setFoodPlanCalories(user.getFoodPlanCalories() - meals.get(i).getCalories() + selectedMeal.getCalories());
                    user.setFoodPlanProtein(user.getFoodPlanProtein() - meals.get(i).getProtein() + selectedMeal.getProtein());
                    user.setFoodPlanCarbs(user.getFoodPlanCarbs() - meals.get(i).getCarbs() + selectedMeal.getCarbs());
                    user.setFoodPlanFats(user.getFoodPlanFats() - meals.get(i).getFats() + selectedMeal.getFats());

                    // the saving of the user is happening later
                    return true;
                }
            }
        }
        return false;

    }

    private void setListeners() {
        loadMealImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMealImageView.setClickable(false);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

                mealDialogRecyclerView = dialogView.findViewById(R.id.mealDialogRecyclerView);
                mealDialogSpinner = dialogView.findViewById(R.id.mealDialogSpinner);

                spinnerDialogAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, MealEnum.values());
                spinnerDialogAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mealDialogSpinner.setAdapter(spinnerDialogAdapter);
//                mealDialogSpinner.setSelection(0);

                mealDialogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedMealType = (MealEnum) mealDialogSpinner.getItemAtPosition(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        selectedMealType = MealEnum.BREAKFAST;
                    }
                });

                //mealDialogSpinner.setSelection(BaseActivity.getLoggedInUser().getDiet().ordinal());
                mealViewModel.getAll(BaseActivity.getLoggedInUser().getIdFs());
                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                    @Override
                    public void onChanged(Meals meals) {
                        mealAdapter = new MealAdapter(getContext(), meals, new MealAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClicked(Meal meal) {
                                selectedMeal = meal;
                                Toast.makeText(getContext(), meal.getTitle() + ", was selected", Toast.LENGTH_SHORT).show();
                            }
                        }, new MealAdapter.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClicked(Meal meal) {
                                return false;
                            }
                        });

                        mealDialogRecyclerView.setAdapter(mealAdapter);
                        mealDialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setTitle("Load Meals");
                builder.setMessage("Pick a meal to load to your plan:");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selectedMeal != null){
                            loadMealImageView.setClickable(true);
                            if(selectedMealType == MealEnum.BREAKFAST){
                                //Todo: the adding is somehow working but the deleting isn't so it needs to be fixed and i need to find where im adding the item.
                                User user = BaseActivity.getLoggedInUser();
                                mealViewModel.getMealsMutableLiveData().removeObservers(getViewLifecycleOwner());
                                mealViewModel.getTempoMeals(user.getIdFs());
                                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                                    @Override
                                    public void onChanged(Meals meals) {

                                        boolean changed = updateNutritionValuesAfterChangingMeals(meals, breakfastMealCaloriesTV.getText().toString());

                                        if(changed) {

                                            selectedMeal.setMealEnum(MealEnum.BREAKFAST);
                                            mealViewModel.delete(user.getTodaysMealsIds().get(0), true);
                                            user.getTodaysMealsIds().set(0, selectedMeal.getId());
                                            loadMealToBreakfast(selectedMeal);
                                            userViewModel.update(user);
                                            selectedMeal.setTemp(true);
                                            mealViewModel.add(selectedMeal);
                                            displayPlanData();
                                        }
                                    }
                                });


                            }
                            else if(selectedMealType == MealEnum.LUNCH){
                                User user = BaseActivity.getLoggedInUser();

                                mealViewModel.getTempoMeals(user.getIdFs());
                                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                                    @Override
                                    public void onChanged(Meals meals) {
                                        boolean changed = updateNutritionValuesAfterChangingMeals(meals, lunchMealCaloriesTV.getText().toString());
                                        if(changed) {
                                            selectedMeal.setMealEnum(MealEnum.LUNCH);
                                            selectedMeal.setTemp(true);
                                            mealViewModel.add(selectedMeal);
                                            mealViewModel.delete(user.getTodaysMealsIds().get(1), true);
                                            user.getTodaysMealsIds().set(1, selectedMeal.getId());
                                            loadMealToLunch(selectedMeal);
                                            userViewModel.update(user);

                                            displayPlanData();
                                        }

                                    }
                                });
                            }
                            else if(selectedMealType == MealEnum.DINNER){
                                User user = BaseActivity.getLoggedInUser();

                                mealViewModel.getTempoMeals(user.getIdFs());
                                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                                    @Override
                                    public void onChanged(Meals meals) {
                                        boolean changed = updateNutritionValuesAfterChangingMeals(meals, dinnerMealCaloriesTV.getText().toString());
                                        if(changed) {
                                            selectedMeal.setMealEnum(MealEnum.DINNER);
                                            mealViewModel.delete(user.getTodaysMealsIds().get(2), true);
                                            user.getTodaysMealsIds().set(2, selectedMeal.getId());
                                            loadMealToDinner(selectedMeal);
                                            userViewModel.update(user);
                                            selectedMeal.setTemp(true);
                                            mealViewModel.add(selectedMeal);
                                            displayPlanData();
                                        }

                                    }
                                });
                            }
                        }
                    }
                });
                builder.setOnCancelListener(selection ->
                {
                    loadMealImageView.setClickable(true);
                });

                builder.setOnDismissListener(selection ->
                {
                    loadMealImageView.setClickable(true);
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadMealImageView.setClickable(true);
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });

        reGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = BaseActivity.getLoggedInUser();
                user.getTodaysMealsIds().clear();
                user.setFoodPlanCarbs(0);
                user.setFoodPlanProtein(0);
                user.setFoodPlanFats(0);
                user.setFoodPlanCalories(0);

                userViewModel.update(user);
                mealViewModel.removeTempoFields(user.getIdFs());

                switchScreen = false;
                // Reset the parameters
                caloriesValue.setText("0");
                carbsValue.setText("0");
                fatValue.setText("0");
                proteinValue.setText("0");

                final Observer<Boolean> successObserver = new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            searchRecipes(BaseActivity.getLoggedInUser().getRecommendedCalories());
                        }
                        // Remove observer after it's triggered once
                        mealViewModel.getSuccessOperation().removeObserver(this);
                    }
                };

                mealViewModel.getSuccessOperation().observe(getViewLifecycleOwner(), successObserver);
            }


        });
        breakfastCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchScreen = true;
                getRecipeInformation(BaseActivity.getLoggedInUser().getTodaysMealsIds().get(0), true, 0, null);


            }
        });
        breakfastCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                User user = BaseActivity.getLoggedInUser();

                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                    @Override
                    public void onChanged(Meals meals) {
                        for(Meal meal: meals){
                            if(meal != null && (int) meal.getCalories() == (int) Math.floor(Double.parseDouble(breakfastMealCaloriesTV.getText().toString()))){                                Nutrients nutrients = new Nutrients();
                                nutrients.setCalories(meal.getCalories());
                                nutrients.setCarbs(meal.getCarbs());
                                nutrients.setFat(meal.getFats());
                                nutrients.setProtein(meal.getProtein());

                                Food food = new Food(meal.getImage(), meal.getTitle(), DateUtil.localDateToLong(LocalDate.now()), MealEnum.BREAKFAST, 1, user.getIdFs(), nutrients);
                                foodViewModel.add(food);
                                mealViewModel.getMealsMutableLiveData().removeObserver(this);
                                user.addFood(food);
                                Toast.makeText(getContext(), "Meal added to the diary", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
                mealViewModel.getTempoMeals(user.getIdFs());
                return true;
            }
        });
        lunchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchScreen = true;
                getRecipeInformation(BaseActivity.getLoggedInUser().getTodaysMealsIds().get(1), true, 0, null);


            }
        });
        lunchCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                User user = BaseActivity.getLoggedInUser();

                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                    @Override
                    public void onChanged(Meals meals) {
                        for(Meal meal: meals){
                            if(meal != null && (int) meal.getCalories() == (int) Math.floor(Double.parseDouble(lunchMealCaloriesTV.getText().toString()))){
                                Nutrients nutrients = new Nutrients();
                                nutrients.setCalories(meal.getCalories());
                                nutrients.setCarbs(meal.getCarbs());
                                nutrients.setFat(meal.getFats());
                                nutrients.setProtein(meal.getProtein());

                                Food food = new Food(meal.getImage(), meal.getTitle(), DateUtil.localDateToLong(LocalDate.now()), MealEnum.LUNCH, 1, user.getIdFs(), nutrients);
                                foodViewModel.add(food);
                                mealViewModel.getMealsMutableLiveData().removeObserver(this);
                                user.addFood(food);
                                Toast.makeText(getContext(), "Meal added to the diary", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                mealViewModel.getTempoMeals(user.getIdFs());
                return true;
            }
        });

        dinnerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchScreen = true;
                getRecipeInformation(BaseActivity.getLoggedInUser().getTodaysMealsIds().get(2), true, 0, null);


            }
        });

        dinnerCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                User user = BaseActivity.getLoggedInUser();

                mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
                    @Override
                    public void onChanged(Meals meals) {
                        for(Meal meal: meals){
                            if(meal != null && (int) meal.getCalories() == (int) Math.floor(Double.parseDouble(dinnerMealCaloriesTV.getText().toString()))){                                Nutrients nutrients = new Nutrients();
                                nutrients.setCalories(meal.getCalories());
                                nutrients.setCarbs(meal.getCarbs());
                                nutrients.setFat(meal.getFats());
                                nutrients.setProtein(meal.getProtein());

                                Food food = new Food(meal.getImage(), meal.getTitle(), DateUtil.localDateToLong(LocalDate.now()), MealEnum.DINNER, 1, user.getIdFs(), nutrients);
                                foodViewModel.add(food);
                                mealViewModel.getMealsMutableLiveData().removeObserver(this);
                                user.addFood(food);
                                Toast.makeText(getContext(), "Meal added to the diary", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
                mealViewModel.getTempoMeals(user.getIdFs());
                return true;
            }
        });

        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSidebar();
            }
        });
        resetParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = BaseActivity.getLoggedInUser();

                user.setAllergies(new ArrayList<>());
                user.setDiet(DietEnum.REGULAR);

                userViewModel.update(user);
                itemAdapter.notifyDataSetChanged();
                spinnerAdapter.notifyDataSetChanged();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserPreferences();
            }
        });
        addFoodNameToRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodNameToRemove != null && foodNameToRemove.getText() != null && !foodNameToRemove.getText().toString().isEmpty() && !BaseActivity.getLoggedInUser().getAllergies().contains(foodNameToRemove.getText().toString())) {
                    itemAdapter.addItem(foodNameToRemove.getText().toString());
                    foodNameToRemove.setText("");
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        dietPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DietEnum dietEnum = (DietEnum) (adapterView.getItemAtPosition(i));
                foodDescription.setText(dietEnum.getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadMealToBreakfast(Meal selectedMeal) {
        breakfastMealTitleTV.setText("Title: " + selectedMeal.getTitle());
        breakfastMealReadyInMinutesTV.setText("Ready in Minutes: " + selectedMeal.getReadyInMinutes());
        breakfastMealServingsTV.setText("Servings: " + selectedMeal.getServings());
        breakfastMealCaloriesTV.setText(String.valueOf(selectedMeal.getCalories()));
        Transformation transformation = new RoundedCornersTransformation(300,0);
        Picasso.get().load(selectedMeal.getImage()).transform(transformation).into(breakfastMealImageView);


    }

    private void loadMealToLunch(Meal selectedMeal) {
        lunchMealTitleTV.setText("Title: " + selectedMeal.getTitle());
        lunchMealReadyInMinutesTV.setText("Ready in Minutes: " + selectedMeal.getReadyInMinutes());
        lunchMealServingsTV.setText("Servings: " + selectedMeal.getServings());
        lunchMealCaloriesTV.setText(String.valueOf(selectedMeal.getCalories()));
        Transformation transformation = new RoundedCornersTransformation(300,0);
        Picasso.get().load(selectedMeal.getImage()).transform(transformation).into(lunchMealImageView);


    }

    private void loadMealToDinner(Meal selectedMeal) {
        dinnerMealTitleTV.setText("Title: " + selectedMeal.getTitle());
        dinnerMealReadyInMinutesTV.setText("Ready in Minutes: " + selectedMeal.getReadyInMinutes());
        dinnerMealServingsTV.setText("Servings: " + selectedMeal.getServings());
        dinnerMealCaloriesTV.setText(String.valueOf(selectedMeal.getCalories()));
        Transformation transformation = new RoundedCornersTransformation(300,0);
        Picasso.get().load(selectedMeal.getImage()).transform(transformation).into(dinnerMealImageView);

    }

    private void getSavedMealsFromStorage(){
        User user = BaseActivity.getLoggedInUser();

        mealViewModel.getTempoMeals(user.getIdFs());

        mealViewModel.getMealsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meals>() {
            @Override
            public void onChanged(Meals meals) {
                if (meals != null) {
                    for (Meal meal : meals) {
                        if (user.getTodaysMealsIds().get(0) == meal.getId()) {
                            loadMealToBreakfast(meal);

                        } else if (user.getTodaysMealsIds().get(1) == meal.getId()) {
                            loadMealToLunch(meal);

                        } else if (user.getTodaysMealsIds().get(2) == meal.getId()) {
                            loadMealToDinner(meal);
                            displayPlanData();
                        }
                    }
                }
            }
        });
    }

    public void displayPlanData() {
        User user = BaseActivity.getLoggedInUser();
        caloriesValue.setText(String.format("%.2f", user.getFoodPlanCalories()));
        proteinValue.setText(String.format("%.2f", user.getFoodPlanProtein()));
        fatValue.setText(String.format("%.2f", user.getFoodPlanFats()));
        carbsValue.setText(String.format("%.2f", user.getFoodPlanCarbs()));
    }
    private void saveUserPreferences() {
        User user = BaseActivity.getLoggedInUser();
        user.setDiet((DietEnum) dietPlan.getSelectedItem());
        user.setAllergies(itemAdapter.getItems());
        userViewModel.update(user);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(getContext(), "Preferences saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void openSidebar() {
        // Check if the drawer is closed before opening
        if (!drawerLayout.isDrawerOpen(getView().findViewById(R.id.recipeFilter))) {
            drawerLayout.openDrawer(getView().findViewById(R.id.recipeFilter)); // Replace sidebar_layout with the ID of your sidebar layout
        }
    }
    private void getRecipeInformation(int recipeId, boolean includeNutrition, int counter, Meal meal) {
        Call<RecipeResponse> call = recipeService.getRecipeInformation(recipeId, Api_Key, includeNutrition);
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful()) {
                    recipeResponse = response.body();
                    setMealData(response.body(), counter);
                    if(meal != null) {
                        meal.setMealEnum(MealEnum.getValue(counter));
                        meal.setTemp(true);
                        meal.setUserId(BaseActivity.getLoggedInUser().getIdFs());

                        meal.setCalories(recipeResponse.getCalories());
                        meal.setFats(recipeResponse.getFats());
                        meal.setProtein(recipeResponse.getProteins());
                        meal.setCarbs(recipeResponse.getCarbs());


                        meal.setImage(recipeResponse.getImage());
                        mealViewModel.add(meal);
                    }


                    if(switchScreen){

                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("Recipe", recipeResponse);
                        startActivity(intent);
                    }
                    else{
                        displayPlanData(recipeResponse);
                    }
                } else {
                    System.out.println("Failed to get recipe information. Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                System.out.println("Failed to get recipe information. Error: " + t.getMessage());
            }
        });
    }
    private void searchRecipes(int targetCalories) {

        User user = BaseActivity.getLoggedInUser();
        Call<MealPlanResponse> call = recipeService.searchMealPlanByCalories(Api_Key, "day", targetCalories, user.getStringAllergies() ,(user.getDiet()!= DietEnum.REGULAR) ? user.getDiet().getName():null);

        call.enqueue(new Callback<MealPlanResponse>() {
            @Override
            public void onResponse(Call<MealPlanResponse> call, Response<MealPlanResponse> response) {
                if (response.isSuccessful()) {
                    MealPlanResponse mealPlanResponse = response.body();

                    int counter =1;
                    for(Meal meal : mealPlanResponse.getMeals()){
                        getRecipeInformation(meal.getId(), true, counter, meal);
                        BaseActivity.getLoggedInUser().getTodaysMealsIds().add(meal.getId());
                        counter++;
                    }

                    User user = BaseActivity.getLoggedInUser();
                    user.setFoodPlanCalories(mealPlanResponse.getNutrients().getCalories());
                    user.setFoodPlanFats(mealPlanResponse.getNutrients().getFat());
                    user.setFoodPlanCarbs(mealPlanResponse.getNutrients().getCarbs());
                    user.setFoodPlanProtein(mealPlanResponse.getNutrients().getProtein());

                    userViewModel.update(user);

                    userViewModel.update(BaseActivity.getLoggedInUser());
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve meal plan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MealPlanResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMealData(RecipeResponse recipeResponse, int mealType) {
        switch (mealType) {
            case 1:
                setBreakfastMealData(recipeResponse);
                break;
            case 2:
                setLunchMealData(recipeResponse);
                break;
            case 3:
                setDinnerMealData(recipeResponse);
                break;
            default:
                break;
        }
    }
    private void setBreakfastMealData(RecipeResponse recipeResponse) {
        breakfastMealTitleTV.setText("Title: " + recipeResponse.getTitle());
        breakfastMealReadyInMinutesTV.setText("Ready in Minutes: " + recipeResponse.getReadyInMinutes());
        breakfastMealServingsTV.setText("Servings: " + recipeResponse.getServings());
        breakfastMealCaloriesTV.setText(String.valueOf(recipeResponse.getCalories()));
        Transformation transformation = new RoundedCornersTransformation(300,0);
        Picasso.get().load(recipeResponse.getImage()).transform(transformation).into(breakfastMealImageView);
    }

    private void setLunchMealData(RecipeResponse recipeResponse) {
        lunchMealTitleTV.setText("Title: " + recipeResponse.getTitle());
        lunchMealReadyInMinutesTV.setText("Ready in Minutes: " + recipeResponse.getReadyInMinutes());
        lunchMealServingsTV.setText("Servings: " + recipeResponse.getServings());
        lunchMealCaloriesTV.setText(String.valueOf(recipeResponse.getCalories()));
        Transformation transformation = new RoundedCornersTransformation(300,0);
        Picasso.get().load(recipeResponse.getImage()).transform(transformation).into(lunchMealImageView);
    }

    private void setDinnerMealData(RecipeResponse recipeResponse) {
        dinnerMealTitleTV.setText("Title: " + recipeResponse.getTitle());
        dinnerMealReadyInMinutesTV.setText("Ready in Minutes: " + recipeResponse.getReadyInMinutes());
        dinnerMealServingsTV.setText("Servings: " + recipeResponse.getServings());
        dinnerMealCaloriesTV.setText(String.valueOf(recipeResponse.getCalories()));
        Transformation transformation = new RoundedCornersTransformation(300,0);
        Picasso.get().load(recipeResponse.getImage()).transform(transformation).into(dinnerMealImageView);
    }

}