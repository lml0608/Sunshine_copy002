package com.example.android.sunshine.data;

import android.provider.BaseColumns;

/**
 * Created by lfs-ios on 2017/4/21.
 */

public class WeatherContract {

    public static final class WeatherEntry implements BaseColumns {

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
    }
}
