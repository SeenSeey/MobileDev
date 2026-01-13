package com.example.hoops_mobile_7.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hoops_mobile_7.network.model.character.Character;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Character> characters);

    @Query("SELECT * FROM characters")
    Observable<List<Character>> getAllCharacters();

    @Query("DELETE FROM characters")
    Completable deleteAll();
}
