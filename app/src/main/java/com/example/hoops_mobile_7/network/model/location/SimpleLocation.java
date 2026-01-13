package com.example.hoops_mobile_7.network.model.location;

import com.google.gson.annotations.SerializedName;

public class SimpleLocation {
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    public String getName() { return name; }
    public String getUrl() { return url; }
}
