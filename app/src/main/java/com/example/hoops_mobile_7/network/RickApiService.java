package com.example.hoops_mobile_7.network;

import com.example.hoops_mobile_7.network.model.character.CharacterResponse;
import com.example.hoops_mobile_7.network.model.episode.EpisodeResponse;
import com.example.hoops_mobile_7.network.model.location.LocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RickApiService {
    @GET("character")
    Call<CharacterResponse> getCharacters(@Query("page") int page);

    @GET("character/{id}")
    Call<Character> getCharacterById(@Path("id") int id);

    @GET("character")
    Call<CharacterResponse> getCharactersFiltered(
            @Query("page") Integer page,
            @Query("name") String name,
            @Query("status") String status,
            @Query("species") String species,
            @Query("type") String type
    );

    @GET("location")
    Call<LocationResponse> getLocations(@Query("page") int page);

    @GET("episode")
    Call<EpisodeResponse> getEpisodes(@Query("page") int page);
}

