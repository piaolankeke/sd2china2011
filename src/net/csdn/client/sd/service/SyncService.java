package net.csdn.client.sd.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class SyncService extends IntentService {
    private static final String TAG = "SyncService";


    public static final int STATUS_RUNNING = 0x1;
    public static final int STATUS_ERROR = 0x2;
    public static final int STATUS_FINISHED = 0x3;

    public SyncService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent(intent=" + intent.toString() + ")");
    }

}
