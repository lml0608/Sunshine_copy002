package com.example.android.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);



        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);


    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]>{


        @Override
        protected String[] doInBackground(String... params) {
            return new String[0];
        }
    }

}
