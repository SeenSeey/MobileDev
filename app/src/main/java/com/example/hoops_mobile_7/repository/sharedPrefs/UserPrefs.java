package com.example.hoops_mobile_7.repository.sharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hoops_mobile_7.model.User;

public class UserPrefs {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";

    public static void saveUser(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_EMAIL, user.getEmail())
                .putString(KEY_PASSWORD, user.getPassword())
                .putString(KEY_AGE, user.getAge())
                .putString(KEY_GENDER, user.getGender())
                .apply();
    }

    public static User getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String email = prefs.getString(KEY_EMAIL, null);
        String password = prefs.getString(KEY_PASSWORD, null);
        String age = prefs.getString(KEY_AGE, null);
        String gender = prefs.getString(KEY_GENDER, null);

        if (email != null && password != null && age != null && gender != null) {
            return new User(email, password, age, gender);
        }
        return null;
    }

    public static void clearUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
