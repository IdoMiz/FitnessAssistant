/*
package com.example.junk;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.helper.DateUtil;
import com.example.model.Foods;
import com.example.model.User;
import com.example.viewmodel.FoodViewModel;
import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.MealViewModel;
import com.example.viewmodel.UserViewModel;

import java.time.LocalDate;

import IdoMizrahi.fitnessassistant.R;

// can delete this
public class MainActivity extends BaseActivity {

    private Button signIn;
    private Button signUp;
    private TextView title;
    private static SharedPreferences sharedPreferences;
    private UserViewModel userViewModel;
    private FoodViewModel foodViewModel;
    private MealViewModel mealViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        initializeViews();
    }

    public static String getLoggedInUserFromSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("savedUserId", null);
    }

    public void initializeViews() {

        signIn = findViewById(R.id.signInButton);
        signUp = findViewById(R.id.signUpButton);

        title = findViewById(R.id.titleTV);

        GenericViewModelFactory<UserViewModel> factory1 = new GenericViewModelFactory<>(getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory1).get(UserViewModel.class);

        GenericViewModelFactory<FoodViewModel> factory2 = new GenericViewModelFactory<>(getApplication(), FoodViewModel::new);
        foodViewModel = new ViewModelProvider(this, factory2).get(FoodViewModel.class);

        GenericViewModelFactory<MealViewModel> factory3 = new GenericViewModelFactory<>(getApplication(), MealViewModel::new);
        mealViewModel = new ViewModelProvider(this, factory3).get(MealViewModel.class);

        String loggedInUserId = getLoggedInUserFromSharedPreferences(this);
        if (loggedInUserId != null) {
            userViewModel.getUser(loggedInUserId);

            userViewModel.getUserLiveData().observe(MainActivity.this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        BaseActivity.setLoggedInUser(user);
                        foodViewModel.getAll(loggedInUserId);
                        foodViewModel.getFoodsMutableLiveData().observe(MainActivity.this, new Observer<Foods>() {
                            @Override
                            public void onChanged(Foods foods) {
                                User user1 = BaseActivity.getLoggedInUser();
                                user1.setHistory(foods);
                                if(DateUtil.localDateToLong(LocalDate.now())- user1.getLastDayLoggedIn()== 1){
                                    user1.setLastDayLoggedIn(DateUtil.localDateToLong(LocalDate.now()));
                                    user1.setDaysInApp(user1.getDaysInApp()+1);
                                    user1.getTodaysMealsIds().clear();
                                    userViewModel.update(user1);
                                    mealViewModel.removeTempoFields(user1.getIdFs());
                                }
                                else if(DateUtil.localDateToLong(LocalDate.now())- user1.getLastDayLoggedIn()>1){
                                    user1.setLastDayLoggedIn(DateUtil.localDateToLong(LocalDate.now()));
                                    user1.setDaysInApp(1);
                                    user1.getTodaysMealsIds().clear();
                                    userViewModel.update(user1);
                                    mealViewModel.removeTempoFields(user1.getIdFs());
                                }

                                BaseActivity.setLoggedInUser(user1);
                                BaseActivity.setCurrentDate(DateUtil.localDateToLong(LocalDate.now()));
                                Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
        }
        setListeners();
    }


    public void setListeners() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

 */