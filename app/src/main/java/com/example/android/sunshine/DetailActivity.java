package com.example.android.sunshine;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private String mForecast;
    private TextView mWeatherDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);

        Intent i = getIntent();

        if (i != null) {

            if (i.hasExtra(Intent.EXTRA_TEXT)) {

                mForecast = i.getStringExtra(Intent.EXTRA_TEXT);

                mWeatherDisplay.setText(mForecast);
            }
        }


    }

    // TODO (4) Display the menu and implement the forecast sharing functionality

    //分享
    private Intent createShareForecastIntent() {

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }



    // TODO (3) Create a menu with an item with id of action_share

    //分享menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        menuItem.setIntent(createShareForecastIntent());

        return true;
    }



}
