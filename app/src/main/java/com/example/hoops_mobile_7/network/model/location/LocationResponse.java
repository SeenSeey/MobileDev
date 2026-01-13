package com.example.hoops_mobile_7.network.model.location;

import com.example.hoops_mobile_7.network.model.Info;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationResponse {
    @SerializedName("info")
    private Info info;
    @SerializedName("results")
    private List<Location> results;

    public Info getInfo() { return info; }
    public List<Location> getResults() { return results; }
}
