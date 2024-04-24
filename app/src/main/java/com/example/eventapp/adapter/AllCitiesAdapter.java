package com.example.eventapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;
import com.example.eventapp.listeners.ItemListener;

import java.util.ArrayList;
import java.util.List;

public class AllCitiesAdapter extends RecyclerView.Adapter<AllCitiesAdapter.CityViewHolder> {
    private List<String> cityList;
    private Context context;
    private ItemListener itemListener;
    private List<String> filteredCitiesList;
    public AllCitiesAdapter(Context context, List<String> cityList, ItemListener itemListener) {

        this.cityList = cityList;
        this.context = context;
        this.itemListener=itemListener;
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.lots_of_cities,parent,false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        if (filteredCitiesList != null) {
            holder.textViewCityName.setText(filteredCitiesList.get(position));
            // Bind your data to the views in your ViewHolder here
        }else {
            holder.textViewCityName.setText(cityList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (filteredCitiesList != null) {
            return filteredCitiesList.size();
        }
        return cityList.size();
    }

    public void filterList(List<String> filteredCities) {
        filteredCitiesList = new ArrayList<>(filteredCities);
        notifyDataSetChanged();
    }

    public  class CityViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCityName;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCityName = itemView.findViewById(R.id.textViewCityName);

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.OnItemPosition(getAdapterPosition());
                }
            });
        }
    }
}
