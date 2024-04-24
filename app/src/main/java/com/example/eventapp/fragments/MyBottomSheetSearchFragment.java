package com.example.eventapp.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventapp.Models.keys;
import com.example.eventapp.R;
import com.example.eventapp.showSearchData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Nullable;

public class MyBottomSheetSearchFragment extends BottomSheetDialogFragment implements MyBottomSheetCitiesFragmentSearch.CitySelectionListener {
    String[] item={"Banquet Hall","Marrquee","Catering","Photographer","Makeup Artist","DJ","Transportation Service","Service Only"};
    ArrayAdapter<String> adapteritem ;
    AutoCompleteTextView autoCompleteTextView;
    TextView city,done,parsedPriceStart;
    EditText price_start,price_end;
    String priceStart,priceEnd,City;
    String Type,City_name;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.my_bottom_sheet_search_fragment, container, false);
        autoCompleteTextView=view.findViewById(R.id.auto_complete_Text);
        city=view.findViewById(R.id.city);
        price_start=view.findViewById(R.id.priceStart);
        price_end=view.findViewById(R.id.priceEnd);
        done=view.findViewById(R.id.textView6);
        parsedPriceStart=view.findViewById(R.id.parsedPriceStart);
        adapteritem=new ArrayAdapter<String>(getContext(),R.layout.list_item,item );
        autoCompleteTextView.setAdapter(adapteritem);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=adapterView.getItemAtPosition(i).toString();
                Type=item;
                if(Type.equals("Marrquee")||Type.equals("Banquet Hall"))
                {
                    parsedPriceStart.setText("Per head");
                }else if(Type.equals("Photographer")||Type.equals("DJ")||Type.equals("Transportation Service"))
                {
                    parsedPriceStart.setText("Per day");
                }else
                {
                    parsedPriceStart.setText("Starting from");
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceStart=price_start.getText().toString();
                priceEnd=price_end.getText().toString();

                Intent intent=new Intent(getContext(), showSearchData.class);
                intent.putExtra("city",City);
                intent.putExtra("type",Type);
                intent.putExtra("priceRangeStart",priceStart);
                intent.putExtra("priceRangeEnd",priceEnd);

                startActivity(intent);
                dismiss();
            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*MyBottomSheetCitiesFragment bottomSheetFragment = new MyBottomSheetCitiesFragment();
                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());*/
                openCitiesFragment();
                City_name= keys.getInstance().getName();
                Log.i(ContentValues.TAG, "onCreateView key: "+City_name);
            }
        });



        return view;
    }


    @Override
    public void onCitySelected(String cityName) {
        if (city != null) {
            city.setText(cityName);
            City=cityName;
        } else {
            Log.e(ContentValues.TAG, "City TextView is null!");
        }
    }
    private void openCitiesFragment() {
        MyBottomSheetCitiesFragmentSearch bottomSheetFragment = new MyBottomSheetCitiesFragmentSearch();
        bottomSheetFragment.setCitySelectionListener(this); // Set the listener
        bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
    }
}