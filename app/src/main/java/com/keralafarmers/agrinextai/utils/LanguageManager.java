package com.keralafarmers.agrinextai.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Language Manager for Kerala Farm Assistant App
 * Supports English, Hindi, and Malayalam languages
 */
public class LanguageManager {
    
    private static final String PREF_LANGUAGE = "app_language";
    private static final String PREF_NAME = "language_prefs";
    
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_HINDI = "hi";
    public static final String LANGUAGE_MALAYALAM = "ml";
    
    private Context context;
    private SharedPreferences prefs;
    
    public LanguageManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * Set the app language
     * @param languageCode Language code (en, hi, ml)
     */
    public void setLanguage(String languageCode) {
        prefs.edit().putString(PREF_LANGUAGE, languageCode).apply();
        updateLocale(languageCode);
    }
    
    /**
     * Get the current app language
     * @return Current language code
     */
    public String getCurrentLanguage() {
        return prefs.getString(PREF_LANGUAGE, LANGUAGE_ENGLISH);
    }
    
    /**
     * Update the locale for the given language code
     * @param languageCode Language code to set
     */
    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        
        context.getResources().updateConfiguration(config, resources.getDisplayMetrics());
    }
    
    /**
     * Apply the saved language on app start
     */
    public void applySavedLanguage() {
        String savedLanguage = getCurrentLanguage();
        updateLocale(savedLanguage);
    }
    
    /**
     * Get language display name in the respective language
     * @param languageCode Language code
     * @return Display name
     */
    public String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case LANGUAGE_ENGLISH:
                return "English";
            case LANGUAGE_HINDI:
                return "हिंदी";
            case LANGUAGE_MALAYALAM:
                return "മലയാളം";
            default:
                return "English";
        }
    }
    
    /**
     * Check if the given language code is supported
     * @param languageCode Language code to check
     * @return True if supported
     */
    public boolean isSupportedLanguage(String languageCode) {
        return LANGUAGE_ENGLISH.equals(languageCode) || 
               LANGUAGE_HINDI.equals(languageCode) || 
               LANGUAGE_MALAYALAM.equals(languageCode);
    }
    
    /**
     * Restart activity to apply language change
     * @param activity Activity to restart
     */
    public void restartActivity(Activity activity) {
        activity.recreate();
    }
    
    /**
     * Get all supported languages
     * @return Array of supported language codes
     */
    public String[] getSupportedLanguages() {
        return new String[]{LANGUAGE_ENGLISH, LANGUAGE_HINDI, LANGUAGE_MALAYALAM};
    }
    
    /**
     * Get all supported language display names
     * @return Array of display names
     */
    public String[] getSupportedLanguageNames() {
        return new String[]{"English", "हिंदी", "മലയാളം"};
    }
}