package com.example.android.sunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private TextView mWeatherDiaplay;

    private String mForecast;

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mWeatherDiaplay = (TextView) findViewById(R.id.tv_display_weather);

        if (getIntent() != null) {

            if (getIntent().hasExtra(Intent.EXTRA_TEXT)) {

                mForecast = getIntent().getStringExtra(Intent.EXTRA_TEXT);

                mWeatherDiaplay.setText(mForecast);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }
}
