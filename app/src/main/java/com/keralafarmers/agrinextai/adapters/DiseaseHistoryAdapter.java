package com.keralafarmers.agrinextai.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.PlantDisease;
import com.keralafarmers.agrinextai.utils.LanguageManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying disease detection history in RecyclerView
 */
public class DiseaseHistoryAdapter extends RecyclerView.Adapter<DiseaseHistoryAdapter.DiseaseHistoryViewHolder> {
    
    private Context context;
    private List<PlantDisease> diseaseList;
    private OnDiseaseClickListener clickListener;
    private SimpleDateFormat dateFormat;
    private LanguageManager languageManager;
    
    public interface OnDiseaseClickListener {
        void onDiseaseClick(PlantDisease plantDisease);
    }
    
    public DiseaseHistoryAdapter(Context context, List<PlantDisease> diseaseList, OnDiseaseClickListener clickListener) {
        this.context = context;
        this.diseaseList = diseaseList;
        this.clickListener = clickListener;
        this.dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        this.languageManager = new LanguageManager(context);
    }
    
    @NonNull
    @Override
    public DiseaseHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disease_history, parent, false);
        return new DiseaseHistoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DiseaseHistoryViewHolder holder, int position) {
        PlantDisease plantDisease = diseaseList.get(position);
        String currentLanguage = languageManager.getCurrentLanguage();
        
        // Set disease name
        holder.tvDiseaseName.setText(plantDisease.getLocalizedDiseaseName(currentLanguage));
        
        // Set crop type
        if (plantDisease.getCropType() != null && !plantDisease.getCropType().isEmpty()) {
            holder.tvCropType.setText(plantDisease.getCropType());
            holder.tvCropType.setVisibility(View.VISIBLE);
        } else {
            holder.tvCropType.setVisibility(View.GONE);
        }
        
        // Set detection date
        Date detectionDate = new Date(plantDisease.getDetectionDate());
        holder.tvDetectionDate.setText(dateFormat.format(detectionDate));
        
        // Set confidence level
        int confidence = (int) (plantDisease.getConfidenceLevel() * 100);
        holder.tvConfidence.setText(confidence + "%");
        
        // Set confidence color based on level
        if (confidence >= 80) {
            holder.tvConfidence.setTextColor(context.getResources().getColor(R.color.success));
        } else if (confidence >= 60) {
            holder.tvConfidence.setTextColor(context.getResources().getColor(R.color.warning));
        } else {
            holder.tvConfidence.setTextColor(context.getResources().getColor(R.color.error));
        }
        
        // Load plant image if available
        if (plantDisease.getImagePath() != null && !plantDisease.getImagePath().isEmpty()) {
            File imageFile = new File(plantDisease.getImagePath());
            if (imageFile.exists()) {
                holder.ivPlantImage.setImageBitmap(BitmapFactory.decodeFile(plantDisease.getImagePath()));
                holder.ivPlantImage.setVisibility(View.VISIBLE);
            } else {
                holder.ivPlantImage.setVisibility(View.GONE);
            }
        } else {
            holder.ivPlantImage.setVisibility(View.GONE);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onDiseaseClick(plantDisease);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return diseaseList != null ? diseaseList.size() : 0;
    }
    
    /**
     * Update the disease list
     * @param newDiseaseList New list of diseases
     */
    public void updateDiseaseList(List<PlantDisease> newDiseaseList) {
        this.diseaseList = newDiseaseList;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for disease history items
     */
    static class DiseaseHistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlantImage;
        TextView tvDiseaseName, tvCropType, tvDetectionDate, tvConfidence;
        
        public DiseaseHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlantImage = itemView.findViewById(R.id.ivPlantImage);
            tvDiseaseName = itemView.findViewById(R.id.tvDiseaseName);
            tvCropType = itemView.findViewById(R.id.tvCropType);
            tvDetectionDate = itemView.findViewById(R.id.tvDetectionDate);
            tvConfidence = itemView.findViewById(R.id.tvConfidence);
        }
    }
}