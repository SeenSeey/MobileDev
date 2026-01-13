package com.example.hoops_mobile_7.network.model.episode;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Episode {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("air_date")
    private String airDate;
    @SerializedName("episode")
    private String episodeCode;
    @SerializedName("characters")
    private List<String> characters;
    @SerializedName("url")
    private String url;
    @SerializedName("created")
    private String created;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAirDate() { return airDate; }
    public String getEpisodeCode() { return episodeCode; }
    public List<String> getCharacters() { return characters; }
    public String getUrl() { return url; }
    public String getCreated() { return created; }
}
