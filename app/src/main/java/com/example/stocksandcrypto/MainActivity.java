package com.example.stocksandcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    boolean isScrolling = false;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrolledOutItems;
    int currentPage = 1;
    int itemsPerPage = 20;
    ArrayList<Cryptocurrency> topCryptoList = new ArrayList<>();
    RecyclerViewAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide the action bar on home page
        getSupportActionBar().hide();

        // Setup the recycler view
        recyclerView = findViewById(R.id.cryptoListRV);
        progressBar = findViewById(R.id.progressBar);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapter(topCryptoList, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrolledOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems+scrolledOutItems == totalItems)) {
                    isScrolling = false;
                    fetchTopCryptoData(currentPage, itemsPerPage);
                }
            }
        });

        // Fetch the top 20 crptos from first page to populate recycler view
        fetchTopCryptoData(currentPage, itemsPerPage);
    }

    public void fetchTopCryptoData(int page, int itemsPerPage) {
        // Don't fetch when it reaches page 10
        if (page > 10) return;


        // Show the progress bar, while data is being fetched
        progressBar.setVisibility(View.VISIBLE);

        // Fetch itemsPerPage items from the respective page
        String url = String.format(
            Locale.US,
            "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=%d&page=%d&sparkline=true",
            itemsPerPage, page
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
            Request.Method.GET, url,
            response -> {
                try {
                    JSONArray topCryptoData = new JSONArray(response);
                    for (int i = 0; i < topCryptoData.length(); i++) {
                        topCryptoList.add(Cryptocurrency.parseData(topCryptoData.getJSONObject(i).toString()));
                    }

                    // Hide the progress bar after data has been fetched
                    progressBar.setVisibility(View.INVISIBLE);

                    // Notify the adapter that more data has been added;
                    adapter.notifyDataSetChanged();

                    // Increment the page, so that new results can be fetched from next page
                    // when called next time
                    currentPage += 1;

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