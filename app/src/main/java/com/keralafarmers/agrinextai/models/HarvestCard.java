package com.keralafarmers.agrinextai.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Harvest Card entity for Room database
 * Stores traceability information for harvest with QR code support
 */
@Entity(tableName = "harvest_cards")
public class HarvestCard {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "card_id")
    private String cardId; // Unique QR code identifier
    
    @ColumnInfo(name = "user_id")
    private int userId;
    
    @ColumnInfo(name = "farmer_name")
    private String farmerName;
    
    @ColumnInfo(name = "farm_location")
    private String farmLocation;
    
    @ColumnInfo(name = "farm_size")
    private double farmSize; // in acres
    
    @ColumnInfo(name = "crop_name")
    private String cropName;
    
    @ColumnInfo(name = "crop_variety")
    private String cropVariety;
    
    @ColumnInfo(name = "planting_date")
    private long plantingDate;
    
    @ColumnInfo(name = "harvest_date")
    private long harvestDate;
    
    @ColumnInfo(name = "quantity_harvested")
    private double quantityHarvested; // in kg
    
    @ColumnInfo(name = "unit")
    private String unit; // kg, quintal, ton
    
    @ColumnInfo(name = "quality_grade")
    private String qualityGrade; // A, B, C
    
    @ColumnInfo(name = "is_organic")
    private boolean isOrganic;
    
    @ColumnInfo(name = "certification_number")
    private String certificationNumber; // Organic certification if applicable
    
    @ColumnInfo(name = "pesticides_used")
    private String pesticidesUsed; // JSON string of pesticides
    
    @ColumnInfo(name = "fertilizers_used")
    private String fertilizersUsed; // JSON string of fertilizers
    
    @ColumnInfo(name = "irrigation_method")
    private String irrigationMethod;
    
    @ColumnInfo(name = "water_source")
    private String waterSource;
    
    @ColumnInfo(name = "soil_type")
    private String soilType;
    
    @ColumnInfo(name = "weather_conditions")
    private String weatherConditions; // JSON string of weather data
    
    @ColumnInfo(name = "carbon_footprint")
    private double carbonFootprint; // in kg CO2
    
    @ColumnInfo(name = "transportation_method")
    private String transportationMethod;
    
    @ColumnInfo(name = "storage_conditions")
    private String storageConditions;
    
    @ColumnInfo(name = "processing_details")
    private String processingDetails;
    
    @ColumnInfo(name = "lab_test_results")
    private String labTestResults; // JSON string of test results
    
    @ColumnInfo(name = "market_destination")
    private String marketDestination;
    
    @ColumnInfo(name = "price_per_kg")
    private double pricePerKg;
    
    @ColumnInfo(name = "total_revenue")
    private double totalRevenue;
    
    @ColumnInfo(name = "production_cost")
    private double productionCost;
    
    @ColumnInfo(name = "profit_margin")
    private double profitMargin;
    
    @ColumnInfo(name = "qr_code_data")
    private String qrCodeData; // Base64 encoded QR code image
    
    @ColumnInfo(name = "blockchain_hash")
    private String blockchainHash; // For future blockchain integration
    
    @ColumnInfo(name = "verification_status")
    private String verificationStatus; // PENDING, VERIFIED, REJECTED
    
    @ColumnInfo(name = "notes")
    private String notes;
    
    @ColumnInfo(name = "created_date")
    private long createdDate;
    
    @ColumnInfo(name = "updated_date")
    private long updatedDate;
    
    @ColumnInfo(name = "is_active")
    private boolean isActive;

    // Constants
    public static final String VERIFICATION_PENDING = "PENDING";
    public static final String VERIFICATION_VERIFIED = "VERIFIED";
    public static final String VERIFICATION_REJECTED = "REJECTED";

    public static final String GRADE_A = "A";
    public static final String GRADE_B = "B";
    public static final String GRADE_C = "C";

    // Constructors
    public HarvestCard() {
        this.unit = "kg";
        this.verificationStatus = VERIFICATION_PENDING;
        this.isActive = true;
        this.createdDate = System.currentTimeMillis();
        this.updatedDate = System.currentTimeMillis();
        this.cardId = generateCardId();
    }

    @Ignore
    public HarvestCard(int userId, String farmerName, String cropName, long harvestDate) {
        this();
        this.userId = userId;
        this.farmerName = farmerName;
        this.cropName = cropName;
        this.harvestDate = harvestDate;
    }

    /**
     * Generate unique card ID
     * @return Unique card identifier
     */
    private String generateCardId() {
        return "HC" + System.currentTimeMillis() + String.valueOf((int)(Math.random() * 1000));
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    public double getFarmSize() {
        return farmSize;
    }

    public void setFarmSize(double farmSize) {
        this.farmSize = farmSize;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropVariety() {
        return cropVariety;
    }

    public void setCropVariety(String cropVariety) {
        this.cropVariety = cropVariety;
    }

    public long getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(long plantingDate) {
        this.plantingDate = plantingDate;
    }

    public long getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(long harvestDate) {
        this.harvestDate = harvestDate;
    }

    public double getQuantityHarvested() {
        return quantityHarvested;
    }

    public void setQuantityHarvested(double quantityHarvested) {
        this.quantityHarvested = quantityHarvested;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQualityGrade() {
        return qualityGrade;
    }

    public void setQualityGrade(String qualityGrade) {
        this.qualityGrade = qualityGrade;
    }

    public boolean isOrganic() {
        return isOrganic;
    }

    public void setOrganic(boolean organic) {
        isOrganic = organic;
    }
    
    // Helper methods for compatibility with TraceabilityActivity
    public String getVariety() {
        return cropVariety;
    }
    
    public void setVariety(String variety) {
        this.cropVariety = variety;
    }
    
    public String getFarmerId() {
        return "F" + userId; // Generate farmer ID from user ID
    }
    
    public void setFarmerId(String farmerId) {
        // Extract user ID from farmer ID if needed
        // For now, we'll ignore this as it's derived from userId
    }
    
    public boolean isOrganicCertified() {
        return isOrganic;
    }
    
    public void setOrganicCertified(boolean organicCertified) {
        this.isOrganic = organicCertified;
    }
    
    public String getTreatmentsApplied() {
        StringBuilder treatments = new StringBuilder();
        if (pesticidesUsed != null && !pesticidesUsed.isEmpty()) {
            treatments.append(pesticidesUsed);
        }
        if (fertilizersUsed != null && !fertilizersUsed.isEmpty()) {
            if (treatments.length() > 0) treatments.append(", ");
            treatments.append(fertilizersUsed);
        }
        return treatments.toString();
    }
    
    public void setTreatmentsApplied(String treatmentsApplied) {
        // For simplicity, store in pesticidesUsed field
        this.pesticidesUsed = treatmentsApplied;
    }
    
    public String getStorageCondition() {
        return storageConditions;
    }
    
    public void setStorageCondition(String storageCondition) {
        this.storageConditions = storageCondition;
    }
    
    public String getQrCode() {
        return cardId; // Use cardId as QR code
    }
    
    public void setQrCode(String qrCode) {
        this.cardId = qrCode;
    }

    public String getCertificationNumber() {
        return certificationNumber;
    }

    public void setCertificationNumber(String certificationNumber) {
        this.certificationNumber = certificationNumber;
    }

    public String getPesticidesUsed() {
        return pesticidesUsed;
    }

    public void setPesticidesUsed(String pesticidesUsed) {
        this.pesticidesUsed = pesticidesUsed;
    }

    public String getFertilizersUsed() {
        return fertilizersUsed;
    }

    public void setFertilizersUsed(String fertilizersUsed) {
        this.fertilizersUsed = fertilizersUsed;
    }

    public String getIrrigationMethod() {
        return irrigationMethod;
    }

    public void setIrrigationMethod(String irrigationMethod) {
        this.irrigationMethod = irrigationMethod;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public double getCarbonFootprint() {
        return carbonFootprint;
    }

    public void setCarbonFootprint(double carbonFootprint) {
        this.carbonFootprint = carbonFootprint;
    }

    public String getTransportationMethod() {
        return transportationMethod;
    }

    public void setTransportationMethod(String transportationMethod) {
        this.transportationMethod = transportationMethod;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public String getProcessingDetails() {
        return processingDetails;
    }

    public void setProcessingDetails(String processingDetails) {
        this.processingDetails = processingDetails;
    }

    public String getLabTestResults() {
        return labTestResults;
    }

    public void setLabTestResults(String labTestResults) {
        this.labTestResults = labTestResults;
    }

    public String getMarketDestination() {
        return marketDestination;
    }

    public void setMarketDestination(String marketDestination) {
        this.marketDestination = marketDestination;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(double productionCost) {
        this.productionCost = productionCost;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Calculate growing period in days
     * @return Growing period in days
     */
    public long getGrowingPeriod() {
        if (plantingDate > 0 && harvestDate > 0) {
            return (harvestDate - plantingDate) / (24 * 60 * 60 * 1000); // Convert to days
        }
        return 0;
    }

    /**
     * Get formatted harvest quantity with unit
     * @return Formatted quantity string
     */
    public String getFormattedQuantity() {
        return String.format("%.2f %s", quantityHarvested, unit);
    }

    /**
     * Get formatted total revenue
     * @return Formatted revenue string
     */
    public String getFormattedRevenue() {
        return String.format("₹%.2f", totalRevenue);
    }

    /**
     * Calculate and update profit margin
     */
    public void calculateProfitMargin() {
        if (totalRevenue > 0 && productionCost > 0) {
            profitMargin = ((totalRevenue - productionCost) / totalRevenue) * 100;
        }
    }

    /**
     * Get verification status display text
     * @return Human readable verification status
     */
    public String getVerificationStatusText() {
        switch (verificationStatus) {
            case VERIFICATION_VERIFIED:
                return "Verified";
            case VERIFICATION_REJECTED:
                return "Rejected";
            case VERIFICATION_PENDING:
            default:
                return "Pending";
        }
    }

    /**
     * Check if the harvest card is verified
     * @return true if verified
     */
    public boolean isVerified() {
        return VERIFICATION_VERIFIED.equals(verificationStatus);
    }

    @Override
    public String toString() {
        return "HarvestCard{" +
                "cardId='" + cardId + '\'' +
                ", farmerName='" + farmerName + '\'' +
                ", cropName='" + cropName + '\'' +
                ", quantityHarvested=" + quantityHarvested +
                ", verificationStatus='" + verificationStatus + '\'' +
                '}';
    }
}