package com.example.stocksandcrypto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public ArrayList<Cryptocurrency> data;
    public Context context;

    public RecyclerViewAdapter(ArrayList<Cryptocurrency> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_row, parent, false);
        ViewHolder holder = new ViewHolder(rowItem, context);
        rowItem.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Cryptocurrency cryptocurrency = this.data.get(position);
                Intent intent = new Intent(context, ItemDetails.class);
                intent.putExtra("itemdata", cryptocurrency);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Cryptocurrency cryptocurrency = this.data.get(position);
        TextView itemSymbol = holder.linearLayout.findViewById(R.id.itemSymbol);
        TextView itemName = holder.linearLayout.findViewById(R.id.itemName);
        SparkView sparkline = holder.linearLayout.findViewById(R.id.sparkline);
        TickerView tickerView = holder.linearLayout.findViewById(R.id.tickerView);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());

        itemName.setText(cryptocurrency.name);
        itemSymbol.setText(cryptocurrency.symbol.toUpperCase());
        tickerView.setText("$" + cryptocurrency.currentPrice);
        SparklineAdapter adapter = new SparklineAdapter(cryptocurrency.sparklineData7D);
        sparkline.setAdapter(adapter);

        int color = Float.parseFloat(cryptocurrency.priceChange) < 0 ?
                ContextCompat.getColor(context, R.color.pricered) :
                ContextCompat.getColor(context, R.color.pricegreen);

        sparkline.setLineColor(color);
        tickerView.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            this.linearLayout = view.findViewById(R.id.linearLayoutRow);
            this.context = context;
        }
    }
}