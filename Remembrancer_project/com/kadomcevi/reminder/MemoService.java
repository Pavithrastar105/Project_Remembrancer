package com.kadomcevi.reminder;

import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class MemoService extends MemoIntentService {
    public MemoService() {
        super("MemoService");
    }

    void doReminderWork(Intent intent) {
        Log.d("ReminderService", "Doing work.");
        Long rowId = Long.valueOf(intent.getExtras().getLong(DB.COLUMN_ID));
        Log.d("Intent id=", Long.toString(rowId.longValue()));
        NotificationManager mgr = (NotificationManager) getSystemService("notification");
        Intent notificationIntent = new Intent(this, InfoActivity.class);
        notificationIntent.putExtra(DB.COLUMN_ID, rowId);
        int id = (int) rowId.longValue();
        DB db = new DB(this);
        db.open();
        String msg = db.getMemo(rowId.longValue());
        db.close();
        mgr.notify(id, new Builder(this).setSmallIcon(C0064R.drawable.ic_memo).setAutoCancel(true).setTicker(msg).setContentText(msg).setContentIntent(PendingIntent.getActivity(this, id, notificationIntent, 268435456)).setWhen(System.currentTimeMillis()).setContentTitle("Reminder").setDefaults(-1).getNotification());
    }
}
