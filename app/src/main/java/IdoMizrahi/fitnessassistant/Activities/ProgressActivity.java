package IdoMizrahi.fitnessassistant.Activities;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.helper.DateUtil;
import com.example.viewmodel.FoodViewModel;
import com.example.viewmodel.GenericViewModelFactory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import IdoMizrahi.fitnessassistant.R;

public class ProgressActivity extends BaseActivity {
    private TextView title;
    private BarChart chart;
    private TextView dateTV;
    private List<BarEntry> barEntryList;
    private FoodViewModel foodViewModel;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        initializeViews();
    }

    @Override
    protected void initializeViews() {
        title = findViewById(R.id.title);
        chart = findViewById(R.id.chart1);
        dateTV = findViewById(R.id.dateTV2);
        backArrow = findViewById(R.id.backArrow);

        barEntryList = new ArrayList<>();

        GenericViewModelFactory<FoodViewModel> factory = new GenericViewModelFactory<>(getApplication(), FoodViewModel::new);
        foodViewModel = new ViewModelProvider(this, factory).get(FoodViewModel.class);

        dateTV.setText(DateUtil.longDateToString(BaseActivity.getCurrentDate()));
        setListeners();

        setupBarChart();
    }

    @Override
    protected void setListeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // creates a dialog to pick a date
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
                    constraint = DateUtil.buidCalendarConstrains(LocalDate.now().minusYears(120), LocalDate.now());
                }
                builder.setCalendarConstraints(constraint);

                MaterialDatePicker picker = builder.build();
                picker.show(getSupportFragmentManager(), picker.toString());

                picker.addOnPositiveButtonClickListener(selection -> {
                    dateTV.setClickable(true);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    dateTV.setText(dateFormat.format(new Date((Long) selection)));
                    setupBarChart();
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
    }
    public void setupBarChart() {
        long selectedDate = DateUtil.stringDateToLong((String) dateTV.getText());
        chart.removeAllViews();
        chart.clear();
        chart.refreshDrawableState();
        chart.getAxisLeft().removeAllLimitLines();


        getValuesToBarEntry(selectedDate);

        // Calculate the average and expected calorie intake
        double averageCalories = calculateAverageCalories(selectedDate) / 7;
        double expectedCalories = BaseActivity.getLoggedInUser().getRecommendedCalories();

        BarDataSet dataSet = new BarDataSet(barEntryList, "Calories Eaten");
        dataSet.setColor(Color.rgb(0, 128, 0)); // Set color for the bars
        dataSet.setValueTextColor(Color.BLACK); // Set color for the value text
        dataSet.setValueTextSize(15);
        Legend legend = chart.getLegend(); // square thing that contains the name of the dataset
        legend.setTextSize(17f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.75f); // Adjust the width of the bars

        chart.setData(barData);
        chart.setPinchZoom(false);

        // Customize X-axis
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                long date = selectedDate - (6-(long) value);
                String formattedDate = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(new Date(date));
                String date1 = DateUtil.longDateToString(date);
                return date1.substring(0, 5);
            }
        });
        chart.getXAxis().setGranularity(1); // Ensure only one label per day
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Move X-axis labels to the bottom

        // Add limit lines for average and expected values
        LimitLine averageLine = new LimitLine((float) averageCalories, "Average: "+ String.valueOf((int) averageCalories));
        averageLine.setLineWidth(2);
        averageLine.enableDashedLine(10, 10, 0);
        averageLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        averageLine.setTextSize(20);

        LimitLine expectedLine = new LimitLine((float) expectedCalories, "Goal: "+ String.valueOf((int) expectedCalories));
        expectedLine.setLineWidth(2);
        expectedLine.enableDashedLine(10, 10, 0);
        expectedLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        expectedLine.setTextSize(20);

        // Add limit lines to the left axis
        chart.getAxisLeft().addLimitLine(averageLine);
        chart.getAxisLeft().addLimitLine(expectedLine);

        // Ensure the minimum value on the left axis is 0
        chart.getAxisLeft().setAxisMinimum(0);

        // Set fixed limit for the goal line
        double maxValue = barEntryList.stream()
                .mapToDouble(BarEntry::getY)
                .max()
                .orElse(0.0);
        double leftAxisMaximum = Math.max(expectedCalories + 400, maxValue + 200);
        chart.getAxisLeft().setAxisMaximum((float) leftAxisMaximum);

        // Disable the right axis
        chart.getAxisRight().setEnabled(false);


        chart.getXAxis().setTextSize(15);
        chart.getAxisLeft().setTextSize(15);

        chart.setFitBars(true); // Adjusts the size of bars to fit the chart width
        chart.invalidate(); // Refreshes the chart
    }


    // calculates the average calories for the last 7 days
    private double calculateAverageCalories(long selectedDate) {
        double sum =0;

        for (int i = 0;i<7; i++) {
            sum += barEntryList.get(i).getY();
        }

        return sum;
    }

    // gets the values for the bar chart
    public void getValuesToBarEntry(long endingDate) {
        barEntryList.clear();
        for (int i = 6; i >= 0; i--) {
            barEntryList.add(new BarEntry(6 - i, (float) BaseActivity.getLoggedInUser().getConsumedCalories(endingDate - i)));
        }
    }

}