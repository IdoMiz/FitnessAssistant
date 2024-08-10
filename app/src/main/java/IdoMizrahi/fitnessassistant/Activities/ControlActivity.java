package IdoMizrahi.fitnessassistant.Activities;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import IdoMizrahi.fitnessassistant.R;

public class ControlActivity extends BaseActivity {

    private NavController navController;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        initializeViews();
    }

    public void initializeViews() {
        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        // Initialize NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        // Initialize NavController
        navController = navHostFragment.getNavController();
        // Connect NavController to BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigation, navController);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {

    }
}
