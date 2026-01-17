package com.keralafarmers.agrinextai.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.keralafarmers.agrinextai.models.User;

/**
 * Session Manager for handling user authentication and session persistence
 */
public class SessionManager {
    
    private static final String PREF_NAME = "user_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_SESSION_TOKEN = "session_token";
    private static final String KEY_LOGIN_TIME = "login_time";
    
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    
    /**
     * Create login session for user
     * @param user User object
     */
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getFarmerName());
        editor.putString(KEY_SESSION_TOKEN, SecurityUtils.generateSessionToken());
        editor.putLong(KEY_LOGIN_TIME, System.currentTimeMillis());
        editor.commit();
    }
    
    /**
     * Check if user is logged in
     * @return True if user is logged in
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Get logged in user ID
     * @return User ID or -1 if not logged in
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }
    
    /**
     * Get logged in user email
     * @return User email or null if not logged in
     */
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }
    
    /**
     * Get logged in user name
     * @return User name or null if not logged in
     */
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }
    
    /**
     * Get current session token
     * @return Session token or null if not logged in
     */
    public String getSessionToken() {
        return pref.getString(KEY_SESSION_TOKEN, null);
    }
    
    /**
     * Get login time
     * @return Login timestamp
     */
    public long getLoginTime() {
        return pref.getLong(KEY_LOGIN_TIME, 0);
    }
    
    /**
     * Check if session has expired (24 hours)
     * @return True if session expired
     */
    public boolean isSessionExpired() {
        long loginTime = getLoginTime();
        long currentTime = System.currentTimeMillis();
        long sessionDuration = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
        
        return (currentTime - loginTime) > sessionDuration;
    }
    
    /**
     * Refresh session token
     */
    public void refreshSession() {
        if (isLoggedIn()) {
            editor.putString(KEY_SESSION_TOKEN, SecurityUtils.generateSessionToken());
            editor.putLong(KEY_LOGIN_TIME, System.currentTimeMillis());
            editor.commit();
        }
    }
    
    /**
     * Update user name in session
     * @param userName New user name
     */
    public void updateUserName(String userName) {
        if (isLoggedIn()) {
            editor.putString(KEY_USER_NAME, userName);
            editor.commit();
        }
    }
    
    /**
     * Log out user and clear session
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
    
    /**
     * Get all session data as a formatted string (for debugging)
     * @return Session data string
     */
    public String getSessionInfo() {
        if (!isLoggedIn()) {
            return "No active session";
        }
        
        return "User ID: " + getUserId() + 
               "\nEmail: " + getUserEmail() + 
               "\nName: " + getUserName() + 
               "\nLogin Time: " + new java.util.Date(getLoginTime()) +
               "\nExpired: " + isSessionExpired();
    }
    
    /**
     * Clear expired session
     * @return True if session was expired and cleared
     */
    public boolean clearExpiredSession() {
        if (isLoggedIn() && isSessionExpired()) {
            logoutUser();
            return true;
        }
        return false;
    }
}
