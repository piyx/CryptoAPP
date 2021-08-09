package com.example.stocksandcrypto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cryptocurrency {
    String id;
    String symbol;
    String name;
    String currentPrice;
    String priceChange;
    String priceChangePercent;
    String marketCap;
    String marketCapRank;
    String circulatingSupply;
    String totalSupply;
    String ath;
    String low24h;
    String high24;
    ArrayList<Float> sparklineData;


    public Cryptocurrency(String id, String symbol, String name, String currentPrice, String priceChange, String priceChangePercent, String marketCap, String marketCapRank, String circulatingSupply, String totalSupply, String ath, String low24h, String high24, ArrayList<Float> sparklineData) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.marketCap = marketCap;
        this.marketCapRank = marketCapRank;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.ath = ath;
        this.low24h = low24h;
        this.high24 = high24;
        this.sparklineData = sparklineData;
    }

    public static Cryptocurrency parseData(String response) throws JSONException {
        JSONArray jsonarray = new JSONArray(response);
        JSONObject cryptodata = jsonarray.getJSONObject(0);
        JSONArray sparkline = cryptodata.getJSONObject("sparkline_in_7d").getJSONArray("price");
        ArrayList<Float> sparklineData = new ArrayList<>();
        for (int i = 0; i < sparkline.length(); i++) {
            sparklineData.add(Float.parseFloat(sparkline.getString(i)));
            
        }

        return new Cryptocurrency(
            cryptodata.getString("id"),
            cryptodata.getString("symbol"),
            cryptodata.getString("name"),
            cryptodata.getString("current_price"),
            cryptodata.getString("price_change_24h"),
            cryptodata.getString("price_change_percentage_24h"),
            cryptodata.getString("market_cap"),
            cryptodata.getString("market_cap_rank"),
            cryptodata.getString("circulating_supply"),
            cryptodata.getString("total_supply"),
            cryptodata.getString("ath"),
            cryptodata.getString("low_24h"),
            cryptodata.getString("high_24h"),
            sparklineData
        );
    }
}
