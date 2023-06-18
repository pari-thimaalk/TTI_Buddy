package com.example.inventory3.loanledger.mvvm;

import androidx.room.TypeConverter;

import com.example.inventory3.loanledger.mvvm.LoanListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<LoanListItem> from_String(String value) {
        Type listType = new TypeToken<List<LoanListItem>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String from_List(List<LoanListItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
