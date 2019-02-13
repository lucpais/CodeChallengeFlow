package com.lucas.concept.codechallengeflw.model;

public class Weather {

    private String mCity;
    private String mTemp;
    private String mHumidity;
    private String mTempMax;
    private String mTempMin;
    private String mMainStatus;
    private String mDesc;
    private String mDate;
    private String mIconId;

    public Weather(){}

    public Weather(String mCity, String mTemp, String mHumidity, String mTempMax, String mTempMin, String mMainStatus, String mDesc, String mDate, String mIconId) {
        this.mCity = mCity;
        this.mTemp = mTemp;
        this.mHumidity = mHumidity;
        this.mTempMax = mTempMax;
        this.mTempMin = mTempMin;
        this.mMainStatus = mMainStatus;
        this.mDesc = mDesc;
        this.mDate = mDate;
        this.mIconId = mIconId;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        this.mCity = city;
    }

    public String getTemp() {
        return mTemp;
    }

    public void setTemp(String temp) {
        this.mTemp = temp;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        this.mHumidity = humidity;
    }

    public String getTempMax() {
        return mTempMax;
    }

    public void setTempMax(String tempMax) {
        this.mTempMax = tempMax;
    }

    public String getTempMin() {
        return mTempMin;
    }

    public void setTempMin(String tempMin) {
        this.mTempMin = tempMin;
    }

    public String getMainStatus() {
        return mMainStatus;
    }

    public void setMainStatus(String mainStatus) {
        this.mMainStatus = mainStatus;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        this.mDesc = desc;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getIconId() {
        return mIconId;
    }

    public void setIconId(String iconId) {
        this.mIconId = iconId;
    }
}
