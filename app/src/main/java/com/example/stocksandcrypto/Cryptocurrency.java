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
        this.marketCap = Utils.formatLargeNumber(marketCap);
        this.marketCapRank = marketCapRank;
        this.circulatingSupply = Utils.formatLargeNumber(circulatingSupply);
        this.totalSupply = Utils.formatLargeNumber(totalSupply);
        this.ath = Utils.formatLargeNumber(ath);
        this.athDate = Utils.formatDate(athDate);
        this.marketCapChange = Utils.formatLargeNumber(marketCapChange);
        this.marketCapChangePercent = Utils.formatLargeNumber(marketCapChangePercent);
        this.totalVolume = Utils.formatLargeNumber(totalVolume);
        this.low24h = Utils.formatLargeNumber(low24h);
        this.high24 = Utils.formatLargeNumber(high24);
        this.sparklineData7D = sparklineData7D;
    }

    public static Cryptocurrency parseData(String response) throws JSONException {
        JSONObject cryptodata = new JSONObject(response);
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
            cryptodata.getString("market_cap"),
            cryptodata.getString("market_cap_rank"),
            cryptodata.getString("circulating_supply"),
            cryptodata.getString("total_supply"),
            cryptodata.getString("ath"),
            cryptodata.getString("ath_date"),
            cryptodata.getString("market_cap_change_24h"),
            cryptodata.getString("market_cap_change_percentage_24h"),
            cryptodata.getString("total_volume"),
            cryptodata.getString("low_24h"),
            cryptodata.getString("high_24h"),
            sparklineData
        );
    }
}
