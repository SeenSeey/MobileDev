package com.example.hoops_mobile_7.network.model.episode;

import com.example.hoops_mobile_7.network.model.Info;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EpisodeResponse {
    @SerializedName("info")
    private Info info;
    @SerializedName("results")
    private List<Episode> results;

    public Info getInfo() { return info; }
    public List<Episode> getResults() { return results; }
}
