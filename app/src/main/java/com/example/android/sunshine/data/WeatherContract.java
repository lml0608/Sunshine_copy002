package com.example.android.sunshine.data;

import android.net.Uri;
import android.os.ParcelUuid;
import android.provider.BaseColumns;

import com.example.android.sunshine.utilities.SunshineDateUtils;

/**
 * Created by lfs-ios on 2017/4/21.
 */

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.sunshine";
    public static final Uri BASE_CONTENT_URL = Uri.parse("content://" + CONTENT_AUTHORITY);
    //BASE_CONTENT_URL = content://com.example.android.sunshine
    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns {

        //CONTENT_URI = content://com.example.android.sunshine/weather
        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_WEATHER_ID = "weather_id";

        public static final String COLUMN_MIN_TEMP = "min";

        public static final String COLUMN_MAX_TEMP = "max";

        /*湿度*/
        public static final String COLUMN_HUMIDITY = "humidity";
        /*压力*/
        public static final String COLUMN_PRESSURE = "pressure";
        /*风速*/
        public static final String COLUMN_WIND_SPEED = "wind";

        public static final String COLUMN_DEGREES = "degrees";

        ////Uri = content://com.example.android.sunshine/weather/1493424000000
        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

        /**
         * Returns just the selection part of the weather query from a normalized today value.
         * This is used to get a weather forecast from today's date. To make this easy to use
         * in compound selection, we embed today's date as an argument in the query.
         *
         * @return The selection part of the weather query for today onwards
         */
        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
            return WeatherContract.WeatherEntry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }
    }
}
