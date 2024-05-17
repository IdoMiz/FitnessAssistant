package IdoMizrahi.fitnessassistant.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Food;
import com.example.model.Foods;
import com.squareup.picasso.Picasso;


import IdoMizrahi.fitnessassistant.R;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder>
{
    public interface OnItemClickListener {
        public void onItemClicked(Food food);

    }
    public interface OnPlusButtonClickListener {
        void onPlusButtonClick(Food food);
    }

    public interface OnMinusButtonClickListener {
        void onMinusButtonClick(Food food);
    }

    public interface OnItemLongClickListener {

        public boolean onItemLongClicked(Food food);
    }
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;
    private OnPlusButtonClickListener plusButtonClickListener;
    private OnMinusButtonClickListener minusButtonClickListener;
    private Context context;
    private Foods foods;
    private int single_food_layout;


    public FoodAdapter(Context context, Foods foods, int card_view , OnItemClickListener listener, OnItemLongClickListener longListener, OnPlusButtonClickListener plusButtonClickListener, OnMinusButtonClickListener minusButtonClickListener) {
        this.context = context;
        this.foods =  foods;
        this.single_food_layout = card_view;
        this.listener = listener;
        this.longListener = longListener;
        this.plusButtonClickListener = plusButtonClickListener;
        this.minusButtonClickListener = minusButtonClickListener;

    }

    @NonNull
    @Override
    public FoodAdapter.FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodHolder(LayoutInflater.from(context).inflate(single_food_layout, parent, false));
    }

    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Food food = foods.get(position);

        if (food != null) {
            holder.bind(food, listener, longListener, plusButtonClickListener, minusButtonClickListener);

        }
    }


    @Override
    public int getItemCount() {
        return (foods != null) ? foods.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFoods(Foods newFoods) {
        if (newFoods != null) {
            foods.clear();
            foods.addAll(newFoods); // Add all elements from the newFoods list
            notifyDataSetChanged();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addFoods(Foods newFoods) {
        if (newFoods != null) {
            foods.addAll(newFoods); // Add all elements from the newFoods list
            notifyDataSetChanged();
        }
    }
    public void removeFood(Food food){
        foods.remove(food);
        notifyDataSetChanged();
    }
    public void clearFood(){
        foods.clear();
        notifyDataSetChanged();
    }

    public Foods getFoods() {
        return foods;
    }

    public static class FoodHolder extends RecyclerView.ViewHolder{

        public TextView foodName;
        public TextView  calories;
        public TextView fats;
        public TextView carbs;
        public TextView protein;
        public Button plusBtn;
        public Button minusBtn;
        public TextView servingTv;
        public ImageView foodImage;
        public FoodHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.textViewName);
            calories  = itemView.findViewById(R.id.calories);
            fats = itemView.findViewById(R.id.fats);
            carbs = itemView.findViewById(R.id.carbs);
            protein = itemView.findViewById(R.id.protein);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            servingTv = itemView.findViewById(R.id.servingAmount);
            foodImage = itemView.findViewById(R.id.foodImage);
        }

        public void setData(Food food){

            String name = food.getLabel();
            String cal = String.valueOf((int)(food.getNutrients().getCalories()*food.getServing()));
            String fat = String.valueOf((int)(food.getNutrients().getFat()*food.getServing()));
            String carb = String.valueOf((int)(food.getNutrients().getCarbs()*food.getServing()));
            String protein1 = String.valueOf((int)(food.getNutrients().getProtein()*food.getServing()));

            foodName.setText(name);
            calories.setText(cal);
            fats.setText(fat);
            carbs.setText(carb);
            protein.setText(protein1);
            servingTv.setText(String.valueOf(food.getServing()));
        }
        public void bind(Food food, OnItemClickListener listener, OnItemLongClickListener longListener, OnPlusButtonClickListener plusButtonClickListener, OnMinusButtonClickListener minusButtonClickListener) {
            if(food != null){
                setData(food);
                Picasso.get().load(food.getImage()).into(foodImage);

                if (plusButtonClickListener != null) {
                    plusBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double serving = food.getServing()+1;
                            food.setServing(serving);
                            setData(food);
                            plusButtonClickListener.onPlusButtonClick(food);
                        }
                    });
                }

                // Minus button click listener
                if (minusButtonClickListener != null) {
                    minusBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double serving = (food.getServing() - 1 > 0) ? food.getServing() - 1 : 1;
                            food.setServing(serving);
                            setData(food);
                            minusButtonClickListener.onMinusButtonClick(food);
                        }
                    });
                }


                itemView.setOnClickListener(v -> listener.onItemClicked(food));

                itemView.setOnLongClickListener(v -> {
                    longListener.onItemLongClicked(food);
                    return true;
                });
            }
        }
    }
}