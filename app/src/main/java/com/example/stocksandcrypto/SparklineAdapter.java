package com.example.stocksandcrypto;

import com.robinhood.spark.SparkAdapter;

import java.util.ArrayList;
import java.util.Random;

public class SparklineAdapter extends SparkAdapter {
    public ArrayList<Float> yData;

    public SparklineAdapter(ArrayList<Float> yData) {
        this.yData = yData;
    }

    @Override
    public int getCount() {
        return yData.size();
    }

    @Override
    public Object getItem(int index) {
        return yData.get(index);
    }

    @Override
    public float getY(int index) {
        return yData.get(index);
    }
}