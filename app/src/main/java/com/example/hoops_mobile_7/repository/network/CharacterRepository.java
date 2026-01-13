package com.example.hoops_mobile_7.repository.network;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.hoops_mobile_7.database.AppDatabase;
import com.example.hoops_mobile_7.database.CharacterDao;
import com.example.hoops_mobile_7.network.RetrofitClient;
import com.example.hoops_mobile_7.network.RickApiService;
import com.example.hoops_mobile_7.network.model.character.Character;
import com.example.hoops_mobile_7.network.model.character.CharacterResponse;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterRepository {
    private final RickApiService api;
    private final CharacterDao dao;

    public CharacterRepository(Context context) {
        api = RetrofitClient.getApi();
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.characterDao();
    }

    public Observable<List<Character>> getCharactersFromDb() {
        return dao.getAllCharacters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable clearAllCharacters() {
        return dao.deleteAll()
                .subscribeOn(Schedulers.io());
    }

    public void loadApiAndSaveToDb(int page, boolean isRefresh, Runnable onComplete, Runnable onError) {
        Call<CharacterResponse> call = api.getCharacters(page);
        call.enqueue(new Callback<CharacterResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Character> list = response.body().getResults();

                    if (isRefresh) {
                        dao.deleteAll()
                                .andThen(dao.insertAll(list))
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                    if (onComplete != null) onComplete.run();
                                }, throwable -> {
                                    if (onError != null) onError.run();
                                });
                    } else {
                        dao.insertAll(list)
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                    if (onComplete != null) onComplete.run();
                                }, throwable -> {
                                    if (onError != null) onError.run();
                                });
                    }
                } else {
                    if (onError != null) onError.run();
                }
            }

            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                if (onError != null) onError.run();
            }
        });
    }
}
