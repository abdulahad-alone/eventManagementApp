package com.example.eventapp.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.Models.DaigModel;
import com.example.eventapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddMealAdapter extends RecyclerView.Adapter<AddMealAdapter.ImageViewHolder> {
    private List<Uri> selectedImages;
    private List<String> mealNames;
    private List<String> mealPrices;
    private List<String> descriptions;
    private List<String> minimum_orderList;
    private List<DaigModel> daigList;

    public AddMealAdapter(List<Uri> selectedImages, List<String> mealNames, List<String> mealPrices, List<String> descriptions) {
        this.selectedImages = selectedImages;
        this.mealNames = mealNames;
        this.mealPrices = mealPrices;
        this.descriptions = descriptions;
    }

    public AddMealAdapter(List<Uri> selectedImages, List<String> mealNames, List<String> mealPrices, List<String> descriptions, List<String> minimum_orderList, List<DaigModel> daigList) {
        this.selectedImages = selectedImages;
        this.mealNames = mealNames;
        this.mealPrices = mealPrices;
        this.descriptions = descriptions;
        this.minimum_orderList = minimum_orderList;
        this.daigList = daigList;

    }

    @NonNull
    @Override
    public AddMealAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_of_meals, parent, false);
        return new AddMealAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMealAdapter.ImageViewHolder holder, int position) {
        Uri imageUri = selectedImages.get(position);
        Picasso.get().load(imageUri).into(holder.imageView);

        holder.mealName.setText(mealNames.get(position));
        holder.mealPrice.setText(mealPrices.get(position));
        holder.description.setText(descriptions.get(position));
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImages.remove(position);
                mealNames.remove(position);
                mealPrices.remove(position);
                descriptions.remove(position);
                minimum_orderList.remove(position);

                if (!daigList.isEmpty()) {
                    daigList.remove(position);
                }
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, selectedImages.size());
                notifyItemRangeChanged(position, mealNames.size());
                notifyItemRangeChanged(position, mealPrices.size());
                notifyItemRangeChanged(position, descriptions.size());
                notifyItemRangeChanged(position, minimum_orderList.size());
                notifyItemRangeChanged(position, daigList.size());


            }
        });
    }
    public String getMealName(int position) {
        return mealNames.get(position);
    }
    public String getMealPrice(int position) {
        return mealPrices.get(position);
    }



    // Getter method to retrieve description for a specific position
    public String getDescription(int position) {
        return descriptions.get(position);
    }

    // Getter method to retrieve selected image URI for a specific position
    public Uri getSelectedImageUri(int position) {
        return selectedImages.get(position);
    }

    public String getMinimumorderList(int position) {
        return minimum_orderList.get(position);
    }

    public DaigModel getdaigList(int position) {
        return daigList.get(position);
    }


    @Override
    public int getItemCount() {
        return selectedImages.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView mealName;
        TextView mealPrice;
        TextView description;
        ImageView deleteImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_image);
            deleteImage = itemView.findViewById(R.id.deleteMeal);
            mealName = itemView.findViewById(R.id.meal_name);
            mealPrice = itemView.findViewById(R.id.meal_price);
            description = itemView.findViewById(R.id.meal_detail);
        }
    }
}
