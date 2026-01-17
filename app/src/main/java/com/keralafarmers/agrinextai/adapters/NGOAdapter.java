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
import com.keralafarmers.agrinextai.models.NGO;
import java.util.List;

/**
 * Adapter for displaying NGO list in RecyclerView
 */
public class NGOAdapter extends RecyclerView.Adapter<NGOAdapter.NGOViewHolder> {
    
    private List<NGO> ngoList;
    private Context context;
    private OnNGOClickListener listener;
    
    public interface OnNGOClickListener {
        void onNGOClick(NGO ngo);
        void onCallClick(NGO ngo);
        void onEmailClick(NGO ngo);
        void onWebsiteClick(NGO ngo);
    }
    
    public NGOAdapter(List<NGO> ngoList, Context context, OnNGOClickListener listener) {
        this.ngoList = ngoList;
        this.context = context;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public NGOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ngo, parent, false);
        return new NGOViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NGOViewHolder holder, int position) {
        NGO ngo = ngoList.get(position);
        
        // Set NGO information
        holder.tvNgoName.setText(ngo.getName());
        holder.tvNgoDescription.setText(ngo.getDescription());
        holder.tvNgoLocation.setText(context.getString(R.string.location_emoji_label, ngo.getLocation()));
        holder.tvNgoServices.setText(context.getString(R.string.services_emoji_label, ngo.getServices()));
        holder.tvNgoCategory.setText(ngo.getCategory());
        holder.tvNgoRating.setText(ngo.getFormattedRating());
        
        // Show verification status
        if (ngo.isVerified()) {
            holder.ivVerified.setVisibility(View.VISIBLE);
            holder.ivVerified.setImageResource(R.drawable.ic_verified);
        } else {
            holder.ivVerified.setVisibility(View.GONE);
        }
        
        // Set contact information
        holder.tvContactInfo.setText(ngo.getContactInfo());
        
        // Card click listener
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNGOClick(ngo);
            }
        });
        
        // Call button
        holder.btnCall.setOnClickListener(v -> {
            if (listener != null && ngo.hasPhoneNumber()) {
                listener.onCallClick(ngo);
            }
        });
        
        // Email button
        holder.btnEmail.setOnClickListener(v -> {
            if (listener != null && ngo.hasEmail()) {
                listener.onEmailClick(ngo);
            }
        });
        
        // Website button
        holder.btnWebsite.setOnClickListener(v -> {
            if (listener != null && ngo.hasWebsite()) {
                listener.onWebsiteClick(ngo);
            }
        });
        
        // Hide buttons if contact info not available
        holder.btnCall.setVisibility(ngo.hasPhoneNumber() ? View.VISIBLE : View.GONE);
        holder.btnEmail.setVisibility(ngo.hasEmail() ? View.VISIBLE : View.GONE);
        holder.btnWebsite.setVisibility(ngo.hasWebsite() ? View.VISIBLE : View.GONE);
        
        // Set category-specific styling
        setCategoryIcon(holder, ngo.getCategory());
    }
    
    private void setCategoryIcon(NGOViewHolder holder, String category) {
        int iconRes = R.drawable.ic_ngo_support; // Default icon
        
        if (category != null) {
            switch (category.toLowerCase()) {
                case "agricultural support":
                case "agriculture":
                    iconRes = R.drawable.ic_agriculture;
                    break;
                case "organic farming":
                    iconRes = R.drawable.ic_organic;
                    break;
                case "financial support":
                case "finance":
                    iconRes = R.drawable.ic_finance;
                    break;
                case "spice farming":
                    iconRes = R.drawable.ic_spice;
                    break;
                case "women empowerment":
                    iconRes = R.drawable.ic_women;
                    break;
                case "climate support":
                    iconRes = R.drawable.ic_climate;
                    break;
                default:
                    iconRes = R.drawable.ic_ngo_support;
            }
        }
        
        holder.ivCategory.setImageResource(iconRes);
    }
    
    @Override
    public int getItemCount() {
        return ngoList.size();
    }
    
    static class NGOViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivCategory, ivVerified;
        TextView tvNgoName, tvNgoDescription, tvNgoLocation, tvNgoServices;
        TextView tvNgoCategory, tvNgoRating, tvContactInfo;
        ImageView btnCall, btnEmail, btnWebsite;
        
        public NGOViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.cardView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            ivVerified = itemView.findViewById(R.id.ivVerified);
            tvNgoName = itemView.findViewById(R.id.tvNgoName);
            tvNgoDescription = itemView.findViewById(R.id.tvNgoDescription);
            tvNgoLocation = itemView.findViewById(R.id.tvNgoLocation);
            tvNgoServices = itemView.findViewById(R.id.tvNgoServices);
            tvNgoCategory = itemView.findViewById(R.id.tvNgoCategory);
            tvNgoRating = itemView.findViewById(R.id.tvNgoRating);
            tvContactInfo = itemView.findViewById(R.id.tvContactInfo);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnEmail = itemView.findViewById(R.id.btnEmail);
            btnWebsite = itemView.findViewById(R.id.btnWebsite);
        }
    }
}