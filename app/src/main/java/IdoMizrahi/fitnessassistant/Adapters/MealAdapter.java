package IdoMizrahi.fitnessassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Meal;
import com.example.model.Meals;
import com.squareup.picasso.Picasso;

import IdoMizrahi.fitnessassistant.R;
import com.squareup.picasso.Transformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealHolder> {

    public interface OnItemClickListener {
        public void onItemClicked(Meal meal);

    }

    public interface OnItemLongClickListener {

        public boolean onItemLongClicked(Meal meal);
    }

    private Context context;
    private Meals meals;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public MealAdapter(Context context, Meals meals, OnItemClickListener onItemClickListener,  OnItemLongClickListener onItemLongClickListener) {
        this.context = context;
        this.meals = meals;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public MealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_card, parent, false);
        return new MealHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public class MealHolder extends RecyclerView.ViewHolder {

        private TextView titleTV, readyInMinutesTV, servingsTV, caloriesTV;
        private ImageView mealImageView;

        public MealHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            readyInMinutesTV = itemView.findViewById(R.id.readyInMinutesTV);
            servingsTV = itemView.findViewById(R.id.servingsTV);
            caloriesTV = itemView.findViewById(R.id.caloriesTV);
            mealImageView = itemView.findViewById(R.id.mealImageView);
        }

        public void bind(Meal meal) {
            titleTV.setText(meal.getTitle());
            readyInMinutesTV.setText("Ready in Minutes: " + meal.getReadyInMinutes());
            servingsTV.setText("Servings: " + meal.getServings());
            caloriesTV.setText("Calories: " + meal.getCalories());

            // Load image using Picasso

            Transformation transformation = new RoundedCornersTransformation(30,0);
            Picasso.get().load(meal.getImage()).transform(transformation).into(mealImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(meal);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        return onItemLongClickListener.onItemLongClicked(meal);
                    }
                    return false;
                }
            });
        }
    }
}
