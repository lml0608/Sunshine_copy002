package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by lfs-ios on 2017/4/25.
 */

public class SunshineSyncIntentService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SunshineSyncIntentService(String name) {
        super(name);
    }

    public SunshineSyncIntentService() {
        super("SunshineSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SunshineSyncTask.syncWeather(this);

    }
}
