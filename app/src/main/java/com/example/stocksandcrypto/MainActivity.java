package com.example.stocksandcrypto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide the action bar on home page
        getSupportActionBar().hide();
        // Fetch the top 20 crptos to populate recycler view
        fetchTopCryptoData();
    }

    public void fetchTopCryptoData() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=true";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    try {
                        ArrayList<Cryptocurrency> topCrypos = new ArrayList<>();
                        JSONArray topCryptoData = new JSONArray(response);
                        for (int i = 0; i < topCryptoData.length(); i++) {
                            topCrypos.add(Cryptocurrency.parseData(topCryptoData.getJSONObject(i).toString()));
                        }

                        RecyclerView recyclerView = findViewById(R.id.cryptoListRV);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setAdapter(new RecyclerViewAdapter(topCrypos, this));
                        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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