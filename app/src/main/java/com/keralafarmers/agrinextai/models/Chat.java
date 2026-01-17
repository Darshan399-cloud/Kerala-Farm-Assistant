package com.keralafarmers.agrinextai.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Chat entity for Room database
 * Stores chatbot conversation history with multilingual support
 */
@Entity(tableName = "chats")
public class Chat {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @ColumnInfo(name = "user_id")
    private int userId;
    
    @ColumnInfo(name = "session_id")
    private String sessionId;
    
    @ColumnInfo(name = "message")
    private String message;
    
    @ColumnInfo(name = "response")
    private String response;
    
    @ColumnInfo(name = "message_type")
    private String messageType; // USER, BOT
    
    @ColumnInfo(name = "language")
    private String language; // en, hi, ml
    
    @ColumnInfo(name = "category")
    private String category; // CROP_CARE, WEATHER, FERTILIZER, PEST_CONTROL, etc.
    
    @ColumnInfo(name = "confidence_score")
    private double confidenceScore;
    
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    
    @ColumnInfo(name = "is_helpful")
    private Boolean isHelpful; // User feedback
    
    @ColumnInfo(name = "context")
    private String context; // Additional context for the conversation
    
    // Constants for message types
    public static final String MESSAGE_TYPE_USER = "USER";
    public static final String MESSAGE_TYPE_BOT = "BOT";
    
    // Constants for categories
    public static final String CATEGORY_CROP_CARE = "CROP_CARE";
    public static final String CATEGORY_WEATHER = "WEATHER";
    public static final String CATEGORY_FERTILIZER = "FERTILIZER";
    public static final String CATEGORY_PEST_CONTROL = "PEST_CONTROL";
    public static final String CATEGORY_SOIL = "SOIL";
    public static final String CATEGORY_IRRIGATION = "IRRIGATION";
    public static final String CATEGORY_HARVEST = "HARVEST";
    public static final String CATEGORY_MARKET = "MARKET";
    public static final String CATEGORY_GENERAL = "GENERAL";

    // Constructors
    public Chat() {
        this.timestamp = System.currentTimeMillis();
        this.confidenceScore = 1.0;
    }

    @Ignore
    public Chat(int userId, String sessionId, String message, String messageType, String language) {
        this();
        this.userId = userId;
        this.sessionId = sessionId;
        this.message = message;
        this.messageType = messageType;
        this.language = language;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsHelpful() {
        return isHelpful;
    }

    public void setIsHelpful(Boolean isHelpful) {
        this.isHelpful = isHelpful;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    
    // Additional fields for UI state
    @Ignore
    private boolean isTyping = false;
    
    // Helper methods for ChatbotActivity compatibility
    public boolean isFromUser() {
        return MESSAGE_TYPE_USER.equals(messageType);
    }
    
    public void setIsFromUser(boolean isFromUser) {
        this.messageType = isFromUser ? MESSAGE_TYPE_USER : MESSAGE_TYPE_BOT;
    }
    
    public boolean isTyping() {
        return isTyping;
    }
    
    public void setIsTyping(boolean isTyping) {
        this.isTyping = isTyping;
    }

    /**
     * Check if this is a user message
     * @return true if message is from user
     */
    public boolean isUserMessage() {
        return MESSAGE_TYPE_USER.equals(messageType);
    }

    /**
     * Check if this is a bot message
     * @return true if message is from bot
     */
    public boolean isBotMessage() {
        return MESSAGE_TYPE_BOT.equals(messageType);
    }

    /**
     * Get formatted timestamp for display
     * @return Formatted timestamp string
     */
    public String getFormattedTimestamp() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }

    /**
     * Get confidence level description
     * @return String description of confidence level
     */
    public String getConfidenceLevel() {
        if (confidenceScore >= 0.8) {
            return "High";
        } else if (confidenceScore >= 0.6) {
            return "Medium";
        } else {
            return "Low";
        }
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", messageType='" + messageType + '\'' +
                ", message='" + message + '\'' +
                ", language='" + language + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}