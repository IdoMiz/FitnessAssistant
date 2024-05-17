package IdoMizrahi.fitnessassistant.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.model.User;

import IdoMizrahi.fitnessassistant.R;

public abstract class BaseActivity extends AppCompatActivity {

    public static User loggedInUser;
    public static long currentDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        BaseActivity.loggedInUser = loggedInUser;
    }

    public static long getCurrentDate() {
        return currentDate;
    }

    public static void setCurrentDate(long currentDate) {
        BaseActivity.currentDate = currentDate;
    }

    protected abstract void initializeViews();
    protected abstract void setListeners();
}