package com.example.eventapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.eventapp.Models.Meals;
import com.example.eventapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class ShowMealAdapter extends RecyclerView.Adapter<ShowMealAdapter.ViewHolder>{
    private int selectedItemPosition = RecyclerView.NO_POSITION;
    private List<Meals> meals;
    private Context context;
    private OnItemClickListener listener;




    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public ShowMealAdapter(List<Meals> meals) {
        this.meals = meals;
        this.context = context;
    }
    @NonNull
    @Override
    public ShowMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowMealAdapter.ViewHolder holder, int position) {
        Meals meal = meals.get(position);

        // Bind meal data to ViewHolder
        holder.bind(meal);
        if (position == selectedItemPosition) {

        } else {
            holder.itemView.setBackgroundResource(R.drawable.item_selector_tranparent);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemPosition == position) {
                    // If the clicked item is already selected, deselect it
                    selectedItemPosition = RecyclerView.NO_POSITION;
                } else {
                    // Otherwise, select the clicked item
                    selectedItemPosition = position;
                }
                if (listener != null) {
                    listener.onItemClick(position);
                }
                // Notify the adapter that the data set has changed to apply the highlighting effect
                //notifyDataSetChanged();
            }
        });
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return meals.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mealNameTextView;
        private TextView mealPriceTextView;
        private TextView descriptionTextView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealNameTextView = itemView.findViewById(R.id.meal_name);
            mealPriceTextView = itemView.findViewById(R.id.meal_price);
            descriptionTextView = itemView.findViewById(R.id.meal_detail);
            imageView = itemView.findViewById(R.id.home_image);
        }

        public void bind(Meals meal) {
            // Bind meal data to UI elements
            mealNameTextView.setText(meal.getMealName());
            mealPriceTextView.setText("Rs "+meal.getMealPrice()+" per head");
            descriptionTextView.setText(meal.getDescription());
            Picasso.get().load(meal.getImageUrl()).into(imageView);
        }
    }
}
