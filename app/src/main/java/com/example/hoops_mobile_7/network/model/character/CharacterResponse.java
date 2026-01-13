package com.example.hoops_mobile_7.network.model.character;

import com.example.hoops_mobile_7.network.model.Info;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CharacterResponse {
    @SerializedName("info")
    private Info info;
    @SerializedName("results")
    private List<com.example.hoops_mobile_7.network.model.character.Character> results;

    public Info getInfo() { return info; }
    public List<com.example.hoops_mobile_7.network.model.character.Character> getResults() { return results; }
}
