package IdoMizrahi.fitnessassistant.Activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.model.CircleProgressBar;

import com.example.model.User;

import java.time.LocalDate;

import IdoMizrahi.fitnessassistant.R;

public class HomeScreenFragment extends Fragment {
    private TextView title;
    private CircleProgressBar circleProgressBar;
    private TextView caloriesAmount, caloriesTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View rootView) {
        title = rootView.findViewById(R.id.title11);
        caloriesAmount = rootView.findViewById(R.id.caloriesAmount);
        caloriesTV = rootView.findViewById(R.id.caloriesTV2);
        circleProgressBar = rootView.findViewById(R.id.circleProgressBar);

        User user = BaseActivity.getLoggedInUser();

        if(user != null){
            double cal = user.getConsumedCalories(BaseActivity.getCurrentDate());
            caloriesAmount.setText(String.valueOf((int) cal) + '/' + String.valueOf(user.getRecommendedCalories()));
            circleProgressBar.setProgress((float) (cal / user.getRecommendedCalories()));
        }
    }
}

