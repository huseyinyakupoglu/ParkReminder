package com.greyjacob.parkingapp;

import android.app.Activity;
import android.text.Html;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by hyakupoglu on 29-6-2017.
 */
// TODO change name to ParkModel
public class DataProvider {
    private String parkName;
    private String period;
    private String tariff;
    public Date creationTime;
    private String number;
    public Integer remainTime;
    private Integer period2;
    private String finish;

    public Integer getPeriod2() {
        return period2;
    }

    public void setPeriod2(Integer period2) {
        this.period2 = period2;
    }

    public void setRemainTime(Integer remainTime) {
        this.remainTime = remainTime;
    }

    public Integer getRemainTime() {
        return remainTime;
    }


    public String getParkingname() {
        return parkName;
    }

    public DataProvider(String parkName, String period, String tariff) {
        this.parkName = parkName;
        this.period = period;
        this.tariff = tariff;
    }

    public DataProvider(String number, String finish, Integer remainTime, String tariff, Calendar creationTime, Integer period2) {
        this.number = number;
        this.finish = finish;
        this.remainTime = remainTime;
        this.tariff = tariff;
        this.creationTime = creationTime.getTime();
        this.period2 = period2;
    }
    public void setParkName(String park_name) {
        this.parkName = park_name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getNumberPeriod() {
        return number;
    }

    public String getFinish() {
        return finish;
    }

}
