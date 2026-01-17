package com.keralafarmers.agrinextai.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Market Price entity for Room database
 * Stores real-time crop price information for Kerala markets
 */
@Entity(tableName = "market_prices")
public class MarketPrice {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "crop_name")
    private String cropName;
    
    @ColumnInfo(name = "crop_name_hi")
    private String cropNameHi;
    
    @ColumnInfo(name = "crop_name_ml")
    private String cropNameMl;
    
    @ColumnInfo(name = "variety")
    private String variety;
    
    @ColumnInfo(name = "market_name")
    private String marketName;
    
    @ColumnInfo(name = "district")
    private String district;
    
    @ColumnInfo(name = "state")
    private String state;
    
    @ColumnInfo(name = "price_per_kg")
    private double pricePerKg;
    
    @ColumnInfo(name = "currency")
    private String currency;
    
    @ColumnInfo(name = "unit")
    private String unit; // kg, quintal, etc.
    
    @ColumnInfo(name = "price_date")
    private long priceDate;
    
    @ColumnInfo(name = "last_updated")
    private long lastUpdated;
    
    @ColumnInfo(name = "trend")
    private String trend; // UP, DOWN, STABLE
    
    @ColumnInfo(name = "previous_price")
    private double previousPrice;
    
    @ColumnInfo(name = "change_percentage")
    private double changePercentage;
    
    @ColumnInfo(name = "min_price")
    private double minPrice;
    
    @ColumnInfo(name = "max_price")
    private double maxPrice;
    
    @ColumnInfo(name = "quality_grade")
    private String qualityGrade; // A, B, C grade
    
    @ColumnInfo(name = "is_organic")
    private boolean isOrganic;
    
    @ColumnInfo(name = "source")
    private String source; // API source or manual entry
    
    @ColumnInfo(name = "is_active")
    private boolean isActive;

    // Constructors
    public MarketPrice() {
        this.currency = "INR";
        this.unit = "kg";
        this.state = "Kerala";
        this.isActive = true;
        this.lastUpdated = System.currentTimeMillis();
    }

    @Ignore
    public MarketPrice(String cropName, String marketName, String district, 
                      double pricePerKg, String trend) {
        this();
        this.cropName = cropName;
        this.marketName = marketName;
        this.district = district;
        this.pricePerKg = pricePerKg;
        this.trend = trend;
        this.priceDate = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropNameHi() {
        return cropNameHi;
    }

    public void setCropNameHi(String cropNameHi) {
        this.cropNameHi = cropNameHi;
    }

    public String getCropNameMl() {
        return cropNameMl;
    }

    public void setCropNameMl(String cropNameMl) {
        this.cropNameMl = cropNameMl;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(long priceDate) {
        this.priceDate = priceDate;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    
    // Helper methods for compatibility
    public double getModalPrice() {
        return pricePerKg; // Modal price is the main price in this model
    }
    
    public void setModalPrice(double modalPrice) {
        this.pricePerKg = modalPrice; // Set main price as modal price
    }

    /**
     * Get formatted price with currency
     * @return Formatted price string
     */
    public String getFormattedPrice() {
        return String.format("₹%.2f/%s", pricePerKg, unit);
    }

    /**
     * Get price trend indicator
     * @return Unicode arrow for trend
     */
    public String getTrendIndicator() {
        if (trend == null) return "";
        switch (trend.toUpperCase()) {
            case "UP":
                return "↗";
            case "DOWN":
                return "↘";
            case "STABLE":
                return "→";
            default:
                return "";
        }
    }

    /**
     * Calculate price change from previous price
     */
    public void calculatePriceChange() {
        if (previousPrice > 0) {
            changePercentage = ((pricePerKg - previousPrice) / previousPrice) * 100;
            
            if (changePercentage > 1) {
                trend = "UP";
            } else if (changePercentage < -1) {
                trend = "DOWN";
            } else {
                trend = "STABLE";
            }
        }
    }

    @Override
    public String toString() {
        return "MarketPrice{" +
                "cropName='" + cropName + '\'' +
                ", marketName='" + marketName + '\'' +
                ", pricePerKg=" + pricePerKg +
                ", trend='" + trend + '\'' +
                '}';
    }
}