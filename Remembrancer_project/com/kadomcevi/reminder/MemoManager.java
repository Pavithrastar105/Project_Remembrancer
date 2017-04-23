package com.kadomcevi.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Calendar;

public class MemoManager {
    private AlarmManager mAlarmManager;
    private Context mContext;
    private SharedPreferences preferences;

    public MemoManager(Context context) {
        this.mContext = context;
        this.mAlarmManager = (AlarmManager) context.getSystemService("alarm");
    }

    public void setReminder(Long id, Calendar when) {
        if (Calendar.getInstance().getTimeInMillis() < when.getTimeInMillis()) {
            int minutes;
            Intent i = new Intent(this.mContext, OnAlarmReceiver.class);
            i.putExtra(DB.COLUMN_ID, id.longValue());
            PendingIntent pi = PendingIntent.getBroadcast(this.mContext, (int) id.longValue(), i, 134217728);
            this.preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
            try {
                minutes = Integer.parseInt(this.preferences.getString("alarm_repeat_minutes", "0"));
            } catch (NumberFormatException e) {
                minutes = 0;
            }
            if (minutes == 0) {
                this.mAlarmManager.set(0, when.getTimeInMillis(), pi);
            } else {
                this.mAlarmManager.setRepeating(0, when.getTimeInMillis(), (long) (60000 * minutes), pi);
            }
        }
    }
}
