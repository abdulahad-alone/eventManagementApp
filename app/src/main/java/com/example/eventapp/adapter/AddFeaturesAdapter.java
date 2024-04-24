package com.example.eventapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;

import java.util.ArrayList;

public class AddFeaturesAdapter extends RecyclerView.Adapter<AddFeaturesAdapter.ViewHolder> {

    private ArrayList<String> newDataList;

    public AddFeaturesAdapter(ArrayList<String> newDataList) {
        this.newDataList = newDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dataItem = newDataList.get(position);
        holder.textView.setText(dataItem);

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove item from the list
                newDataList.remove(position);
                // Notify adapter about item removal
                notifyItemRemoved(position);
                // Notify any item change after the removed position
                notifyItemRangeChanged(position, newDataList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return newDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textNewData);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}