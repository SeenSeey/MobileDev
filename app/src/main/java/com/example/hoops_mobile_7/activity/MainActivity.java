package com.example.hoops_mobile_7.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hoops_mobile_7.R;
import com.example.hoops_mobile_7.fragments.HomeFragmentDirections;
import com.example.hoops_mobile_7.model.User;
import com.example.hoops_mobile_7.repository.sharedPrefs.SettingsPreferences;
import com.example.hoops_mobile_7.repository.sharedPrefs.UserPrefs;
import com.example.hoops_mobile_7.utils.AppContextWrapper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(AppContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsPreferences prefs = new SettingsPreferences(this);
        int nightMode = prefs.isDarkTheme() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);

        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) return;

        NavController navController = navHostFragment.getNavController();

        User user = UserPrefs.getUser(this);
        if (user != null) {
            NavDirections action =
                    (NavDirections) HomeFragmentDirections.actionGlobalHomeFragment(user);
            navController.navigate(action);
        }
    }
}
