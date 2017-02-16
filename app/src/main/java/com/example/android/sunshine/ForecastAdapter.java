package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lfs-ios on 2017/2/16.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;

    private final ForecastAdapterOnClickHandler mClickHandler;



    public interface ForecastAdapterOnClickHandler{
        void onClick(String weatherForday);
    }

    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }



    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public final TextView mWeatherTextView;
        public ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            String weatherForDay = mWeatherData[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.forecast_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {

        String weatherForThisDay = mWeatherData[position];

        holder.mWeatherTextView.setText(weatherForThisDay);

    }

    @Override
    public int getItemCount() {

        if (null == mWeatherData) {
            return 0;
        }
        return mWeatherData.length;
    }


    public void setWeatherData(String[] weatherData) {

        mWeatherData = weatherData;
        notifyDataSetChanged();
    }



}
