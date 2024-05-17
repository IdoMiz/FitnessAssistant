package IdoMizrahi.fitnessassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Food;
import com.example.model.Foods;

import java.util.List;

import IdoMizrahi.fitnessassistant.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{



    // create the item adapter and its card

    public interface OnItemLongClickListener {

        public boolean onItemLongClicked(String string);
    }
    private ItemAdapter.OnItemLongClickListener longListener;

    private Context context;
    private List<String> items;
    private int single_item_layout;

    public ItemAdapter(OnItemLongClickListener longListener, Context context, List<String> items, int single_item_layout) {
        this.longListener = longListener;
        this.context = context;
        this.items = items;
        this.single_item_layout = single_item_layout;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemAdapter.ItemHolder(LayoutInflater.from(context).inflate(single_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        String item = items.get(position);

        if (items != null) {
            holder.bind(item,longListener);
        }
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    public void addItem(String item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(String item){
        items.remove(item);
        notifyDataSetChanged();
    }

    public OnItemLongClickListener getLongListener() {
        return longListener;
    }

    public void setLongListener(OnItemLongClickListener longListener) {
        this.longListener = longListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public int getSingle_item_layout() {
        return single_item_layout;
    }

    public void setSingle_item_layout(int single_item_layout) {
        this.single_item_layout = single_item_layout;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{

        public TextView foodName;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.itemName);
        }

        public void setData(String item){
            foodName.setText(item);
        }

        public void bind(String item, ItemAdapter.OnItemLongClickListener longListener) {
            if (item != null) {
                setData(item);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return longListener.onItemLongClicked(item);
                    }
                });
            }
        }
    }

}
