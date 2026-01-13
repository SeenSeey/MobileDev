package com.example.hoops_mobile_7.database;

import androidx.room.TypeConverter;
import com.example.hoops_mobile_7.network.model.location.SimpleLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private final Gson gson = new Gson();

    @TypeConverter
    public String fromEpisodeList(List<String> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public List<String> toEpisodeList(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public String fromSimpleLocation(SimpleLocation location) {
        return gson.toJson(location);
    }

    @TypeConverter
    public SimpleLocation toSimpleLocation(String value) {
        return gson.fromJson(value, SimpleLocation.class);
    }
}
