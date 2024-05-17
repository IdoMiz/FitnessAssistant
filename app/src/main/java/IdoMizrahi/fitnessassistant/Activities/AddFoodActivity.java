package IdoMizrahi.fitnessassistant.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Food;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import IdoMizrahi.fitnessassistant.R;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AddFoodActivity extends BaseActivity {

    private TextView foodName;
    private TextView addFoodTitle;
    private TextView numOfServings;
    private TextView carbsTV;
    private TextView proteinTV;
    private TextView fatsTV;
    private Button addBtn;
    private TextView carbsAmount;
    private ImageView arrowBtn;
    private TextView fatsAmount;
    private TextView proteinsAmount;
    private EditText numOfServingsET;
    private TextView caloriesAmount;
    private TextView caloriesTV;
    private Food food;
    private ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        initializeViews();
    }

    @Override
    protected void initializeViews() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("FoodItem")) {
            food = (Food) intent.getSerializableExtra("FoodItem");
            if (food == null) {
                finish();
                return;
            }
        } else {
            finish();
            return;
        }


        caloriesAmount = findViewById(R.id.caloriesAmount);
        caloriesTV = findViewById(R.id.caloriesTV);
        foodName = findViewById(R.id.foodName);
        addFoodTitle = findViewById(R.id.addFoodTitle);
        numOfServings = findViewById(R.id.numOfServings);
        carbsTV = findViewById(R.id.carbsTV);
        proteinTV = findViewById(R.id.proteinTV);
        fatsTV = findViewById(R.id.fatsTV);
        addBtn = findViewById(R.id.addBtn);
        carbsAmount = findViewById(R.id.carbsAmount);
        fatsAmount = findViewById(R.id.fatsAmount);
        proteinsAmount = findViewById(R.id.proteinsAmount);
        numOfServingsET = findViewById(R.id.numOfServingsET);
        foodImage = findViewById(R.id.foodImage);
        arrowBtn = findViewById(R.id.arrowBtn);

        if (food != null) {
            foodName.setText(food.getLabel());
            Transformation transformation = new RoundedCornersTransformation(30,0);
            Picasso.get().load(food.getImage()).transform(transformation).into(foodImage);
            numOfServingsET.setText(String.valueOf(food.getServing()));

        } else {
            finish();
            return;
        }

        setListeners();
        changeFieldsText();
    }


    public void changeFieldsText() {
        if (food != null) {
            double servings = Double.parseDouble(numOfServingsET.getText().toString());
            servings = (servings > 0) ? servings : 1;

            double carbs = (double) (Math.round(100* food.getNutrients().getCarbs() * servings)/100);
            double protein = (double) (Math.round(100* food.getNutrients().getProtein() * servings)/100);
            double fats = (double) (Math.round(100* food.getNutrients().getFat() * servings)/100);
            double calories = (double) (Math.round(100* food.getNutrients().getCalories() * servings)/100);

            carbsAmount.setText(String.valueOf(carbs));
            proteinsAmount.setText(String.valueOf(protein));
            fatsAmount.setText(String.valueOf(fats));
            caloriesAmount.setText(String.valueOf(calories));
        }
    }


    @Override
    protected void setListeners() {
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        numOfServingsET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changeFieldsText();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food != null) {
                    double servings = Double.parseDouble(numOfServingsET.getText().toString());
                    servings = (servings > 0) ? servings : 1;

                    food.setServing(servings);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NewFood", food);
                    setResult(Activity.RESULT_OK,resultIntent);
                    finish();
                }
            }
        });
    }
}
