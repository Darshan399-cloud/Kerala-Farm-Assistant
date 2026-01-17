package com.keralafarmers.agrinextai.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Plant Disease model for storing disease detection results and treatment recommendations
 */
@Entity(tableName = "plant_diseases")
public class PlantDisease {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String imagePath;
    private String diseaseName;
    private String diseaseNameHindi;
    private String diseaseNameMalayalam;
    private String description;
    private String descriptionHindi;
    private String descriptionMalayalam;
    private String symptoms;
    private String symptomsHindi;
    private String symptomsMalayalam;
    private String treatment;
    private String treatmentHindi;
    private String treatmentMalayalam;
    private String fertilizer;
    private String fertilizerHindi;
    private String fertilizerMalayalam;
    private String waterRequirement;
    private String waterRequirementHindi;
    private String waterRequirementMalayalam;
    private String applicationMethod;
    private String applicationMethodHindi;
    private String applicationMethodMalayalam;
    private String prevention;
    private String preventionHindi;
    private String preventionMalayalam;
    private double confidenceLevel; // 0.0 to 1.0
    private String cropType;
    private long detectionDate;
    private int userId;
    
    // Constructor
    public PlantDisease() {
        this.detectionDate = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String getDiseaseName() {
        return diseaseName;
    }
    
    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }
    
    public String getDiseaseNameHindi() {
        return diseaseNameHindi;
    }
    
    public void setDiseaseNameHindi(String diseaseNameHindi) {
        this.diseaseNameHindi = diseaseNameHindi;
    }
    
    public String getDiseaseNameMalayalam() {
        return diseaseNameMalayalam;
    }
    
    public void setDiseaseNameMalayalam(String diseaseNameMalayalam) {
        this.diseaseNameMalayalam = diseaseNameMalayalam;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescriptionHindi() {
        return descriptionHindi;
    }
    
    public void setDescriptionHindi(String descriptionHindi) {
        this.descriptionHindi = descriptionHindi;
    }
    
    public String getDescriptionMalayalam() {
        return descriptionMalayalam;
    }
    
    public void setDescriptionMalayalam(String descriptionMalayalam) {
        this.descriptionMalayalam = descriptionMalayalam;
    }
    
    public String getSymptoms() {
        return symptoms;
    }
    
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
    
    public String getSymptomsHindi() {
        return symptomsHindi;
    }
    
    public void setSymptomsHindi(String symptomsHindi) {
        this.symptomsHindi = symptomsHindi;
    }
    
    public String getSymptomsMalayalam() {
        return symptomsMalayalam;
    }
    
    public void setSymptomsMalayalam(String symptomsMalayalam) {
        this.symptomsMalayalam = symptomsMalayalam;
    }
    
    public String getTreatment() {
        return treatment;
    }
    
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }
    
    public String getTreatmentHindi() {
        return treatmentHindi;
    }
    
    public void setTreatmentHindi(String treatmentHindi) {
        this.treatmentHindi = treatmentHindi;
    }
    
    public String getTreatmentMalayalam() {
        return treatmentMalayalam;
    }
    
    public void setTreatmentMalayalam(String treatmentMalayalam) {
        this.treatmentMalayalam = treatmentMalayalam;
    }
    
    public String getFertilizer() {
        return fertilizer;
    }
    
    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }
    
    public String getFertilizerHindi() {
        return fertilizerHindi;
    }
    
    public void setFertilizerHindi(String fertilizerHindi) {
        this.fertilizerHindi = fertilizerHindi;
    }
    
    public String getFertilizerMalayalam() {
        return fertilizerMalayalam;
    }
    
    public void setFertilizerMalayalam(String fertilizerMalayalam) {
        this.fertilizerMalayalam = fertilizerMalayalam;
    }
    
    public String getWaterRequirement() {
        return waterRequirement;
    }
    
    public void setWaterRequirement(String waterRequirement) {
        this.waterRequirement = waterRequirement;
    }
    
    public String getWaterRequirementHindi() {
        return waterRequirementHindi;
    }
    
    public void setWaterRequirementHindi(String waterRequirementHindi) {
        this.waterRequirementHindi = waterRequirementHindi;
    }
    
    public String getWaterRequirementMalayalam() {
        return waterRequirementMalayalam;
    }
    
    public void setWaterRequirementMalayalam(String waterRequirementMalayalam) {
        this.waterRequirementMalayalam = waterRequirementMalayalam;
    }
    
    public String getApplicationMethod() {
        return applicationMethod;
    }
    
    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }
    
    public String getApplicationMethodHindi() {
        return applicationMethodHindi;
    }
    
    public void setApplicationMethodHindi(String applicationMethodHindi) {
        this.applicationMethodHindi = applicationMethodHindi;
    }
    
    public String getApplicationMethodMalayalam() {
        return applicationMethodMalayalam;
    }
    
    public void setApplicationMethodMalayalam(String applicationMethodMalayalam) {
        this.applicationMethodMalayalam = applicationMethodMalayalam;
    }
    
    public String getPrevention() {
        return prevention;
    }
    
    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }
    
    public String getPreventionHindi() {
        return preventionHindi;
    }
    
    public void setPreventionHindi(String preventionHindi) {
        this.preventionHindi = preventionHindi;
    }
    
    public String getPreventionMalayalam() {
        return preventionMalayalam;
    }
    
    public void setPreventionMalayalam(String preventionMalayalam) {
        this.preventionMalayalam = preventionMalayalam;
    }
    
    public double getConfidenceLevel() {
        return confidenceLevel;
    }
    
    public void setConfidenceLevel(double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    
    public String getCropType() {
        return cropType;
    }
    
    public void setCropType(String cropType) {
        this.cropType = cropType;
    }
    
    public long getDetectionDate() {
        return detectionDate;
    }
    
    public void setDetectionDate(long detectionDate) {
        this.detectionDate = detectionDate;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Get localized disease name based on language
     * @param language Language code (en, hi, ml)
     * @return Localized disease name
     */
    public String getLocalizedDiseaseName(String language) {
        switch (language) {
            case "hi":
                return diseaseNameHindi != null ? diseaseNameHindi : diseaseName;
            case "ml":
                return diseaseNameMalayalam != null ? diseaseNameMalayalam : diseaseName;
            default:
                return diseaseName;
        }
    }
    
    /**
     * Get localized description based on language
     * @param language Language code (en, hi, ml)
     * @return Localized description
     */
    public String getLocalizedDescription(String language) {
        switch (language) {
            case "hi":
                return descriptionHindi != null ? descriptionHindi : description;
            case "ml":
                return descriptionMalayalam != null ? descriptionMalayalam : description;
            default:
                return description;
        }
    }
    
    /**
     * Get localized treatment based on language
     * @param language Language code (en, hi, ml)
     * @return Localized treatment
     */
    public String getLocalizedTreatment(String language) {
        switch (language) {
            case "hi":
                return treatmentHindi != null ? treatmentHindi : treatment;
            case "ml":
                return treatmentMalayalam != null ? treatmentMalayalam : treatment;
            default:
                return treatment;
        }
    }
}