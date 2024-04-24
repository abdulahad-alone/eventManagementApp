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
import java.util.List;

public class BookProductAdapter  extends RecyclerView.Adapter<BookProductAdapter.ViewHolder> {

    private ArrayList<String> newDataList;
    private ArrayList<String> newDataPriceList;
    private TextView total_price_in_bottom_toolbar;

    public BookProductAdapter(ArrayList<String> newDataList,ArrayList<String> newDataPriceList,TextView textView) {
        this.newDataList = newDataList;
        this.newDataPriceList = newDataPriceList;
        this.total_price_in_bottom_toolbar = textView;

    }

    @NonNull
    @Override
    public BookProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_data, parent, false);
        return new BookProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookProductAdapter.ViewHolder holder, int position) {
        String dataItem = newDataList.get(position);
        holder.textView.setText(dataItem);

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove item from the list
                newDataList.remove(position);
                newDataPriceList.remove(position);
                // Notify adapter about item removal
                notifyItemRemoved(position);
                // Notify any item change after the removed position
                notifyItemRangeChanged(position, newDataList.size(),newDataPriceList.size());
                updateTotalPrice();
            }
        });
    }
    private void updateTotalPrice() {
        double total = 0.0;
        for (String price : newDataPriceList) {
            total += Double.parseDouble(price);
        }

        if (newDataPriceList.isEmpty()) {
            total_price_in_bottom_toolbar.setText("0.0");
        } else {
            total_price_in_bottom_toolbar.setText(String.valueOf(total));
        }
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

