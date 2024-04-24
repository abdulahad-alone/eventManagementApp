package com.example.eventapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class DaigModel extends HashMap<String, Object> implements Parcelable {

    private  String Ingredient;
    private  String IngredientPrice;

    public DaigModel() {
    }

    public DaigModel(String ingredient, String ingredientPrice) {
        this.Ingredient = ingredient;
        this.IngredientPrice = ingredientPrice;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }

    public String getIngredientPrice() {
        return IngredientPrice;
    }

    public void setIngredientPrice(String ingredientPrice) {
        IngredientPrice = ingredientPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(Ingredient);
        parcel.writeString(IngredientPrice);

    }
    protected DaigModel(Parcel in) {
        Ingredient = in.readString();
        IngredientPrice = in.readString();
    }

    public static final Creator<DaigModel> CREATOR = new Creator<DaigModel>() {
        @Override
        public DaigModel createFromParcel(Parcel in) {
            return new DaigModel(in);
        }

        @Override
        public DaigModel[] newArray(int size) {
            return new DaigModel[size];
        }
    };
}
