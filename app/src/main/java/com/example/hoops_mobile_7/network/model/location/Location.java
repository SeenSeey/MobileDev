package com.example.hoops_mobile_7.network.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("dimension")
    private String dimension;
    @SerializedName("residents")
    private List<String> residents;
    @SerializedName("url")
    private String url;
    @SerializedName("created")
    private String created;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getDimension() { return dimension; }
    public List<String> getResidents() { return residents; }
    public String getUrl() { return url; }
    public String getCreated() { return created; }
}
