package IdoMizrahi.fitnessassistant;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.model.User;
import com.example.viewmodel.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText birthDateEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private Button signUpButton;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Find views
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        birthDateEditText = findViewById(R.id.editTextBirthDate);
        heightEditText = findViewById(R.id.editTextHeight);
        weightEditText = findViewById(R.id.editTextWeight);
        signUpButton = findViewById(R.id.btnSignUp);

        // Set click listener for the sign-up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                signUp();
            }
        });
    }

    private void signUp() {
        // Retrieve user input
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String birthDate = birthDateEditText.getText().toString().trim();
        String heightStr = heightEditText.getText().toString().trim();
        String weightStr = weightEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(birthDate) || TextUtils.isEmpty(heightStr) ||
                TextUtils.isEmpty(weightStr)) {
            // Handle validation error (show an error message, etc.)
            Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert height and weight to appropriate types
        double height = Double.parseDouble(heightStr);
        double weight = Double.parseDouble(weightStr);

        // Create a User object
        User user = new User(null, firstName, lastName, email, password, birthDate, height, weight);

        // Save the user using the ViewModel
        userViewModel.add(user);

        // Provide feedback to the user
        Toast.makeText(SignUpActivity.this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
