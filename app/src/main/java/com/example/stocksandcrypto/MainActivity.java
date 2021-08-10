package com.example.stocksandcrypto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(this::redirectToItemDetails);
    }

    protected void redirectToItemDetails(View v) {
        String url = String.format(
            "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=%s&order=market_cap_desc&per_page=100&page=1&sparkline=true",
            ((Button)v).getText()
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
            Request.Method.GET, url,
            response -> {
                Intent intent = new Intent(this, ItemDetails.class);
                intent.putExtra("itemdata", response);
                startActivity(intent);
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