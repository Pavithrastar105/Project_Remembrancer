package com.kadomcevi.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ThemedActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String theme = PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "default");
        if (theme.equals("default")) {
            setTheme(C0064R.style.AppTheme);
        }
        if (theme.equals("light")) {
            setTheme(C0064R.style.AppTheme.Light);
        }
        if (theme.equals("black")) {
            setTheme(C0064R.style.AppTheme.Black);
        }
    }
}
