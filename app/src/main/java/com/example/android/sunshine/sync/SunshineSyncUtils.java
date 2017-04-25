package com.example.android.sunshine.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lfs-ios on 2017/4/25.
 */

public class SunshineSyncUtils {

    public static void startImmediateSync(final Context context) {

        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
