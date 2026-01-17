package com.keralafarmers.agrinextai.models;

/**
 * Model class for Government Schemes
 */
public class GovernmentScheme {
    private int id;
    private String name;
    private String description;
    private String eligibility;
    private String benefit;
    private String applicationProcess;
    private String contactInfo;
    private String category;
    private boolean isActive;
    
    public GovernmentScheme() {
        this.isActive = true;
    }
    
    public GovernmentScheme(String name, String description, String eligibility, String benefit) {
        this.name = name;
        this.description = description;
        this.eligibility = eligibility;
        this.benefit = benefit;
        this.isActive = true;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getEligibility() {
        return eligibility;
    }
    
    public String getBenefit() {
        return benefit;
    }
    
    public String getApplicationProcess() {
        return applicationProcess;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public String getCategory() {
        return category;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }
    
    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
    
    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "GovernmentScheme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}