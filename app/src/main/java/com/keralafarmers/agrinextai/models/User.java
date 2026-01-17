package com.keralafarmers.agrinextai.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * User model class for farmer authentication and profile management
 */
@Entity(tableName = "users")
public class User {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @NonNull
    private String email;
    
    @NonNull
    private String passwordHash;
    
    @NonNull
    private String farmerName;
    
    @NonNull
    private String phoneNumber;
    
    private String farmLocation;
    
    private double farmSize; // in acres
    
    private String profileImage;
    
    private long createdAt;
    
    private long lastLoginAt;
    
    private boolean isActive;
    
    private String preferredLanguage;
    
    // Constructor
    public User() {
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
        this.preferredLanguage = "en";
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @NonNull
    public String getEmail() {
        return email;
    }
    
    public void setEmail(@NonNull String email) {
        this.email = email;
    }
    
    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(@NonNull String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    @NonNull
    public String getFarmerName() {
        return farmerName;
    }
    
    public void setFarmerName(@NonNull String farmerName) {
        this.farmerName = farmerName;
    }
    
    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getPreferredLanguage() {
        return preferredLanguage;
    }
    
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", farmerName='" + farmerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", farmLocation='" + farmLocation + '\'' +
                ", farmSize=" + farmSize +
                ", isActive=" + isActive +
                ", preferredLanguage='" + preferredLanguage + '\'' +
                '}';
    }
}