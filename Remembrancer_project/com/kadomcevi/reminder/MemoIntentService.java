package com.kadomcevi.reminder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public abstract class MemoIntentService extends IntentService {
    public static final String LOCK_NAME_STATIC = "com.kadomcevi.reminder.Static";
    private static WakeLock lockStatic;

    abstract void doReminderWork(Intent intent);

    static {
        lockStatic = null;
    }

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    private static synchronized WakeLock getLock(Context context) {
        WakeLock wakeLock;
        synchronized (MemoIntentService.class) {
            if (lockStatic == null) {
                lockStatic = ((PowerManager) context.getSystemService("power")).newWakeLock(1, LOCK_NAME_STATIC);
                lockStatic.setReferenceCounted(true);
            }
            wakeLock = lockStatic;
        }
        return wakeLock;
    }

    public MemoIntentService(String name) {
        super(name);
    }

    protected final void onHandleIntent(Intent intent) {
        try {
            doReminderWork(intent);
        } finally {
            getLock(this).release();
        }
    }
}
