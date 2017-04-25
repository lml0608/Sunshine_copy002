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
package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts to a
 * {@link RecyclerView}
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final Context mContext;


    private final ForecastAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;


    /**
     * 回调接口，
     */
    public interface ForecastAdapterOnClickHandler {
        //点击RecyclerView每一个条目的事件
        void onClick(long date);
    }

    // COMPLETED (4) Add a ForecastAdapterOnClickHandler as a parameter to the constructor and store it in mClickHandler
    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        //获取ForecastAdapterOnClickHandler对象
        mClickHandler = clickHandler;
    }

    // COMPLETED (5) Implement OnClickListener in the ForecastAdapterViewHolder class
    /**
     * Cache of the children views for a forecast list item.
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;
        final ImageView iconView;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);

            view.setOnClickListener(this);
        }

        // COMPLETED (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method
        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis= mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(dateInMillis);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.forecast_list_item, viewGroup, false);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        int weatherImageId;

        weatherImageId = SunshineWeatherUtils
                .getSmallArtResourceIdForWeatherCondition(weatherId);

        forecastAdapterViewHolder.iconView.setImageResource(weatherImageId);

        /****************
         * Weather Date *
         ****************/
         /* Read date from the cursor */
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
         /* Get human readable string using our utility method */
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);

         /* Display friendly date string */
        forecastAdapterViewHolder.dateView.setText(dateString);

        /***********************
         * Weather Description *
         ***********************/
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
         /* Create the accessibility (a11y) String from the weather description */
        String descriptionA11y = mContext.getString(R.string.a11y_forecast, description);

         /* Set the text and content description (for accessibility purposes) */
        forecastAdapterViewHolder.descriptionView.setText(description);
        forecastAdapterViewHolder.descriptionView.setContentDescription(descriptionA11y);

        /**************************
         * High (max) temperature *
         **************************/
         /* Read high temperature from the cursor (in degrees celsius) */
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
         /*
          * If the user's preference for weather is fahrenheit, formatTemperature will convert
          * the temperature. This method will also append either °C or °F to the temperature
          * String.
          */
        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
         /* Create the accessibility (a11y) String from the weather description */
        String highA11y = mContext.getString(R.string.a11y_high_temp, highString);

         /* Set the text and content description (for accessibility purposes) */
        forecastAdapterViewHolder.highTempView.setText(highString);
        forecastAdapterViewHolder.highTempView.setContentDescription(highA11y);

        /*************************
         * Low (min) temperature *
         *************************/
         /* Read low temperature from the cursor (in degrees celsius) */
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
         /*
          * If the user's preference for weather is fahrenheit, formatTemperature will convert
          * the temperature. This method will also append either °C or °F to the temperature
          * String.
          */
        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
        String lowA11y = mContext.getString(R.string.a11y_low_temp, lowString);

         /* Set the text and content description (for accessibility purposes) */
        forecastAdapterViewHolder.lowTempView.setText(lowString);
        forecastAdapterViewHolder.lowTempView.setContentDescription(lowA11y);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }


    void swapCursor(Cursor newCursor) {

        mCursor = newCursor;
        notifyDataSetChanged();
    }


}