package com.example.eventapp.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyBottomSheetCitiesFragment extends BottomSheetDialogFragment implements ItemListener {


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

   // MyBottomSheetSearchFragment myBottomSheetSearchFragment;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.activity_my_bottom_sheet_cities_fragment, container, false);
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: Context = " + context.getClass().getSimpleName());
        if (context instanceof CitySelectionListener){
            citySelectionListener = (CitySelectionListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement CitySelectionListener");
        }
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

       // myBottomSheetSearchFragment=new MyBottomSheetSearchFragment();
        bundle = new Bundle();
        List<String> citiesOfPakistan = Arrays.asList(
                "Karachi", "Lahore", "Faisalabad", "Rawalpindi", "Multan", "Gujranwala", "Hyderabad", "Peshawar",
                "Quetta", "Islamabad", "Sargodha", "Sialkot", "Bahawalpur", "Sukkur", "Larkana", "Sheikhupura",
                "Rahim Yar Khan", "Jhang", "Dera Ghazi Khan", "Gujrat", "Sahiwal", "Wah Cantonment", "Mardan",
                "Kasur", "Okara", "Mingora", "Nawabshah", "Chiniot", "Kotri", "Kāmoke", "Hafizabad", "Sadiqabad",
                "Mirpur Khas", "Burewala", "Kohat", "Khanewal", "Dera Ismail Khan", "Turbat", "Muzaffargarh",
                "Abbotabad", "Mandi Bahauddin", "Shikarpur", "Jacobabad", "Jhelum", "Khanpur", "Khairpur",
                "Khuzdar", "Pakpattan", "Hub", "Daska", "Gojra", "Dadu", "Muridke", "Bahawalnagar", "Samundri",
                "Tando Allahyar", "Tando Adam", "Jaranwala", "Chishtian", "Attock", "Vehari", "Kot Abdul Malik",
                "Ferozwala", "Chakwal", "Gujranwala Cantonment", "Kamalia", "Umerkot", "Ahmedpur East", "Kot Addu",
                "Wazirabad", "Mansehra", "Layyah", "Swabi", "Chaman", "Taxila", "Nowshera", "Khushab", "Shahdadkot",
                "Mianwali", "Kabal", "Lodhran", "Hasilpur", "Charsadda", "Bhakkar", "Badin", "Arifwala", "Ghotki",
                "Sambrial", "Jatoi", "Haroonabad", "Daharki", "Narowal", "Tando Muhammad Khan", "Kamber Ali Khan",
                "Mirpur Mathelo", "Kandhkot", "Bhalwal", "Zahir Pir", "Kunjah", "Shahdadpur", "Pano Aqil",
                "Mehrabpur", "Tando Jam", "Tando Bagho", "Jalalpur Jattan", "Nankana Sahib", "Qila Didar Singh",
                "Loralai", "Kahror Pakka", "Mitha Tiwana", "Jahania", "Tando Ghulam Ali", "Usta Muhammad",
                "Loralai", "Dunga Bunga", "Rajanpur", "Ladhewala Waraich", "Chak Azam Saffo", "Dunyapur",
                "Talagang", "Gambat", "Mian Channun", "Daud Khel", "Ratodero", "Jhol", "Pind Dadan Khan",
                "Paharpur", "Sodhra", "Beloha", "Kamar Mushani", "Gadani", "Kandiaro", "Digri", "Pasni", "Kot Samaba",
                "Warburton", "Kulachi", "Chor", "Pishin", "Pindi Gheb", "Qadirpur Ran", "Kahna Nau", "Kamra", "Kalur Kot",
                "Kundian", "Mustafabad", "Jalalpur Pirwala", "Chak Two Hundred Forty-Nine TDA", "Pir Mahal",
                "Choa Saidan Shah", "Jiwani", "Shahr Sultan", "Chawinda", "Hingorja", "Mormugao", "Malakwal City",
                "Faruka", "Pad Idan", "Nasirabad", "Kotli Loharan", "New Badah", "Bannu", "Bagh", "Kalabagh",
                "Daud Khel", "Alipur Chatha", "Bat Khela", "Khadro", "Kaur", "Lehri", "Mithi", "Mithani", "Kot Diji",
                "Sehwan", "Chaman", "Thul", "Uthal", "Kot Malik Barkhurdar", "Musa Khel Bazar", "Parachinar", "Hala",
                "Ranipur", "Mirpur Sakro", "Ubauro", "Kundla", "Ghotki", "Gharo", "Khairpur Nathan Shah", "Gandava",
                "Manora", "Warah", "Rohri", "Loralai", "New Mirpur", "Zafarwal", "Bhong", "Bhiria City", "Dadhar",
                "Kashmor", "Pabbi", "Samaro", "Shahpur Chakar", "Sukheke Mandi", "Sarai Naurang", "Chunian",
                "Tando Bago", "Khairpur Tamiwali", "Rojhan", "Jand", "Dunyapur", "Darya Khan", "Sodhri", "Daur",
                "Talhar", "Lakki Marwat", "Kangi", "Qadirpur Ran", "Rajana", "Kot Radha Kishan", "Kot Ghulam Muhammad",
                "Haveli Lakha", "Faqirwali", "Bhopalwala", "Dijkot", "Dera Bugti", "Shahr Sultan", "Bhit Shah",
                "Bakrani", "Shahpur Saddar", "Khangah Dogran", "Garhi Khairo", "Gambat", "Ghotki", "Gharo", "Ghauspur",
                "Gujar Khan", "Hala", "Kandiaro", "Karak", "Kashmor", "Keti Bandar", "Khadro", "Kotli",
                "Kotri", "Lakki Marwat", "Matiari", "Mirpur Bhtoro", "Miro Khan", "Morro", "Mehar", "Mehrabpur",
                "Mian Channun", "Miro Khan", "More Khunda", "Murree", "Narang", "Naushahro Firoz", "Nawabshah",
                "Nazir Town", "New Badah", "Noorpur Thal", "Nowshera", "Okara", "Pir Mahal", "Pind Dadan Khan",
                "Pir Jo Goth", "Qadirpur Ran", "Rajanpur", "Rohri", "Saddar Gogera", "Saeedabad", "Sakrand",
                "Samundri", "Sanghar", "Sangla Hill", "Sarai Alamgir", "Sarai Naurang", "Sargodha", "Shahdadpur",
                "Shahdadkot", "Shahpur Chakar", "Shahpur Saddar", "Shikarpur", "Shujabad", "Sialkot", "Sibi",
                "Sita Road", "Sobhodero", "Sodhra", "Sohbatpur", "Sujawal", "Sukkur", "Surab", "Talhar", "Tando Adam",
                "Tando Allahyar", "Tando Bago", "Tando Muhammad Khan", "Tangi", "Tank", "Tarbela", "Thall", "Tharu Shah",
                "Thatta", "Thul", "Topi", "Turbat", "Ubauro", "Uch", "Umarkot", "Upper Dir", "Usta Muhammad",
                "Vihari", "Wah Cantonment", "Warah", "Wazirabad", "Yazman", "Zafarwal", "Zahir Pir", "Zaida",
                "Zhob", "Khewra", "Chakwal", "Mardan", "Swat", "Kharan", "Hingorja", "Haripur", "Chaman",
                "Kambar", "Umerkot", "Jhal Magsi", "Naudero", "Pasrur", "Bela", "Jiwani", "Qambar", "Qila Abdullah",
                "Shahdadpur", "Shahdadkot", "Shikarpur", "Sibi", "Lakki Marwat", "Moro", "Malakand", "Rajanpur",
                "Tando Adam", "Tando Bago", "Tando Jam", "Tando Muhammad Khan", "Turbat", "Sarai Naurang", "Zhob","Pind Muneem"
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

       /* bundle.putString("key", City);
        myBottomSheetSearchFragment.setArguments(bundle);
        myBottomSheetSearchFragment.show(getParentFragmentManager(), myBottomSheetSearchFragment.getTag());*/
        sendSelectedCityToParent(City);
    }



}
