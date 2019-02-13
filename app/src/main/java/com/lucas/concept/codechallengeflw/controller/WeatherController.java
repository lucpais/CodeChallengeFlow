package com.lucas.concept.codechallengeflw.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lucas.concept.codechallengeflw.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherController {

    private static final String TAG = WeatherController.class.getCanonicalName();
    public static final String DEVICE_INDEX = "device_index";
    private static WeatherController mInstance;
    private final IInformationAvailableListener mIInformationAvailableListener;
    private Context mContext;
    private Weather mCurrentData;
    private List<Weather> mForecastData;
    private static final String baseWeatherAPI = "http://api.openweathermap.org/data/2.5/weather?id=";
    private static final String baseForecastAPI = "http://api.openweathermap.org/data/2.5/forecast?id=";
    public static final String baseWeatherICON = "http://openweathermap.org/img/w/";
    private static final String APP_ID = "&appid=373089adedbd06efa2a288b94442dad0&units=metric";
    public static final String CBA_CITY_ID = "3846616";
    public static final String BERLIN_CITY_ID = "2950159";
    public static final String BOGOTA_CITY_ID = "3688689";
    public static final String HUSTON_CITY_ID = "5601933";
    public static final String NY_CITY_ID = "5128581";

    public static WeatherController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WeatherController(context);
        }
        return mInstance;
    }

    private WeatherController(Context context) {
        mContext = context;
        mForecastData = new ArrayList<>();
        mIInformationAvailableListener = (IInformationAvailableListener) context;
        pupulateInfo(CBA_CITY_ID);
    }

    public void pupulateInfo(String selectedCity) {

        String url = baseWeatherAPI + selectedCity + APP_ID;
        String forecastUrl = baseForecastAPI + selectedCity + APP_ID;

        JsonObjectRequest currentWeatherRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mCurrentData = new Weather();
                    mCurrentData.setCity(response.get("name").toString());
                    mCurrentData.setDate(formatDate(response.get("dt").toString()));

                    JSONObject mainObject = response.getJSONObject("main");
                    mCurrentData.setHumidity(mainObject.getString("humidity"));
                    mCurrentData.setTemp(formatTemp(mainObject.getString("temp")));
                    mCurrentData.setTempMax(formatTemp(mainObject.getString("temp_max")));
                    mCurrentData.setTempMin(formatTemp(mainObject.getString("temp_min")));

                    JSONArray weatherArray = response.getJSONArray("weather");
                    for (int index = 0; index < weatherArray.length(); index++) {
                        JSONObject currentWeather = weatherArray.getJSONObject(index);
                        mCurrentData.setMainStatus(currentWeather.getString("main"));
                        mCurrentData.setDesc(currentWeather.getString("description"));
                        mCurrentData.setIconId(currentWeather.getString("icon"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mIInformationAvailableListener.refreshCurrentWeather();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        JsonObjectRequest forecastWeatherRequest = new JsonObjectRequest(forecastUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mForecastData.clear();
                    JSONArray forecastArray = response.getJSONArray("list");
                    for (int index = 0; index < 5; index++) {
                        Weather day = new Weather();
                        JSONObject currentWeather = forecastArray.getJSONObject(index);
                        day.setDate(formatDate(currentWeather.getString("dt")));

                        JSONObject mainInfo = currentWeather.getJSONObject("main");
                        day.setTempMax(formatTemp(mainInfo.getString("temp_max")));
                        day.setTempMin(formatTemp(mainInfo.getString("temp_min")));
                        day.setTemp(formatTemp(mainInfo.getString("temp")));

                        JSONArray weatherArray = currentWeather.getJSONArray("weather");
                        for (int i = 0; i < weatherArray.length(); i++) {
                            JSONObject thisWeather = weatherArray.getJSONObject(i);
                            day.setMainStatus(thisWeather.getString("main"));
                            day.setDesc(thisWeather.getString("description"));
                            day.setIconId(thisWeather.getString("icon"));
                        }
                        mForecastData.add(day);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mIInformationAvailableListener.refreshForecastWeather();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(currentWeatherRequest);
        requestQueue.add(forecastWeatherRequest);
    }

    public Weather getCurrentData() {
        return mCurrentData;
    }

    public List<Weather> getForecastData() {
        return mForecastData;
    }

    private String formatDate(String epochDate) {
        Date date = new Date(Long.parseLong(epochDate) * 1000);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        return dateFormat.format(date);
    }

    private String formatTemp(String temp){
        return temp + "°";
    }
}
