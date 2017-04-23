package com.kadomcevi.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {
    private static final String TAG;

    static {
        TAG = ComponentInfo.class.getCanonicalName();
    }

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received wake up from alarm manager.");
        long rowid = intent.getExtras().getLong(DB.COLUMN_ID);
        MemoIntentService.acquireStaticLock(context);
        Intent i = new Intent(context, MemoService.class);
        i.putExtra(DB.COLUMN_ID, rowid);
        context.startService(i);
    }
}
