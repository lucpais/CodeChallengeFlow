package com.lucas.concept.codechallengeflw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lucas.concept.codechallengeflw.controller.IInformationAvailableListener;
import com.lucas.concept.codechallengeflw.controller.WeatherController;
import com.lucas.concept.codechallengeflw.model.Weather;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements IInformationAvailableListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private TextView mCurrentDateTv;
    private TextView mCurrentTempTv;
    private TextView mCurrentMaxTv;
    private TextView mCurrentMinTv;
    private TextView mCurrentDescTv;
    private ImageView mCurrentWeatherImage;
    private ProgressBar mCurrentInfoProgressBar;
    private ProgressBar mForecastInfoProgressBar;
    private RelativeLayout mCurrentInfoRL;
    private RecyclerView mRecyclerView;
    private WeatherAdapter mWeatherAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initViews();
        WeatherController.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.locations_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cordoba:
                getCityInfo(WeatherController.CBA_CITY_ID);
                break;
            case R.id.menu_berlin:
                getCityInfo(WeatherController.BERLIN_CITY_ID);
                break;
            case R.id.menu_bogota:
                getCityInfo(WeatherController.BOGOTA_CITY_ID);
                break;
            case R.id.menu_huston:
                getCityInfo(WeatherController.HUSTON_CITY_ID);
                break;
            case R.id.menu_ny:
                getCityInfo(WeatherController.NY_CITY_ID);
                break;
        }
        return true;
    }

    private void getCityInfo(String cityId) {
        setCurrentInfoInProgress(true);
        setForecastInfoInProgress(true);
        WeatherController.getInstance(this).pupulateInfo(cityId);
    }

    private void initViews() {
        mCurrentInfoProgressBar = findViewById(R.id.current_info_progressbar);
        mCurrentInfoRL = findViewById(R.id.current_info_layout);
        setCurrentInfoInProgress(true);
        mCurrentWeatherImage = findViewById(R.id.current_weather_image);
        mCurrentDateTv = findViewById(R.id.current_date);
        mCurrentMaxTv = findViewById(R.id.current_max_temp);
        mCurrentMinTv = findViewById(R.id.current_min_temp);
        mCurrentDescTv = findViewById(R.id.current_description);
        mCurrentTempTv = findViewById(R.id.current_temp);

        mRecyclerView = findViewById(R.id.forecast_list);
        mForecastInfoProgressBar = findViewById(R.id.forecast_info_progressbar);
        setForecastInfoInProgress(true);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager) mLayoutManager).getOrientation());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mWeatherAdapter = new WeatherAdapter(this);
        mRecyclerView.setAdapter(mWeatherAdapter);
        refreshCurrentWeather();
        refreshForecastWeather();
    }

    private void setForecastInfoInProgress(boolean progressVisible) {
        Log.d(TAG, "progress set visible: " + progressVisible);
        if (progressVisible) {
            mForecastInfoProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mForecastInfoProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setCurrentInfoInProgress(boolean progressVisible) {
        Log.d(TAG, "progress set visible: " + progressVisible);
        if (progressVisible) {
            mCurrentInfoProgressBar.setVisibility(View.VISIBLE);
            mCurrentInfoRL.setVisibility(View.INVISIBLE);
        } else {
            mCurrentInfoProgressBar.setVisibility(View.GONE);
            mCurrentInfoRL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshCurrentWeather() {
        Log.d(TAG,"Current weather now available");
        Weather thisWeather = WeatherController.getInstance(this).getCurrentData();
        if (thisWeather != null) {
            mCurrentDateTv.setText(thisWeather.getDate());
            mCurrentMaxTv.setText(thisWeather.getTempMax());
            mCurrentMinTv.setText(thisWeather.getTempMin());
            mCurrentTempTv.setText(thisWeather.getTemp());
            mCurrentDescTv.setText(thisWeather.getMainStatus() + ", " + thisWeather.getDesc());
            Picasso.with(this)
                    .load(WeatherController.baseWeatherICON + thisWeather.getIconId() + ".png")
                    .resize((int) this.getResources().getDimension(R.dimen.width_image_thumb), (int) this.getResources().getDimension(R.dimen.height_image_thumb))
                    .error(android.R.drawable.ic_menu_camera)
                    .placeholder(android.R.drawable.ic_menu_camera)
                    .into(mCurrentWeatherImage);
            setCurrentInfoInProgress(false);
        }
    }

    @Override
    public void refreshForecastWeather() {
        Log.d(TAG,"Forecast weather now available");
        if (WeatherController.getInstance(this).getForecastData() != null &&
                !WeatherController.getInstance(this).getForecastData().isEmpty()) {
            setForecastInfoInProgress(false);
            mWeatherAdapter.notifyDataSetChanged();
        }
    }
}
