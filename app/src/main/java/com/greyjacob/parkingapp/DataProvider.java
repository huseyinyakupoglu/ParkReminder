package com.greyjacob.parkingapp;


import java.util.Calendar;
import java.util.Date;


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
    private String finish;



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
