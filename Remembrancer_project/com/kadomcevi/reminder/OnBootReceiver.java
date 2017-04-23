package com.kadomcevi.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import java.util.Calendar;
import java.util.Date;

public class OnBootReceiver extends BroadcastReceiver {
    private static final String TAG;

    static {
        TAG = ComponentInfo.class.getCanonicalName();
    }

    public void onReceive(Context context, Intent intent) {
        MemoManager reminderMgr = new MemoManager(context);
        DB db = new DB(context);
        db.open();
        Cursor cursor = db.getAllData();
        if (cursor != null) {
            cursor.moveToFirst();
            int rowIdColumnIndex = cursor.getColumnIndex(DB.COLUMN_ID);
            int dateTimeColumnIndex = cursor.getColumnIndex(DB.COLUMN_DATE);
            int memoColumnIndex = cursor.getColumnIndex(DB.COLUMN_TXT);
            while (!cursor.isAfterLast()) {
                Long rowId = Long.valueOf(cursor.getLong(rowIdColumnIndex));
                long dateTime = cursor.getLong(dateTimeColumnIndex);
                String memo = cursor.getString(memoColumnIndex);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(dateTime));
                reminderMgr.setReminder(rowId, cal);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
    }
}
