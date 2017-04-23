package com.kadomcevi.reminder;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.Locale;

public class ReminderApp extends Application {
    private String lang;
    private Locale locale;
    private SharedPreferences preferences;

    public void onCreate() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.lang = this.preferences.getString("lang", "default");
        if (this.lang.equals("default")) {
            this.lang = getResources().getConfiguration().locale.getCountry();
        }
        this.locale = new Locale(this.lang);
        Locale.setDefault(this.locale);
        Configuration config = new Configuration();
        config.locale = this.locale;
        Log.i("Lang change", "Locale=" + this.locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        getApplicationContext().setTheme(C0064R.style.AppBaseTheme.Light);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.locale = new Locale(this.lang);
        Locale.setDefault(this.locale);
        Configuration config = new Configuration();
        config.locale = this.locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
