package com.example.stocksandcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemDetails extends AppCompatActivity {
    TextView itemname, priceChangeTV, timeline;
    TickerView tickerView;
    SparkView sparkline;
    Cryptocurrency cryptocurrency;
    RadioGroup radioGroup;
    SparklineAdapter adapter;
    HashMap<String, Timeline> timelineMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        sparkline = findViewById(R.id.sparkline);
        itemname = findViewById(R.id.itemname);
        priceChangeTV = findViewById(R.id.priceChange);
        timeline = findViewById(R.id.timeline);
        radioGroup = findViewById(R.id.radiogroup);

        timelineMap.put("1D", Timeline.ONEDAY);
        timelineMap.put("1W", Timeline.ONEWEEK);
        timelineMap.put("1M", Timeline.ONEMONTH);
        timelineMap.put("3M", Timeline.THREEMONTHS);
        timelineMap.put("1Y", Timeline.ONEYEAR);
        timelineMap.put("5Y", Timeline.FIVEYEARS);



        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            getSparkline(timelineMap.get(rb.getText()));
        });

        tickerView = findViewById(R.id.tickerView);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());

        String itemdata = getIntent().getStringExtra("itemdata");
        try {
            cryptocurrency = Cryptocurrency.parseData(itemdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setInitialData();

        adapter = new SparklineAdapter(cryptocurrency.sparklineData7D);
        sparkline.setAdapter(adapter);
        sparkline.setScrubListener(
            value -> {
                if (value == null) { setInitialData(); }
                else {
                    float initialValue = sparkline.getAdapter().getY(0);
                    float priceChange = (float)value - initialValue;
                    float priceChangePercent = priceChange/initialValue * 100;
                    setPriceChange(priceChange, priceChangePercent);
                    tickerView.setText("$"+value.toString());
                }
            }
        );
    }

    protected void setInitialData() {
        itemname.setText(cryptocurrency.name);
        tickerView.setText("$" + cryptocurrency.currentPrice);

        float priceChange = Float.parseFloat(cryptocurrency.priceChange);
        float priceChangePercent = Float.parseFloat(cryptocurrency.priceChangePercent);
        setPriceChange(priceChange, priceChangePercent);

    }

    protected void setPriceChange(float priceChange, float priceChangePercent) {
        String sign = priceChange < 0 ? "-" : "+";
        String format = String.format("%sUS$%f (%s%f)", sign, Math.abs(priceChange), sign, Math.abs(priceChangePercent));
        priceChangeTV.setText(format);
    }

    protected void getSparkline(Timeline timeline) {
        if (timeline == Timeline.ONEWEEK) {
            adapter.update(cryptocurrency.sparklineData7D);
            return;
        }

        String API_KEY = "8782750913501ee9f64a6169174314b7ef8c1b10";
        String[] dateBounds = PricesTimeline.getStartAndEndDate(timeline);
        String url = String.format(
                "https://api.nomics.com/v1/currencies/sparkline?key=%s&ids=%s&start=%s",
                API_KEY, cryptocurrency.symbol.toUpperCase(), dateBounds[0]
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray prices = new JSONArray(response).getJSONObject(0).getJSONArray("prices");
                        ArrayList<Float> sparkline = new ArrayList<>();
                        for (int i = 0; i < prices.length(); i++) {
                            sparkline.add(Float.parseFloat(prices.getString(i)));
                        }

                        adapter.update(sparkline);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Intent intent;
                    // Redirect to error page
                    System.out.println("Error");
                }
        );

        queue.add(request);
    }
}