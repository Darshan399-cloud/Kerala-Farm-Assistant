package com.keralafarmers.agrinextai.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.GovernmentScheme;
import java.util.List;

/**
 * Adapter for displaying government schemes in RecyclerView
 */
public class GovernmentSchemeAdapter extends RecyclerView.Adapter<GovernmentSchemeAdapter.SchemeViewHolder> {
    
    private List<GovernmentScheme> schemeList;
    private Context context;
    
    public GovernmentSchemeAdapter(List<GovernmentScheme> schemeList, Context context) {
        this.schemeList = schemeList;
        this.context = context;
    }
    
    @NonNull
    @Override
    public SchemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_government_scheme, parent, false);
        return new SchemeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SchemeViewHolder holder, int position) {
        GovernmentScheme scheme = schemeList.get(position);
        
        holder.tvSchemeName.setText(scheme.getName());
        holder.tvSchemeDescription.setText(scheme.getDescription());
        holder.tvSchemeCategory.setText(scheme.getCategory());
        holder.tvSchemeBenefit.setText("💰 " + scheme.getBenefit());
        holder.tvSchemeEligibility.setText("✅ " + scheme.getEligibility());
        
        // Set contact info if available
        if (scheme.getContactInfo() != null && !scheme.getContactInfo().isEmpty()) {
            holder.tvContactInfo.setText("📞 " + scheme.getContactInfo());
            holder.tvContactInfo.setVisibility(View.VISIBLE);
        } else {
            holder.tvContactInfo.setVisibility(View.GONE);
        }
        
        // Set application process if available
        if (scheme.getApplicationProcess() != null && !scheme.getApplicationProcess().isEmpty()) {
            holder.tvApplicationProcess.setText("📋 " + scheme.getApplicationProcess());
            holder.tvApplicationProcess.setVisibility(View.VISIBLE);
        } else {
            holder.tvApplicationProcess.setVisibility(View.GONE);
        }
        
        // Card click listener for more details
        holder.cardView.setOnClickListener(v -> {
            showSchemeDetails(scheme);
        });
    }
    
    @Override
    public int getItemCount() {
        return schemeList.size();
    }
    
    /**
     * Show scheme details in a dialog or new activity
     */
    private void showSchemeDetails(GovernmentScheme scheme) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("📋 " + scheme.getName())
                .setMessage("📖 Description: " + scheme.getDescription() + "\n\n" +
                           "✅ Eligibility: " + scheme.getEligibility() + "\n\n" +
                           "💰 Benefit: " + scheme.getBenefit() + "\n\n" +
                           "📋 Application: " + scheme.getApplicationProcess() + "\n\n" +
                           "📞 Contact: " + scheme.getContactInfo())
                .setPositiveButton("Apply Now", (dialog, which) -> {
                    // Open application process (could be a website or app)
                    // For now, we'll show contact info
                    openContact(scheme);
                })
                .setNegativeButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }
    
    /**
     * Open contact information
     */
    private void openContact(GovernmentScheme scheme) {
        String contactInfo = scheme.getContactInfo();
        if (contactInfo != null && contactInfo.contains("http")) {
            // If contact info contains a website, open it
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contactInfo));
            if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(browserIntent);
            }
        } else if (contactInfo != null && contactInfo.contains("toll-free") || contactInfo.contains("phone")) {
            // If contact info contains phone number, show dialer option
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle("Contact Information")
                    .setMessage(contactInfo)
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
    
    /**
     * ViewHolder class for scheme items
     */
    static class SchemeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvSchemeName, tvSchemeDescription, tvSchemeCategory;
        TextView tvSchemeBenefit, tvSchemeEligibility, tvContactInfo, tvApplicationProcess;
        
        public SchemeViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.cardView);
            tvSchemeName = itemView.findViewById(R.id.tvSchemeName);
            tvSchemeDescription = itemView.findViewById(R.id.tvSchemeDescription);
            tvSchemeCategory = itemView.findViewById(R.id.tvSchemeCategory);
            tvSchemeBenefit = itemView.findViewById(R.id.tvSchemeBenefit);
            tvSchemeEligibility = itemView.findViewById(R.id.tvSchemeEligibility);
            tvContactInfo = itemView.findViewById(R.id.tvContactInfo);
            tvApplicationProcess = itemView.findViewById(R.id.tvApplicationProcess);
        }
    }
}