package com.keralafarmers.agrinextai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.MarketPrice;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying market prices in RecyclerView
 */
public class MarketPriceAdapter extends RecyclerView.Adapter<MarketPriceAdapter.PriceViewHolder> {
    
    private List<MarketPrice> prices;
    private Context context;
    private SimpleDateFormat dateFormat;
    
    public MarketPriceAdapter(List<MarketPrice> prices, Context context) {
        this.prices = prices;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_market_price, parent, false);
        return new PriceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        MarketPrice price = prices.get(position);
        
        holder.tvCropName.setText(price.getCropName());
        holder.tvVariety.setText(price.getVariety());
        holder.tvMarketName.setText(price.getMarketName());
        holder.tvUnit.setText(context.getString(R.string.per_unit, price.getUnit()));
        
        // Format prices
        holder.tvMinPrice.setText("₹" + String.format(Locale.getDefault(), "%.0f", price.getMinPrice()));
        holder.tvMaxPrice.setText("₹" + String.format(Locale.getDefault(), "%.0f", price.getMaxPrice()));
        holder.tvModalPrice.setText("₹" + String.format(Locale.getDefault(), "%.0f", price.getModalPrice()));
        
        // Format last updated time
        String lastUpdated = dateFormat.format(new Date(price.getLastUpdated()));
        holder.tvLastUpdated.setText(context.getString(R.string.updated_on, lastUpdated));
    }
    
    @Override
    public int getItemCount() {
        return prices.size();
    }
    
    /**
     * Update prices list and notify adapter
     */
    public void updatePrices(List<MarketPrice> newPrices) {
        this.prices = newPrices;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for market price items
     */
    static class PriceViewHolder extends RecyclerView.ViewHolder {
        TextView tvCropName, tvVariety, tvMarketName, tvUnit;
        TextView tvMinPrice, tvMaxPrice, tvModalPrice;
        TextView tvLastUpdated;
        
        public PriceViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvCropName = itemView.findViewById(R.id.tvCropName);
            tvVariety = itemView.findViewById(R.id.tvVariety);
            tvMarketName = itemView.findViewById(R.id.tvMarketName);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvMinPrice = itemView.findViewById(R.id.tvMinPrice);
            tvMaxPrice = itemView.findViewById(R.id.tvMaxPrice);
            tvModalPrice = itemView.findViewById(R.id.tvModalPrice);
            tvLastUpdated = itemView.findViewById(R.id.tvLastUpdated);
        }
    }
}