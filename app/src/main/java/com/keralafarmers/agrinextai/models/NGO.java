package com.keralafarmers.agrinextai.models;

/**
 * NGO model class for representing Non-Governmental Organizations
 * that provide support to farmers
 */
public class NGO {
    
    private int id;
    private String name;
    private String description;
    private String services;
    private String location;
    private String phoneNumber;
    private String email;
    private String website;
    private String category;
    private float rating;
    private boolean isVerified;
    private String imageUrl;
    private long createdAt;
    private long updatedAt;
    
    // Constructors
    public NGO() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.rating = 0.0f;
        this.isVerified = false;
    }
    
    public NGO(String name, String description, String phoneNumber, String email) {
        this();
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getServices() {
        return services;
    }
    
    public void setServices(String services) {
        this.services = services;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public float getRating() {
        return rating;
    }
    
    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public boolean isVerified() {
        return isVerified;
    }
    
    public void setVerified(boolean verified) {
        isVerified = verified;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public String getFormattedRating() {
        return String.format("%.1f ⭐", rating);
    }
    
    public String getVerificationStatus() {
        return isVerified ? "✅ Verified" : "⚠️ Not Verified";
    }
    
    public String getContactInfo() {
        StringBuilder contact = new StringBuilder();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            contact.append("📞 ").append(phoneNumber);
        }
        if (email != null && !email.isEmpty()) {
            if (contact.length() > 0) contact.append("\n");
            contact.append("📧 ").append(email);
        }
        return contact.toString();
    }
    
    public boolean hasWebsite() {
        return website != null && !website.isEmpty() && !website.equals("N/A");
    }
    
    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isEmpty() && !phoneNumber.equals("N/A");
    }
    
    public boolean hasEmail() {
        return email != null && !email.isEmpty() && !email.equals("N/A");
    }
    
    @Override
    public String toString() {
        return "NGO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                ", verified=" + isVerified +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NGO ngo = (NGO) obj;
        return id == ngo.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}