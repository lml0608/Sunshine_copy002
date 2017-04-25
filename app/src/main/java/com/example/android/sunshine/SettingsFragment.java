package com.example.android.sunshine;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.sync.SunshineSyncUtils;

/**
 * Created by lfs-ios on 2017/4/21.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";


    private void setPreferenceSummary(Preference preference, Object value) {

        String stringValue = value.toString();//值

        String key = preference.getKey();//键

        Log.i(TAG,"setPreferenceSummary:stringValue " + stringValue);
        Log.i(TAG,"setPreferenceSummary:key " + key);

        if (preference instanceof ListPreference) {
            /* For list preferences, look up the correct display value in */
            /* the preference's 'entries' list (since they have separate labels/values). */
            ListPreference listPreference = (ListPreference) preference;
            Log.i(TAG,"listPreference= " + listPreference);
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            Log.i(TAG,"prefIndex= " + prefIndex);
            if (prefIndex >= 0) {
                Log.i(TAG,"listPreference.getEntries()[prefIndex]= " + listPreference.getEntries()[prefIndex]);
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            Log.i(TAG, "location");
            preference.setSummary(stringValue);
        }

    }


    /**
     * 当设置的参数值发生变化时，调用
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Activity activity = getActivity();

        if (key.equals(getString(R.string.pref_location_key))) {
            SunshinePreferences.resetLocationCoordinates(activity);
            SunshineSyncUtils.startImmediateSync(activity);
        }  else if (key.equals(getString(R.string.pref_units_key))) {
            // units have changed. update lists of weather entries accordingly
            activity.getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI, null);
        }

        Log.i(TAG, "key=" + key);
        //获取指定key所对应的preference对象
        Preference preference = findPreference(key);
        Log.i(TAG, "preference=" + preference);
        if (null != preference) {
            Log.i(TAG, String.valueOf(preference instanceof CheckBoxPreference));//false
            //true
            if (!(preference instanceof CheckBoxPreference)) {
                //修改后的值
                Log.i(TAG, "value =" + sharedPreferences.getString(key, ""));
                setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
            }
        }

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_general);

        // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        Log.i(TAG, "count=" + count);//2
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);

            Log.i(TAG, "P=" + p);
            if (!(p instanceof CheckBoxPreference)) {

                String value = sharedPreferences.getString(p.getKey(), "");
                Log.i(TAG, "VALUE11=" + value);
                setPreferenceSummary(p, value);
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
                /* Unregister the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
                /* Register the preference change listener */
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


}
