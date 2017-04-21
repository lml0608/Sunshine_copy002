/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class OpenWeatherJsonUtils {

    private static final String OWM_CITY = "city";
    private static final String OWM_COORD = "coord";

    private static final String OWN_LATITUDE = "lat";
    private static final String OWN_LONGITUDE = "lon";
        /*每天的天气信息都是一个list数组里*/
    private static final String OWM_LIST = "list";

    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";

    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";

    /*所有温度都在temp对象里*/
    static final String OWM_TEMPERATURE = "temp";

    /* Max temperature for the day */
    static final String OWM_MAX = "max";
    static final String OWM_MIN = "min";

    //weather对象
    static final String OWM_WEATHER = "weather";

    static final String OWM_WEATHER_ID = "id";
    //天气描述
    static final String OWM_DESCRIPTION = "main";

    static final String OWM_MESSAGE_CODE = "cod";

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param forecastJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     *
     * 解析数据，保存在数组，返回数据
     */
    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {



        /* String array to hold each day's weather String */
        //每天的天气放入数组
        String[] parsedWeatherData = null;

        //jsonbody
        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                //"cod":"200"
                case HttpURLConnection.HTTP_OK:
                    break;
                //"cod":"404"
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }
        //list数组中保存所有天气信息
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);//list

        //parsedWeatherData数组用来保存每天的天气
        parsedWeatherData = new String[weatherArray.length()];

        //时间处理
        long localDate = System.currentTimeMillis();
        long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
        long startDay = SunshineDateUtils.normalizeDate(utcDate);

        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highAndLow;

            /* These are the values that will be collected */
            //这些字段放入数组
            long dateTimeMillis;
            double high;//最高温度
            double low;//最低温度
            String description;//天气描述

            /* Get the JSON object representing the day */
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i;
            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false);

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             *
             * weather数组的里的数据
             */
            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);//main  "main":"Light Rain",

            /*
             * Temperatures are sent by Open Weather Map in a child object called "temp".
             *
             * Editor's Note: Try not to name variables "temp" when working with temperature.
             * It confuses everybody. Temp could easily mean any number of things, including
             * temperature, temporary and is just a bad variable name.
             */
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);//"temp"
            high = temperatureObject.getDouble(OWM_MAX);//"max":18.27,
            low = temperatureObject.getDouble(OWM_MIN);//"min":13.32,
            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low); //(max / min)
            //每日天气
            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
        }
        //返回每日天气
        return parsedWeatherData;
    }

    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }
}