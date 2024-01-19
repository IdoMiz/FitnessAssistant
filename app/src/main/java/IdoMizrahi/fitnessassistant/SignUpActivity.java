package IdoMizrahi.fitnessassistant;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.helper.inputValidators.EmailRule;
import com.example.helper.inputValidators.NumberRule;
import com.example.helper.inputValidators.PasswordRule;
import com.example.helper.inputValidators.RuleOperation;
import com.example.helper.inputValidators.TextRule;
import com.example.helper.inputValidators.Validator;
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

import co.il.shivhit.helper.DateUtil;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView birthDateEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private Button signUpButton;
    private ImageView datePicker;
    private RadioGroup radioGroupGender;
    private Spinner spinnerGoal;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeViews();
//        setupGenderRadioGroup();
//        setupGoalSpinner();
    }

    private void initializeViews() {
        // Initialize ViewModel
        GenericViewModelFactory<UserViewModel> factory = new GenericViewModelFactory<>(getApplication(), UserViewModel::new);
        userViewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        // Find views
        firstNameEditText = findViewById(R.id.editTextFirstName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        birthDateEditText = findViewById(R.id.textViewBirthDate);
        heightEditText = findViewById(R.id.editTextHeight);
        weightEditText = findViewById(R.id.editTextWeight);
        signUpButton = findViewById(R.id.btnSignUp);
        datePicker = findViewById(R.id.imageViewDatePicker);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        spinnerGoal = findViewById(R.id.spinnerGoal);

        setupGoalSpinner();
        setupGenderRadioGroup();
        setRules();


        setListeners();
    }

    private void setupGenderRadioGroup() {
        // Set a default value for gender
        radioGroupGender.check(R.id.radioButtonMale);
    }

    private void setupGoalSpinner() {
        // Populate the spinner with goal options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.goal_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(adapter);
    }

    public void setListeners() {
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select date");
                builder.setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy"));
                builder.build();
                // הגבלת טווח התאריכים לבחירה
                CalendarConstraints constraint = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    constraint = DateUtil.buidCalendarConstrains(LocalDate.now().minusDays(10), LocalDate.now());
                }
                builder.setCalendarConstraints(constraint);

                // במידה ויש תאריך בתיבת בקלט
                // התאריכון יפתח על התאריך הרשום
                if (!birthDateEditText.getText().toString().isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime date1 =
                                LocalDate.parse(birthDateEditText.getText().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                        ZonedDateTime zdt =
                                ZonedDateTime.of(date1, ZoneId.systemDefault());
                        builder.setSelection(zdt.toInstant().toEpochMilli());
                    }

                MaterialDatePicker picker = builder.build();
                picker.show(getSupportFragmentManager(), picker.toString());

                // Listen to the date selection if needed
                picker.addOnPositiveButtonClickListener(selection ->
                {
                    // Convert the selected date to the desired format and set it in date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    birthDateEditText.setText(dateFormat.format(new java.util.Date((Long) selection)));
                });
            }
        });


        // Set click listener for the sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                signUp();
            }
        });

        spinnerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selection if needed
                String selectedGoal = parentView.getItemAtPosition(position).toString();
                GoalEnum goal = GoalEnum.fromString(selectedGoal);
                // Do something with the selected goal if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });
    }

    private void setRules(){
        Validator.add(new TextRule(firstNameEditText, RuleOperation.TEXT, "Invalid First Name", 2, 20, false, true, null));
        Validator.add(new EmailRule(emailEditText, RuleOperation.REQUIRED, "Invalid Email"));
        Validator.add(new PasswordRule(passwordEditText, RuleOperation.PASSWORD, "Invalid Password", 8, 18));
        Validator.add(new TextRule(birthDateEditText, RuleOperation.DATE, "Invalid Birth Date"));
        Validator.add(new NumberRule(heightEditText, RuleOperation.NUMBER, "Invalid Height", 50, 300));
        Validator.add(new NumberRule(weightEditText, RuleOperation.NUMBER, "Invalid Weight", 2, 300));
    }
    private boolean validateInputs() {
//        return Validator.validate();
        return true;
    }

    private void signUp() {
        if (validateInputs()) {
            // Retrieve user input
            String firstName = firstNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            long birthDate = DateUtil.stringDateToLong(birthDateEditText.getText().toString().trim());
            String heightStr = heightEditText.getText().toString().trim();
            String weightStr = weightEditText.getText().toString().trim();

            // Convert height and weight to appropriate types
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);


            int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            GenderEnum gender = Objects.equals(selectedGenderButton.getText().toString(), "Male") ?
                    GenderEnum.MALE : GenderEnum.FEMALE;

            String selectedGoal = spinnerGoal.getSelectedItem().toString();
            GoalEnum goal = GoalEnum.fromString(selectedGoal);

            // Create a User object
            User user = new User(null, firstName, email, password, birthDate, height, weight, gender, goal);
            // Save the user using the ViewModel
            userViewModel.add(user);

            // Provide feedback to the user
            Toast.makeText(SignUpActivity.this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
