package com.example.stocksandcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONException;

public class ItemDetails extends AppCompatActivity {
    TextView itemname, priceChangeTV, timeline;
    TickerView tickerView;
    SparkView sparkline;
    Cryptocurrency cryptocurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        sparkline = findViewById(R.id.sparkline);
        itemname = findViewById(R.id.itemname);
        priceChangeTV = findViewById(R.id.priceChange);
        timeline = findViewById(R.id.timeline);

        tickerView = findViewById(R.id.tickerView);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());

        String itemdata = getIntent().getStringExtra("itemdata");
        try {
            cryptocurrency = Cryptocurrency.parseData(itemdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setInitialData();

        SparklineAdapter adapter = new SparklineAdapter(cryptocurrency.sparklineData);
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
}