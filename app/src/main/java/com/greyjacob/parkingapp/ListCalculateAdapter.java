package com.greyjacob.parkingapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyakupoglu on 29-6-2017.
 */

public class ListCalculateAdapter extends ArrayAdapter {
    private List list = new ArrayList();
    public ListCalculateAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    static class LayoutHandler{
        TextView NUMBER, FINISH, REMAINED, TARIFF;
    }

    public void ekle(DataProvider dataProvider)
    {
        add(dataProvider);
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if (row==null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            row = layoutInflater.inflate(R.layout.row_calculate,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.NUMBER = row.findViewById(R.id.number);
            layoutHandler.FINISH = row.findViewById(R.id.finish);
            layoutHandler.REMAINED = row.findViewById(R.id.remainTime);
            layoutHandler.TARIFF = row.findViewById(R.id.tariff);
            row.setTag(layoutHandler);
        } else {
            //TODO test
            layoutHandler = (LayoutHandler) row.getTag();
        }
        DataProvider dataProvider = (DataProvider) this.getItem(position);  //Test this
        layoutHandler.NUMBER.setText(dataProvider.getNumberPeriod());
        layoutHandler.FINISH.setText(dataProvider.getFinish());
        int remainTime = dataProvider.getRemainTime();
        layoutHandler.REMAINED.setText(String.valueOf(dataProvider.getRemainTime())+" min");
        layoutHandler.TARIFF.setText("€"+dataProvider.getTariff());
        if(remainTime==0){
            layoutHandler.NUMBER.setTextColor(Color.RED);
            layoutHandler.REMAINED.setText("Finished!");
            layoutHandler.REMAINED.setTextColor(Color.RED);
            layoutHandler.FINISH.setTextColor(Color.RED);
            layoutHandler.TARIFF.setTextColor(Color.RED);
        }else{
            layoutHandler.NUMBER.setTextColor(Color.parseColor("#ff27CA8E"));
            layoutHandler.REMAINED.setTextColor(Color.parseColor("#ff27CA8E"));
            layoutHandler.FINISH.setTextColor(Color.parseColor("#ff27CA8E"));
            layoutHandler.TARIFF.setTextColor(Color.parseColor("#ff27CA8E"));
        }
        return row;
    }



}
