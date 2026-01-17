package com.keralafarmers.agrinextai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.activities.TraceabilityActivity;
import com.keralafarmers.agrinextai.models.HarvestCard;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying harvest cards in RecyclerView
 */
public class HarvestCardAdapter extends RecyclerView.Adapter<HarvestCardAdapter.HarvestCardViewHolder> {
    
    private List<HarvestCard> harvestCards;
    private Context context;
    private TraceabilityActivity activity;
    private SimpleDateFormat dateFormat;
    
    public HarvestCardAdapter(List<HarvestCard> harvestCards, Context context) {
        this.harvestCards = harvestCards;
        this.context = context;
        this.activity = (TraceabilityActivity) context;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public HarvestCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_harvest_card, parent, false);
        return new HarvestCardViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull HarvestCardViewHolder holder, int position) {
        HarvestCard card = harvestCards.get(position);
        
        holder.tvCropName.setText(card.getCropName());
        holder.tvVariety.setText(card.getVariety());
        holder.tvFarmerName.setText("By " + card.getFarmerName());
        holder.tvLocation.setText(card.getFarmLocation());
        holder.tvQuantity.setText(String.format(Locale.getDefault(), "%.1f %s", 
            card.getQuantityHarvested(), card.getUnit()));
        holder.tvQualityGrade.setText(card.getQualityGrade());
        
        // Format harvest date
        String harvestDate = dateFormat.format(new Date(card.getHarvestDate()));
        holder.tvHarvestDate.setText("Harvested: " + harvestDate);
        
        // Show organic badge
        if (card.isOrganicCertified()) {
            holder.tvOrganicBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvOrganicBadge.setVisibility(View.GONE);
        }
        
        // Set QR code info
        holder.tvQrCode.setText("QR: " + card.getQrCode());
        
        // Card click listener
        holder.cardView.setOnClickListener(v -> {
            if (activity != null) {
                activity.onCardClicked(card);
            }
        });
        
        // QR code button - directly open detail page to show QR
        holder.btnQrCode.setOnClickListener(v -> {
            if (activity != null) {
                activity.onCardClicked(card); // Open detail page to show QR
            }
        });
        
        // Share button
        holder.btnShare.setOnClickListener(v -> {
            if (activity != null) {
                activity.shareHarvestCard(card);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return harvestCards.size();
    }
    
    /**
     * ViewHolder class for harvest card items
     */
    static class HarvestCardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvCropName, tvVariety, tvFarmerName, tvLocation;
        TextView tvQuantity, tvQualityGrade, tvHarvestDate;
        TextView tvOrganicBadge, tvQrCode;
        ImageView btnQrCode, btnShare;
        
        public HarvestCardViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.cardView);
            tvCropName = itemView.findViewById(R.id.tvCropName);
            tvVariety = itemView.findViewById(R.id.tvVariety);
            tvFarmerName = itemView.findViewById(R.id.tvFarmerName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvQualityGrade = itemView.findViewById(R.id.tvQualityGrade);
            tvHarvestDate = itemView.findViewById(R.id.tvHarvestDate);
            tvOrganicBadge = itemView.findViewById(R.id.tvOrganicBadge);
            tvQrCode = itemView.findViewById(R.id.tvQrCode);
            btnQrCode = itemView.findViewById(R.id.btnQrCode);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }
}