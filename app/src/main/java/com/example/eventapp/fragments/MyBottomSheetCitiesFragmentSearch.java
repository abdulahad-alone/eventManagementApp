package com.example.eventapp.fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eventapp.Models.keys;
import com.example.eventapp.R;
import com.example.eventapp.adapter.AllCitiesAdapter;
import com.example.eventapp.listeners.ItemListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MyBottomSheetCitiesFragmentSearch extends BottomSheetDialogFragment implements ItemListener {

    public interface CitySelectionListener {
        void onCitySelected(String cityName);
    }
    private RecyclerView recyclerView;
    private AllCitiesAdapter adapter;
    String City;
    List<String> citiesOfPakistansort;
    List<String> filteredCities=null;
    EditText searchs;
    Bundle bundle;


    private CitySelectionListener citySelectionListener;
    MyBottomSheetSearchFragment myBottomSheetSearchFragment;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.my_bottom_sheet_cities_fragment_search, container, false);
        return view;
    }

    public void setCitySelectionListener(CitySelectionListener listener) {
        this.citySelectionListener = listener;
        Log.i(TAG, "setCitySelectionListener: "+listener);
    }
    private void sendSelectedCityToParent(String cityName) {
        if (citySelectionListener != null) {
            Log.i(TAG, "sendSelectedCityToParent: ");
            citySelectionListener.onCitySelected(cityName);
            dismiss(); // Dismiss the bottom sheet after selecting a city
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myBottomSheetSearchFragment=new MyBottomSheetSearchFragment();
        bundle = new Bundle();
        List<String> citiesOfPakistan = Arrays.asList(
                "Karachi", "Lahore", "Faisalabad", "Rawalpindi", "Multan", "Gujranwala", "Hyderabad", "Peshawar",
                "Quetta", "Islamabad", "Sargodha", "Bahawalpur", "Sukkur", "Larkana", "Sheikhupura",
                "Rahim Yar Khan", "Jhang", "Dera Ghazi Khan", "Gujrat", "Sahiwal", "Wah Cantonment",
                "Kasur", "Okara", "Mingora", "Nawabshah", "Chiniot", "Kotri", "Kāmoke", "Hafizabad", "Sadiqabad",
                "Mirpur Khas", "Burewala", "Kohat", "Khanewal", "Dera Ismail Khan", "Turbat", "Muzaffargarh",
                "Abbotabad", "Mandi Bahauddin", "Shikarpur", "Jacobabad", "Jhelum", "Khanpur", "Khairpur",
                "Khuzdar", "Pakpattan", "Hub", "Daska", "Gojra", "Dadu", "Muridke", "Bahawalnagar", "Samundri",
                "Tando Allahyar", "Tando Adam", "Jaranwala", "Chishtian", "Attock", "Vehari", "Kot Abdul Malik",
                "Ferozwala", "Chakwal", "Gujranwala Cantonment", "Kamalia", "Umerkot", "Ahmedpur East", "Kot Addu",
                "Wazirabad", "Mansehra", "Layyah", "Swabi", "Chaman", "Taxila", "Nowshera", "Khushab", "Shahdadkot",
                "Mianwali", "Kabal", "Lodhran", "Hasilpur", "Charsadda", "Bhakkar", "Badin", "Arifwala",
                "Sambrial", "Jatoi", "Haroonabad", "Daharki", "Narowal", "Tando Muhammad Khan", "Kamber Ali Khan",
                "Mirpur Mathelo", "Kandhkot", "Bhalwal", "Zahir Pir", "Kunjah", "Pano Aqil",
                "Tando Jam", "Tando Bagho", "Jalalpur Jattan", "Nankana Sahib", "Qila Didar Singh",
                "Kahror Pakka", "Mitha Tiwana", "Jahania", "Tando Ghulam Ali", "Usta Muhammad", "Dunga Bunga", "Rajanpur", "Ladhewala Waraich", "Chak Azam Saffo", "Dunyapur",
                "Talagang", "Gambat", "Daud Khel", "Ratodero", "Jhol", "Pind Dadan Khan",
                "Paharpur", "Beloha", "Kamar Mushani", "Gadani", "Digri", "Pasni", "Kot Samaba",
                "Warburton", "Kulachi", "Chor", "Pishin", "Pindi Gheb", "Qadirpur Ran", "Kahna Nau", "Kamra", "Kalur Kot",
                "Kundian", "Mustafabad", "Jalalpur Pirwala", "Chak Two Hundred Forty-Nine TDA",
                "Choa Saidan Shah", "Jiwani", "Chawinda", "Hingorja", "Mormugao", "Malakwal City",
                "Faruka", "Pad Idan", "Nasirabad", "Kotli Loharan", "New Badah", "Bannu", "Bagh", "Kalabagh",
                "Alipur Chatha", "Bat Khela", "Khadro", "Kaur", "Lehri", "Mithi", "Mithani", "Kot Diji",
                "Sehwan", "Thul", "Uthal", "Kot Malik Barkhurdar", "Musa Khel Bazar", "Parachinar", "Hala",
                "Ranipur", "Mirpur Sakro", "Ubauro", "Kundla", "Gharo", "Khairpur Nathan Shah", "Gandava",
                "Manora", "Warah", "New Mirpur", "Zafarwal", "Bhong", "Bhiria City", "Dadhar",
                "Kashmor", "Pabbi", "Samaro", "Sukheke Mandi", "Chunian",
                "Khairpur Tamiwali", "Rojhan", "Jand", "Darya Khan", "Sodhri", "Daur",
                "Talhar", "Kangi", "Rajana", "Kot Radha Kishan", "Kot Ghulam Muhammad",
                "Haveli Lakha", "Faqirwali", "Bhopalwala", "Dijkot", "Dera Bugti", "Shahr Sultan", "Bhit Shah",
                "Bakrani", "Khangah Dogran", "Garhi Khairo", "Ghotki", "Ghauspur",
                "Gujar Khan", "Kandiaro", "Karak", "Kashmor", "Keti Bandar", "Khadro", "Kotli", "Lakki Marwat", "Matiari", "Mirpur Bhtoro", "Mehar", "Mehrabpur",
                "Mian Channun", "Miro Khan", "More Khunda", "Murree", "Narang", "Naushahro Firoz",
                "Nazir Town", "Noorpur Thal", "Pir Mahal",
                "Pir Jo Goth", "Rohri", "Saddar Gogera", "Saeedabad", "Sakrand"
                , "Sanghar", "Sangla Hill", "Sarai Alamgir", "Sarai Naurang",
                "Shahpur Chakar", "Shahpur Saddar", "Shujabad", "Sialkot",
                "Sita Road", "Sobhodero", "Sodhra", "Sohbatpur", "Sujawal", "Sukkur", "Surab", "Talhar", "Tando Adam",
                "Tando Allahyar", "Tando Muhammad Khan", "Tangi", "Tank", "Tarbela", "Thall", "Tharu Shah",
                "Thatta", "Thul", "Topi", "Turbat", "Ubauro", "Uch", "Umarkot", "Upper Dir", "Usta Muhammad",
                "Vihari", "Wah Cantonment", "Warah", "Wazirabad", "Yazman", "Zafarwal", "Zahir Pir", "Zaida",
                "Zhob", "Khewra", "Mardan", "Swat", "Kharan", "Hingorja", "Haripur",
                "Kambar", "Umerkot", "Jhal Magsi", "Naudero", "Pasrur", "Bela", "Qambar", "Qila Abdullah",
                "Shahdadpur", "Sibi", "Moro", "Malakand",
                "Tando Adam", "Tando Jam", "Tando Muhammad Khan", "Turbat", "Zhob","Pind Muneem"
        );
        Set<String> uniqueCitiesSet = new LinkedHashSet<>(citiesOfPakistan);
        List<String> uniqueCitiesList = new ArrayList<>(uniqueCitiesSet);
        Collections.sort(uniqueCitiesList);
        citiesOfPakistansort=uniqueCitiesList;

        EditText searchEditText = view.findViewById(R.id.search_edit);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString().toLowerCase().trim();
                Log.i(TAG, "cityname by user: "+userInput);
                filteredCities = new ArrayList<>();
                // Filter the cities based on user input
                for (String city : citiesOfPakistansort) {
                    if (city.toLowerCase().contains(userInput)) {
                        filteredCities.add(city);
                    }
                }
                adapter.filterList(filteredCities);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        recyclerView = view.findViewById(R.id.citiesRV);
        adapter=new AllCitiesAdapter(getContext(),citiesOfPakistansort,this);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }


    @Override
    public void OnItemPosition(int position) {
        if(filteredCities!=null)
        {
            City=filteredCities.get(position);
            Log.i(TAG, "OnItemPosition: "+filteredCities.get(position));
        }else{
            City=citiesOfPakistansort.get(position);
            Log.i(TAG, "OnItemPosition: "+citiesOfPakistansort.get(position));
        }
        //MyBottomSheetSearchFragment myBottomSheetSearchFragment=new MyBottomSheetSearchFragment();
        //Bundle bundle = new Bundle();
        keys.getInstance().setName(City);
       /* bundle.putString("key", City);
        myBottomSheetSearchFragment.setArguments(bundle);
        myBottomSheetSearchFragment.show(getParentFragmentManager(), myBottomSheetSearchFragment.getTag());*/
        sendSelectedCityToParent(City);
    }


}