package com.pedometer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

import java.util.Calendar;


public class WakeLockUtils {

    private static PowerManager.WakeLock mWakeLock;

    @SuppressLint("InvalidWakeLockTag")
    public synchronized static PowerManager.WakeLock getLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            mWakeLock.acquire();
        }
        return (mWakeLock);
    }

}
