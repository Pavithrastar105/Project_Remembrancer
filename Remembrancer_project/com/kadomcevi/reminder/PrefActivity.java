package com.kadomcevi.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PrefActivity extends PreferenceActivity {
    PendingIntent intent;

    /* renamed from: com.kadomcevi.reminder.PrefActivity.1 */
    class C00631 implements OnPreferenceClickListener {
        C00631() {
        }

        public boolean onPreferenceClick(Preference preference) {
            ((AlarmManager) PrefActivity.this.getSystemService("alarm")).set(1, System.currentTimeMillis() + 1000, PrefActivity.this.intent);
            System.exit(1);
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(C0064R.xml.preferences);
        Preference restart = findPreference("restart");
        this.intent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getIntent()), 0);
        restart.setOnPreferenceClickListener(new C00631());
    }
}
