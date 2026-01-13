package com.example.hoops_mobile_7.network.model.character;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.hoops_mobile_7.network.model.location.SimpleLocation;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "characters")
public class Character {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("status")
    private String status;
    @SerializedName("species")
    private String species;
    @SerializedName("type")
    private String type;
    @SerializedName("gender")
    private String gender;
    @SerializedName("origin")
    private SimpleLocation origin;
    @SerializedName("location")
    private SimpleLocation location;
    @SerializedName("image")
    private String image;
    @SerializedName("episode")
    private List<String> episode;
    @SerializedName("url")
    private String url;
    @SerializedName("created")
    private String created;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public String getSpecies() { return species; }
    public String getType() { return type; }
    public String getGender() { return gender; }
    public SimpleLocation getOrigin() { return origin; }
    public SimpleLocation getLocation() { return location; }
    public String getImage() { return image; }
    public List<String> getEpisode() { return episode; }
    public String getUrl() { return url; }
    public String getCreated() { return created; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setOrigin(SimpleLocation origin) {
        this.origin = origin;
    }

    public void setLocation(SimpleLocation location) {
        this.location = location;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEpisode(List<String> episode) {
        this.episode = episode;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
