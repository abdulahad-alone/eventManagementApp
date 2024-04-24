package com.example.eventapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Models.DaigModel;
import com.example.eventapp.R;
import com.example.eventapp.Sub_detailed_activity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowDaigAdapter extends RecyclerView.Adapter<ShowDaigAdapter.ViewHolder> {

private List<Map<String, Object>> dataList;
    private Context context;

public ShowDaigAdapter(Context context,List<Map<String, Object>> dataList) {
    this.context = context;
    this.dataList = dataList;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> item = dataList.get(position);

        // Populate views with data from the Map
        holder.mealNameTextView.setText(String.valueOf(item.get("DaigName")));
        holder.mealPriceTextView.setText("Starting from "+String.valueOf(item.get("DaigPrice")));
        holder.descriptionTextView.setText(String.valueOf(item.get("description")));
        // Add more views if needed for other data fields
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Sub_detailed_activity.class);
                intent.putExtra("DaigName", item.get("DaigName").toString());
                intent.putExtra("DaigPrice", item.get("DaigPrice").toString());
                intent.putExtra("imageUrl", item.get("imageUrl").toString());
                intent.putExtra("description", item.get("description").toString());
                intent.putExtra("id", item.get("id").toString());
                intent.putExtra("DaigRef", item.get("DaigRef").toString());
                intent.putExtra("minimumOrder", item.get("minimumOrder").toString());
                intent.putExtra("Type", "Daig");
                ArrayList<DaigModel> daigModelsList = (ArrayList<DaigModel>) item.get("daigModels");
                intent.putParcelableArrayListExtra("daigModels", daigModelsList);

                // Add other data you need to pass to the new activity

                context.startActivity(intent);
            }
        });
        // Load image using Picasso or Glide (assuming "imageUrl" is a URL string)
        String imageUrl = (String) item.get("imageUrl");
        if (imageUrl != null) {
        Picasso.get().load(imageUrl).into(holder.imageView);
        }
        }

@Override
public int getItemCount() {
        return dataList.size();
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
        // Initialize more TextViews if needed
    }
}
}
