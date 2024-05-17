package IdoMizrahi.fitnessassistant.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helper.DateUtil;
import com.example.model.Food;
import com.example.model.FoodApiService;
import com.example.model.Foods;


import com.example.model.MealEnum;
import com.example.model.User;
import com.example.viewmodel.FoodViewModel;
import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.UserViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import IdoMizrahi.fitnessassistant.Adapters.FoodAdapter;
import IdoMizrahi.fitnessassistant.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogFoodFragment extends Fragment {

    private TextView titleTextView;
    private TextView dateTV;
    //private ImageView datePicker;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private FoodAdapter breakfastAdapter;
    private FoodAdapter lunchAdapter;
    private FoodAdapter dinnerAdapter;
    private Button searchButton1;
    private Button searchButton2;
    private Button searchButton3;
    private Retrofit retrofit;
    private final String baseUrl = "https://api.nal.usda.gov/fdc/v1/";
    private final String apiKey = "xI4mYU0nWDtjDlR94hkysl9L1u5pBfKZGj7jheIe";
    private FoodApiService apiService;
    private FoodAdapter selectedFoodAdapter;
    private static final int SEARCH_FOOD_REQUEST_CODE = 1;
    private Foods breakfastFoods;
    private Foods lunchFoods;
    private Foods dinnerFoods;
    private UserViewModel userViewModel;
    private FoodViewModel foodViewModel;
    private MealEnum selectedMeal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
    }

    private void launchSearchFoodActivity() {
        Intent intent = new Intent(getActivity(), SearchFoodActivity.class);
        startActivityForResult(intent, SEARCH_FOOD_REQUEST_CODE);
    }

    // Handle the result from SearchFoodActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_FOOD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                List<Food> rFood = (List<Food>) data.getSerializableExtra("returnedFoods");
                Foods returnedFoods = new Foods();
                returnedFoods.addAll(rFood);
                if (returnedFoods != null && !returnedFoods.isEmpty()) {
                    if (selectedFoodAdapter != null) {
                        selectedFoodAdapter.addFoods(returnedFoods);
                    }

                    if (selectedMeal != null) {
                        for (Food food : returnedFoods) {
                            food.setMeal(selectedMeal);
                            food.setDate(BaseActivity.getCurrentDate());
                            BaseActivity.getLoggedInUser().addFood(food);
                        }
                    }

                    loadToUserFromScreen(returnedFoods);
                    Toast.makeText(getContext(), "Trying to save to user", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initializeViews(View view) {
        titleTextView = view.findViewById(R.id.titleTextView);
        dateTV = view.findViewById(R.id.dateTV);
        //datePicker = view.findViewById(R.id.datePicker);

        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);

        searchButton1 = view.findViewById(R.id.searchButton1);
        searchButton2 = view.findViewById(R.id.searchButton2);
        searchButton3 = view.findViewById(R.id.searchButton3);

        breakfastFoods = new Foods();
        lunchFoods = new Foods();
        dinnerFoods = new Foods();

        setRecyclerView();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(FoodApiService.class);

        GenericViewModelFactory<UserViewModel> factory = new GenericViewModelFactory<>(requireActivity().getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        GenericViewModelFactory<FoodViewModel> factory2 = new GenericViewModelFactory<>(requireActivity().getApplication(), FoodViewModel::new);
        foodViewModel = new ViewModelProvider(this, factory2).get(FoodViewModel.class);

        dateTV.setText(DateUtil.longDateToString(BaseActivity.getCurrentDate()));
        loadToScreenFromUser(String.valueOf(dateTV.getText()));

        setListeners();
    }

    public void loadToScreenFromUser(String date) {
        User loggedUser = BaseActivity.getLoggedInUser();
        Foods breakfast = loggedUser.getBreakfast(DateUtil.stringDateToLong(date));
        Foods lunch = loggedUser.getLunch(DateUtil.stringDateToLong(date));
        Foods dinner = loggedUser.getDinner(DateUtil.stringDateToLong(date));


        if (breakfast != null) {
            breakfastAdapter.setFoods(breakfast);
        }
        if (lunch != null) {
            lunchAdapter.setFoods(lunch);
        }
        if (dinner != null) {
            dinnerAdapter.setFoods(dinner);
        }
    }

    public void loadToUserFromScreen(Foods returnedFoods) {
        User loggedUser = BaseActivity.getLoggedInUser();

        foodViewModel.addAll(returnedFoods, loggedUser.getIdFs());
    }

    private void checkIfDelete(Food food, FoodAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to proceed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                foodViewModel.delete(food.getIdFs());
                foodViewModel.getSuccessOperation().observe(getActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            Toast.makeText(getContext(), "Food deleted!", Toast.LENGTH_SHORT).show();
                            adapter.removeFood(food);
                            BaseActivity.getLoggedInUser().removeFood(food);
                        }
                        else{
                            Toast.makeText(getContext(), "Couldn't not delete food!", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
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

    private void setRecyclerView() {
        // Create breakfast adapter
        breakfastAdapter = new FoodAdapter(getContext(), breakfastFoods, R.layout.food_card, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Food food) {
                // needs to edit the amount of the food
            }
        }, new FoodAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Food food) {

                checkIfDelete(food, breakfastAdapter);

                return true;
            }
        }, new FoodAdapter.OnPlusButtonClickListener() {
            @Override
            public void onPlusButtonClick(Food food) {
                //Todo: need to fix the bag where the values that are added will be saved
                foodViewModel.update(food);
            }
        }, new FoodAdapter.OnMinusButtonClickListener() {
            @Override
            public void onMinusButtonClick(Food food) {
                foodViewModel.update(food);
            }
        });

        recyclerView1.setAdapter(breakfastAdapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create lunch adapter
        lunchAdapter = new FoodAdapter(getContext(), lunchFoods, R.layout.food_card, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Food food) {
                // Handle item click for lunch
            }
        }, new FoodAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Food food) {

                checkIfDelete(food, lunchAdapter);

                return true;

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
        recyclerView2.setAdapter(lunchAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create dinner adapter
        dinnerAdapter = new FoodAdapter(getContext(), dinnerFoods, R.layout.food_card, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Food food) {
                // Handle item click for dinner
            }
        }, new FoodAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Food food) {

                checkIfDelete(food, dinnerAdapter);

                return true;
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
        recyclerView3.setAdapter(dinnerAdapter);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setListeners() {
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTV.setClickable(false);
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select date");
                builder.setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy"));
                builder.build();
                // Set date range constraints
                CalendarConstraints constraint = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    constraint = DateUtil.buidCalendarConstrains(LocalDate.now().minusDays(10), LocalDate.now());
                }
                builder.setCalendarConstraints(constraint);

                // Open date picker
                MaterialDatePicker picker = builder.build();
                picker.show(requireActivity().getSupportFragmentManager(), picker.toString());

                // Listen to date selection
                picker.addOnPositiveButtonClickListener(selection ->
                {
                    // Convert selected date to the desired format and set it
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    dateTV.setText(dateFormat.format(new java.util.Date((Long) selection)));
                    loadToScreenFromUser(String.valueOf(dateTV.getText()));
                    BaseActivity.setCurrentDate(DateUtil.stringDateToLong((String) dateTV.getText()));
                });
                picker.addOnNegativeButtonClickListener(selection ->
                {
                    dateTV.setClickable(true);
                });
                picker.addOnCancelListener(selection ->
                {
                    dateTV.setClickable(true);
                });
                picker.addOnDismissListener(selection ->
                {
                    dateTV.setClickable(true);
                });
            }
        });

        searchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMeal = MealEnum.BREAKFAST;
                selectedFoodAdapter = breakfastAdapter;
                launchSearchFoodActivity();
            }
        });

        searchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMeal = MealEnum.LUNCH;
                selectedFoodAdapter = lunchAdapter;
                launchSearchFoodActivity();
            }
        });

        searchButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedMeal = MealEnum.DINNER;
                selectedFoodAdapter = dinnerAdapter;
                launchSearchFoodActivity();
            }
        });
    }
}
