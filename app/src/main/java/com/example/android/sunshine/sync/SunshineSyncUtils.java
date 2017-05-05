package com.example.android.sunshine.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.internal.widget.PreferenceImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.example.android.sunshine.data.WeatherContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by lfs-ios on 2017/4/25.
 */

public class SunshineSyncUtils {

//    private static final int SYNC_INTERVAL_HOURS = 3;
//    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
//    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;


    private static boolean sInitialized;

    private static final String SUNSHINE_SYNC_TAG = "sunshine-sync";

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncSunshineJob = dispatcher.newJobBuilder()
                .setService(SunshineFirebaseJobService.class)
                .setTag(SUNSHINE_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .build();
        dispatcher.schedule(syncSunshineJob);
    }



    //禁止重复初始化数据

    synchronized public static void initialize (@NonNull final Context context) {

        if (sInitialized) return;

        //当方法中的代码执行的时候，sInitialized设置为true

        sInitialized = true;

        //开启job服务
        scheduleFirebaseJobDispatcherSync(context);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Uri forecastQueryUrl = WeatherContract.WeatherEntry.CONTENT_URI;
                String[] projectionColumns = {WeatherContract.WeatherEntry._ID};
                String selectionStatement = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                Cursor cursor = context.getContentResolver().query(
                        forecastQueryUrl,
                        projectionColumns,
                        selectionStatement,
                        null,
                        null);

                //用户新安装就会为空,
                if (null == cursor || cursor.getCount() == 0) {
                    //启动服务的方法
                    startImmediateSync(context);
                }
                cursor.close();
                return null;
            }
        }.execute();

    }

    public static void startImmediateSync(final Context context) {

        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
