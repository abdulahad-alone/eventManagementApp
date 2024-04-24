package com.example.eventapp.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class ShowSomeThing extends RecyclerView.Adapter<ShowSomeThing.ViewHolder> {
    ArrayList<String> prices;
    ArrayList<String> ingredients;
    private ArrayList<Integer> quantities;
    TextView total_price_in_bottom_toolbar;
    TextView total_kgs;
    String totalPrice;
    private TotalPriceListener totalPriceListener;
    private UpdateQuanlity updateQuanlity;



    public interface TotalPriceListener {
        void onTotalPriceChanged(double totalPrice);
    }

    public interface UpdateQuanlity {
        void onQuantityChanged(String des);
    }

    public ShowSomeThing(ArrayList<String> prices, ArrayList<String> ingredients, TextView textView,
                         TotalPriceListener totalPriceListener, UpdateQuanlity updateQuanlity, TextView total_kgs) {
        this.ingredients=ingredients;
        this.prices=prices;
        this.total_price_in_bottom_toolbar = textView;
        this.quantities = new ArrayList<>(Collections.nCopies(prices.size(), 0));
        this.totalPriceListener = totalPriceListener;
        this.updateQuanlity = updateQuanlity;
        this.total_kgs = total_kgs;

    }

    @NonNull
    @Override
    public ShowSomeThing.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daig_booking, parent, false);
        return new ShowSomeThing.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowSomeThing.ViewHolder holder, int position) {

        String name = ingredients.get(position);
        String price= prices.get(position);
        String desp=name+" "+price+" per kg";
        holder.price_and_name.setText(desp);
        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updatedQuantity = incQuantity(position, false); // Increment quantity associated with the clicked item
                updateTotalPrice();
                holder.t2.setText(String.valueOf(updatedQuantity));
                String value=String.valueOf(updatedQuantity);// Update the quantity display

                if (updateQuanlity != null) {
                    updateQuanlity.onQuantityChanged(desp+" "+value+" kg");
                }
                Log.e(TAG, desp+" "+value+" kg" );
            }
        });

        holder.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int updatedQuantity = incQuantity(position, true); // Decrement quantity associated with the clicked item
                updateTotalPrice();
                String value=String.valueOf(updatedQuantity);
                holder.t2.setText(value); // Update the quantity display
                if (updateQuanlity != null) {
                    updateQuanlity.onQuantityChanged(desp+" "+value+" kg");
                }
                Log.e(TAG, "price: of "+name+" is "+totalPrice );
            }
        });


    }





    private int incQuantity(int position, boolean isIncrement) {
        int quantity = quantities.get(position);
        if (isIncrement) {
            quantity++;
        } else {
            quantity = Math.max(0, quantity - 1); // Ensure quantity does not go below 0
        }
        quantities.set(position, quantity); // Update the quantity associated with the item
        Log.e(TAG, "updateTotalPrice: "+quantity );
        return quantity;
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        double quanlityInKg=0.0;
        for (int i = 0; i < prices.size(); i++) {
            String price = prices.get(i);
            int quantity = quantities.get(i); // Retrieve the quantity associated with the item

            totalPrice += Double.parseDouble(price) * quantity;
            quanlityInKg +=  quantity;
        }
        total_kgs.setText(String.valueOf(quanlityInKg));
        total_price_in_bottom_toolbar.setText(String.valueOf(totalPrice));
        if (totalPriceListener != null) {
            totalPriceListener.onTotalPriceChanged(totalPrice);
        }
    }


    @Override
    public int getItemCount() {
        return prices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView price_and_name, t1, t2, t3;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price_and_name = itemView.findViewById(R.id.price_and_name);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
            t3 = itemView.findViewById(R.id.t3);


        }
        private void inc(Boolean x){
            int y = Integer.parseInt(t2.getText().toString());
            if(x){
                y++;
                t2.setText(String.valueOf(y));
            }else {
                y--;
                if(y == 0){

                }else {
                    t2.setText(String.valueOf(y));
                }
            }
        }

    }
}
