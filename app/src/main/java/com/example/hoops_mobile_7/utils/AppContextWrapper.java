package com.example.hoops_mobile_7.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import com.example.hoops_mobile_7.repository.sharedPrefs.SettingsPreferences;

import java.util.Locale;

public class AppContextWrapper extends ContextWrapper {

    public AppContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context) {
        SettingsPreferences prefs = new SettingsPreferences(context);

        String language = prefs.getLanguage();
        Locale newLocale = new Locale(language);
        Locale.setDefault(newLocale);

        float fontScale = 1.0f;
        switch (prefs.getTextSize()) {
            case 0: fontScale = 0.85f; break;
            case 1: fontScale = 1.0f; break;
            case 2: fontScale = 1.15f; break;
        }

        Resources res = context.getResources();
        Configuration configuration = new Configuration(res.getConfiguration());

        configuration.setLocale(newLocale);
        LocaleList localeList = new LocaleList(newLocale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);

        configuration.fontScale = fontScale;

        Context newContext = context.createConfigurationContext(configuration);
        return new AppContextWrapper(newContext);
    }
}