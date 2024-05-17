package IdoMizrahi.fitnessassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Ingredient;
import com.example.model.Ingredients;

import java.util.List;

import IdoMizrahi.fitnessassistant.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {
    private Context context;
    private Ingredients ingredients;
    private int single_ingredient_layout;

    public IngredientAdapter(Context context, Ingredients ingredients, int single_ingredient_layout) {
        this.context = context;
        this.ingredients = ingredients;
        this.single_ingredient_layout = single_ingredient_layout;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientHolder(LayoutInflater.from(context).inflate(single_ingredient_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        if (ingredient != null) {
            holder.bind(ingredient);
        }
    }

    @Override
    public int getItemCount() {
        return (ingredients != null) ? ingredients.size() : 0;
    }


    public void setIngredients(Ingredients ingredients){
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void setIngredients(List<Ingredient> ingredients){
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }
    public static class IngredientHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;
        public TextView ingredientAmount;
        public TextView ingredientUnit;

        public IngredientHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientAmount = itemView.findViewById(R.id.ingredientSize);
            ingredientUnit = itemView.findViewById(R.id.ingredientSizeTV);
        }

        public void bind(Ingredient ingredient) {
            String name = "Ingredient: " + ingredient.getName();
            String amount = "Amount: " + ingredient.getAmount();
            String unit = "Unit: " + ingredient.getUnit();
            ingredientName.setText(name);
            ingredientAmount.setText(amount);
            ingredientUnit.setText(unit);
        }
    }
}
