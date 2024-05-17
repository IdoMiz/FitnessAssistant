package IdoMizrahi.fitnessassistant.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.helper.DateUtil;
import com.example.helper.inputValidators.EmailRule;
import com.example.helper.inputValidators.PasswordRule;
import com.example.helper.inputValidators.Rule;
import com.example.helper.inputValidators.RuleOperation;
import com.example.helper.inputValidators.Validator;

import com.example.model.Foods;
import com.example.model.User;
import com.example.viewmodel.FoodViewModel;
import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.MealViewModel;
import com.example.viewmodel.UserViewModel;

import java.time.LocalDate;

import IdoMizrahi.fitnessassistant.R;

public class SignInActivity extends BaseActivity {

    private EditText password, email;
    private TextView title;
    private Button logging;
    private UserViewModel userViewModel;
    private FoodViewModel foodViewModel;
    private MealViewModel mealViewModel;
    private String loggedInUser;
    private static SharedPreferences sharedPreferences;
    private SwitchCompat switchBtn;
    private TextView signUpTv;
    private ProgressBar progressBar;
    private ConstraintLayout mainLayout;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeViews();

    }
    public static String getLoggedInUserFromSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("savedUserId", null);
    }
    public void initializeViews() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        password = findViewById(R.id.passwordET);
        title = findViewById(R.id.titleTV);
        email = findViewById(R.id.emailET);
        logging = findViewById(R.id.loggingBtn);
        switchBtn = findViewById(R.id.saveUser);
        signUpTv = findViewById(R.id.signUpTv);
        progressBar = findViewById(R.id.progressBar);

        mainLayout = findViewById(R.id.mainLayout);

        setRules();
        email.setText("");
        password.setText("");
        switchBtn.setChecked(false);
        progressBar.setVisibility(View.GONE);


        GenericViewModelFactory<UserViewModel> factory = new GenericViewModelFactory<>(getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        GenericViewModelFactory<FoodViewModel> factory2 = new GenericViewModelFactory<>(getApplication(), FoodViewModel::new);
        foodViewModel = new ViewModelProvider(this, factory2).get(FoodViewModel.class);

        GenericViewModelFactory<MealViewModel> factory3 = new GenericViewModelFactory<>(getApplication(), MealViewModel::new);
        mealViewModel = new ViewModelProvider(this, factory3).get(MealViewModel.class);

        handleIfUserIsSaved();

        setListeners();
    }


    public void setListeners() {
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        logging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logging.setClickable(false);
                if (validate())
                {
                    String tempEmail = email.getText().toString();
                    String tempPassword = password.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);

                    userViewModel.getUserLiveData().removeObservers(SignInActivity.this);
                    userViewModel.reset();
                    userViewModel.login(tempEmail, tempPassword);

                    userViewModel.getUserLiveData().observe(SignInActivity.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if (user != null) {
                                loggedInUser = user.getIdFs();
                                if (loggedInUser != null) {
                                    BaseActivity.setLoggedInUser(user);
                                    handleDaysInApp();

                                    if (switchBtn.isChecked()) {
                                        saveUser(loggedInUser);
                                    }

                                    handleRetrievingFoodData(loggedInUser);


                                }
                            } else {
                                logging.setClickable(true);
                                Toast.makeText(SignInActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    logging.setClickable(true);
                }
            }
        });

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });
    }


    public void handleIfUserIsSaved(){
        String loggedInUserId = getLoggedInUserFromSharedPreferences(this);
        if (loggedInUserId != null) {
            progressBar.setVisibility(View.VISIBLE);
            userViewModel.getUser(loggedInUserId);
            userViewModel.getUserLiveData().observe(SignInActivity.this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        BaseActivity.setLoggedInUser(user);
                        handleDaysInApp();
                        handleRetrievingFoodData(loggedInUserId);

                        //finishActivity();
                    }
                }
            });
        }
        else{
            progressBar.setVisibility(View.GONE);
            logging.setClickable(true);
        }
    }

    public void handleDaysInApp(){
        User user = BaseActivity.getLoggedInUser();
        if (DateUtil.localDateToLong(LocalDate.now()) - user.getLastDayLoggedIn() == 1) {
            user.setLastDayLoggedIn(DateUtil.localDateToLong(LocalDate.now()));
            user.setDaysInApp(user.getDaysInApp() + 1);
            user.getTodaysMealsIds().clear();
            mealViewModel.removeTempoFields(user.getIdFs());
            userViewModel.update(user);
        } else if (DateUtil.localDateToLong(LocalDate.now()) - user.getLastDayLoggedIn() > 1) {
            user.setLastDayLoggedIn(DateUtil.localDateToLong(LocalDate.now()));
            user.setDaysInApp(1);
            user.getTodaysMealsIds().clear();
            mealViewModel.removeTempoFields(user.getIdFs());
            userViewModel.update(user);
        }
        BaseActivity.setCurrentDate(DateUtil.localDateToLong(LocalDate.now()));

        BaseActivity.setLoggedInUser(user);
    }

    public void handleRetrievingFoodData(String loggedInUser1){
        foodViewModel.getFoodsMutableLiveData().observe(SignInActivity.this, new Observer<Foods>() {
            @Override
            public void onChanged(Foods foods) {
                if(foods != null) {
                    User user = BaseActivity.getLoggedInUser();
                    user.setHistory(foods);
                    BaseActivity.setLoggedInUser(user);
                }
                loggedInUser = null;
                finishActivity();
            }
        });
        foodViewModel.getAll(loggedInUser1);

    }

    public void setRules(){
        Validator.add(new EmailRule(email, RuleOperation.TEXT, "Invalid Email"));
        Validator.add(new Rule(email,RuleOperation.REQUIRED,"Required"));

        Validator.add(new PasswordRule(password, RuleOperation.PASSWORD, "Invalid Password", 5, 18));
        Validator.add(new Rule(password, RuleOperation.REQUIRED,"Required"));
    }

    public boolean validate(){
        return Validator.validate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the other activity
        if (requestCode == REQUEST_CODE) {
            email.setText("");
            password.setText("");
            switchBtn.setChecked(false);
            logging.setClickable(true);
            progressBar.setVisibility(View.GONE);
            Validator.cleanValidators();
            setRules();
        }
    }
    public void finishActivity(){

        BaseActivity.setCurrentDate(DateUtil.localDateToLong(LocalDate.now()));
        Intent intent = new Intent(SignInActivity.this, ControlActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    public void saveUser(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedUserId", id);
        editor.apply();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}