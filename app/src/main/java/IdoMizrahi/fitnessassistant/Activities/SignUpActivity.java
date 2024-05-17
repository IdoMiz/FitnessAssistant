package IdoMizrahi.fitnessassistant.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.helper.DateUtil;
import com.example.helper.inputValidators.DateRule;
import com.example.helper.inputValidators.EmailRule;
import com.example.helper.inputValidators.NameRule;
import com.example.helper.inputValidators.NumberRule;
import com.example.helper.inputValidators.PasswordRule;
import com.example.helper.inputValidators.Rule;
import com.example.helper.inputValidators.RuleOperation;
import com.example.helper.inputValidators.Validator;
import com.example.model.ActivityLevel;
import com.example.model.GenderEnum;
import com.example.model.GoalEnum;
import com.example.model.User;
import com.example.viewmodel.GenericViewModelFactory;
import com.example.viewmodel.UserViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import IdoMizrahi.fitnessassistant.R;


public class SignUpActivity extends BaseActivity{
    private TextView title;
    private EditText firstNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText textViewBirthDate;
    private EditText heightEditText;
    private EditText weightEditText;
    private Button signUpButton;
    private RadioGroup radioGroupGender;
    private UserViewModel userViewModel;
    private Spinner spinnerGoal;
    private Spinner spinnerActivityLevel;
    private boolean isNew;
    private ImageView backArrow;
    private ConstraintLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
    }

    public void initializeViews() {
        GenericViewModelFactory<UserViewModel> factory = new GenericViewModelFactory<>(getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        firstNameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        textViewBirthDate = findViewById(R.id.textViewBirthDate);
        heightEditText = findViewById(R.id.editTextHeight);
        weightEditText = findViewById(R.id.editTextWeight);
        signUpButton = findViewById(R.id.btnSignUp);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        spinnerGoal = findViewById(R.id.spinnerGoal);
        spinnerActivityLevel = findViewById(R.id.spinnerActivityLevel);
        isNew = true;
        title = findViewById(R.id.textViewTitle);
        backArrow = findViewById(R.id.backArrow);
        mainLayout = findViewById(R.id.mainLayout);



        setupGoalSpinner();
        setRules();
        setListeners();
        fillUser();
    }

    public void fillUser() {
        User user = BaseActivity.loggedInUser;
        if (user != null) {
            firstNameEditText.setText(user.getName());
            emailEditText.setText(user.getEmail());
            passwordEditText.setText(user.getPassword());
            textViewBirthDate.setText(DateUtil.longDateToString(user.getBirthDate()));
            heightEditText.setText(String.valueOf(user.getHeight()));
            weightEditText.setText(String.valueOf(user.getWeight()));
            if (user.getGender() == GenderEnum.MALE) {
                radioGroupGender.check(R.id.radioButtonMale);
            } else {
                radioGroupGender.check(R.id.radioButtonFemale);
            }
            int goalPosition = getIndex(spinnerGoal, user.getGoal());
            spinnerGoal.setSelection(goalPosition);
            int activityLevelPosition = getIndex(spinnerActivityLevel, user.getActivityLevel());
            spinnerActivityLevel.setSelection(activityLevelPosition);
            isNew = false;
            title.setText("Edit User");
            signUpButton.setText("Save");

        }
    }

    private int getIndex(Spinner spinner, Enum<?> value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                return i;
            }
        }
        return 0;
    }


    private void setupGoalSpinner() {
        ArrayAdapter<GoalEnum> adapterGoal = new ArrayAdapter<>(this, R.layout.new_spinner_layout, GoalEnum.values());
        adapterGoal.setDropDownViewResource(R.layout.new_spinner_layout);
        spinnerGoal.setAdapter(adapterGoal);
        spinnerGoal.setSelection(0);

        ArrayAdapter<ActivityLevel> adapterActivityLevel = new ArrayAdapter<>(this, R.layout.new_spinner_layout, ActivityLevel.values());
        adapterActivityLevel.setDropDownViewResource(R.layout.new_spinner_layout);
        spinnerActivityLevel.setAdapter(adapterActivityLevel);
        spinnerActivityLevel.setSelection(0);

    }

    public void setListeners() {
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });
        textViewBirthDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                textViewBirthDate.setLongClickable(false);
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select date");
                builder.setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy"));
                builder.build();
                CalendarConstraints constraint = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    constraint = DateUtil.buidCalendarConstrains(LocalDate.now().minusYears(120), LocalDate.now());
                }
                builder.setCalendarConstraints(constraint);
                if (!textViewBirthDate.getText().toString().isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Validator.cleanValidators();
                        Validator.add(new Rule(textViewBirthDate, RuleOperation.REQUIRED, "Required"));
                        Validator.add(new DateRule(textViewBirthDate, RuleOperation.DATE, "Invalid Date", LocalDate.now().minusYears(120), LocalDate.now()));
                        if(Validator.validate()) {
                            LocalDateTime date1 = LocalDate.parse(textViewBirthDate.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            ZonedDateTime zdt = ZonedDateTime.of(date1, ZoneId.systemDefault());
                            builder.setSelection(zdt.toInstant().toEpochMilli());
                        }
                        setRules();
                    }

                MaterialDatePicker picker = builder.build();
                picker.show(getSupportFragmentManager(), picker.toString());

                picker.addOnPositiveButtonClickListener(selection ->
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    textViewBirthDate.setText(dateFormat.format(new java.util.Date((Long) selection)));
                    textViewBirthDate.setLongClickable(true);
                });
                picker.addOnNegativeButtonClickListener(selection ->
                {
                    textViewBirthDate.setLongClickable(true);
                });
                picker.addOnCancelListener(selection ->
                {
                    textViewBirthDate.setLongClickable(true);
                });
                picker.addOnDismissListener(selection ->
                {
                    textViewBirthDate.setLongClickable(true);
                });

                return false;
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signUpButton.setOnClickListener(view -> {
            signUpButton.setClickable(false);
            signUp();
        });


        spinnerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                GoalEnum selectedGoal = (GoalEnum) parentView.getItemAtPosition(position);
                if (selectedGoal == null || selectedGoal == GoalEnum.PICK_GOAL) {
                    spinnerGoal.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                spinnerGoal.setSelection(0);
            }
        });
        spinnerActivityLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ActivityLevel selectedActivityLevel = (ActivityLevel) parentView.getItemAtPosition(position);
                if(selectedActivityLevel == null ||selectedActivityLevel == ActivityLevel.PICK_ACTIVITY_LEVEL) {
                    spinnerActivityLevel.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerActivityLevel.setSelection(0);
            }
        });
    }

    private void setRules() {
        Validator.cleanValidators();
        Validator.add(new Rule(firstNameEditText,RuleOperation.REQUIRED,"Required"));
        Validator.add(new NameRule(firstNameEditText, RuleOperation.NAME, "Invalid Name"));

        Validator.add(new Rule(emailEditText,RuleOperation.REQUIRED,"Required"));
        Validator.add(new EmailRule(emailEditText, RuleOperation.REQUIRED, "Invalid Email"));

        Validator.add(new Rule(passwordEditText, RuleOperation.REQUIRED,"Required"));
        Validator.add(new PasswordRule(passwordEditText, RuleOperation.PASSWORD, "Invalid Password", 5, 18));

        Validator.add(new Rule(textViewBirthDate, RuleOperation.REQUIRED, "Required"));
        Validator.add(new DateRule(textViewBirthDate, RuleOperation.DATE, "Invalid Date", LocalDate.now().minusYears(120), LocalDate.now()));

        Validator.add(new Rule(heightEditText,RuleOperation.REQUIRED,"Required"));
        Validator.add(new NumberRule(heightEditText, RuleOperation.REQUIRED, "Invalid Height", 50, 300));

        Validator.add(new Rule(weightEditText,RuleOperation.REQUIRED,"Required"));
        Validator.add(new NumberRule(weightEditText, RuleOperation.REQUIRED, "Invalid Weight", 2, 300));

        Validator.add(new Rule(radioGroupGender, RuleOperation.REQUIRED, "Required"));

        Validator.add(new Rule(spinnerGoal, RuleOperation.REQUIRED, "Required"));

        Validator.add(new Rule(spinnerActivityLevel, RuleOperation.REQUIRED, "Required"));

    }

    private boolean validateInputs() {
        return Validator.validate();
    }

    private void signUp() {
        if (validateInputs()) {

            String firstName = firstNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            long birthDate = DateUtil.stringDateToLong(textViewBirthDate.getText().toString().trim());
            String heightStr = heightEditText.getText().toString().trim();
            String weightStr = weightEditText.getText().toString().trim();

            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);

            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            GenderEnum gender = Objects.equals(selectedGenderButton.getText().toString(), "Male") ? GenderEnum.MALE : GenderEnum.FEMALE;

            GoalEnum goal = (GoalEnum) spinnerGoal.getSelectedItem();

            ActivityLevel activityLevel = (ActivityLevel) spinnerActivityLevel.getSelectedItem();

            if(!isNew){
                User user = BaseActivity.getLoggedInUser();
                user.setName(firstName);
                user.setEmail(email);
                user.setPassword(password);
                user.setBirthDate(birthDate);
                user.setHeight(height);
                user.setWeight(weight);
                user.setActivityLevel(activityLevel);
                user.setGender(gender);
                user.setGoal(goal);
                userViewModel.update(user);

                Toast.makeText(SignUpActivity.this, "User Updated!", Toast.LENGTH_SHORT).show();
                signUpButton.setClickable(true);
                finish();
            }
            else {

                User user = new User(firstName, email, password, birthDate, height, weight, gender, goal, activityLevel, DateUtil.localDateToLong(LocalDate.now()));
                userViewModel.add(user);
                BaseActivity.setLoggedInUser(user);

                Toast.makeText(SignUpActivity.this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
                signUpButton.setClickable(true);
                Intent intent = new Intent(SignUpActivity.this, ControlActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            signUpButton.setClickable(true);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
