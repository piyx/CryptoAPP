package com.example.stocksandcrypto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Cryptocurrency implements Serializable {
    String id;
    String symbol;
    String name;
    String currentPrice;
    String priceChange;
    String marketCap;
    String marketCapRank;
    String circulatingSupply;
    String totalSupply;
    String ath;
    String athDate;
    String marketCapChange;
    String marketCapChangePercent;
    String totalVolume;
    String low24h;
    String high24;
    ArrayList<Float> sparklineData7D;

    public Cryptocurrency(String id, String symbol, String name, String currentPrice,
                          String priceChange, String marketCap, String marketCapRank,
                          String circulatingSupply, String totalSupply, String ath,
                          String athDate, String marketCapChange, String marketCapChangePercent,
                          String totalVolume, String low24h, String high24, ArrayList<Float> sparklineData7D) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.marketCap = marketCap;
        this.marketCapRank = marketCapRank;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.ath = ath;
        this.athDate = athDate;
        this.marketCapChange = marketCapChange;
        this.marketCapChangePercent = marketCapChangePercent;
        this.totalVolume = totalVolume;
        this.low24h = low24h;
        this.high24 = high24;
        this.sparklineData7D = sparklineData7D;
    }

    public static Cryptocurrency parseData(String response) throws JSONException {
        JSONObject cryptodata = new JSONObject(response);
        JSONArray sparkline = cryptodata.getJSONObject("sparkline_in_7d").getJSONArray("price");
        ArrayList<Float> sparklineData = new ArrayList<>();
        for (int i = 0; i < sparkline.length(); i++) {
            sparklineData.add(Float.parseFloat(sparkline.getString(i)));
        }

        String id = cryptodata.getString("id");
        String symbol = cryptodata.getString("symbol");
        String name = cryptodata.getString("name");
        String currentPrice = cryptodata.getString("current_price");
        String priceChange = cryptodata.getString("price_change_24h");
        String marketCap = cryptodata.getString("market_cap");
        String marketCapRank = cryptodata.getString("market_cap_rank");
        String circulatingSupply = cryptodata.getString("circulating_supply");
        String totalSupply = cryptodata.getString("total_supply");
        String ath = cryptodata.getString("ath");
        String athDate = cryptodata.getString("ath_date");
        String marketCapChange = cryptodata.getString("market_cap_change_24h");
        String marketCapChangePercent = cryptodata.getString("market_cap_change_percentage_24h");
        String totalvolume = cryptodata.getString("total_volume");
        String low24h = cryptodata.getString("low_24h");
        String high24h = cryptodata.getString("high_24h");

        return new Cryptocurrency(
            id,
            symbol,
            name,
            currentPrice,
            priceChange,
            Utils.formatLargeNumber(marketCap),
            marketCapRank,
            Utils.formatLargeNumber(circulatingSupply),
            Utils.formatLargeNumber(totalSupply),
            Utils.formatPrice(ath),
            Utils.formatDate(athDate),
            Utils.formatPrice(marketCapChange),
            Utils.formatPercentChange(marketCapChangePercent),
            Utils.formatPrice(totalvolume),
            Utils.formatPrice(low24h),
            Utils.formatPrice(high24h),
            sparklineData
        );
    }
}
