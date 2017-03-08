package com.example.android.sunshine;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


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

    private Intent createShareForecastIntent(){

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

}
