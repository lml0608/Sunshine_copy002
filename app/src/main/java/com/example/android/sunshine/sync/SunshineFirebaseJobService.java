package com.example.android.sunshine.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.Matrix2f;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by lfs-ios on 2017/5/5.
 */

public class SunshineFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchWeatherTask;
    @Override
    public boolean onStartJob(final JobParameters job) {

        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Context context = getApplicationContext();

                SunshineSyncTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                Log.i("JOB", "JOB");
                jobFinished(job, false);
            }
        };
        mFetchWeatherTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mFetchWeatherTask != null) {
            mFetchWeatherTask.cancel(true);
        }

        return true;
    }
}
