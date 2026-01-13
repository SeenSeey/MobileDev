package com.example.hoops_mobile_7.repository.sharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsPreferences {
    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_DARK_THEME = "dark_theme";
    private static final String KEY_TEXT_SIZE = "text_size";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_LANGUAGE = "language";

    private final SharedPreferences prefs;

    public SettingsPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isDarkTheme() {
        return prefs.getBoolean(KEY_DARK_THEME, false);
    }

    public void setDarkTheme(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply();
    }

    public int getTextSize() {
        return prefs.getInt(KEY_TEXT_SIZE, 1); // 0-small, 1-medium, 2-large
    }

    public void setTextSize(int index) {
        prefs.edit().putInt(KEY_TEXT_SIZE, index).apply();
    }

    public boolean isNotificationsEnabled() {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true);
    }

    public void setNotificationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply();
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "ru");
    }

    public void setLanguage(String lang) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply();
    }

    public void resetDefaults() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(KEY_DARK_THEME);
        editor.remove(KEY_TEXT_SIZE);
        editor.remove(KEY_LANGUAGE);
        editor.remove(KEY_NOTIFICATIONS);

        editor.apply();
    }
}


