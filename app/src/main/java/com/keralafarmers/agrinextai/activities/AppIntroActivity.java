package com.keralafarmers.agrinextai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.utils.LanguageManager;

/**
 * Interactive App Introduction Activity
 * Shows app features, allows language selection, and provides smooth onboarding experience
 */
public class AppIntroActivity extends AppCompatActivity {
    
    private LanguageManager languageManager;
    private Spinner spinnerLanguage;
    private MaterialButton btnContinue;
    
    // UI Components for dynamic text updates
    private TextView tvAppSubtitle;
    private TextView tvFeaturesTitle;
    private TextView tvFeature1;
    private TextView tvFeature2;
    private TextView tvFeature3;
    private TextView tvFeature4;
    private TextView tvFeature5;
    private TextView tvAboutTitle;
    private TextView tvAppDescription;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_app_intro);
        
        initializeViews();
        setupLanguageSpinner();
        setupClickListeners();
        updateTexts();
    }
    
    /**
     * Initialize all view components
     */
    private void initializeViews() {
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        btnContinue = findViewById(R.id.btnContinue);
        
        // Text views for dynamic updates
        tvAppSubtitle = findViewById(R.id.tvAppSubtitle);
        tvFeaturesTitle = findViewById(R.id.tvFeaturesTitle);
        tvFeature1 = findViewById(R.id.tvFeature1);
        tvFeature2 = findViewById(R.id.tvFeature2);
        tvFeature3 = findViewById(R.id.tvFeature3);
        tvFeature4 = findViewById(R.id.tvFeature4);
        tvFeature5 = findViewById(R.id.tvFeature5);
        tvAboutTitle = findViewById(R.id.tvAboutTitle);
        tvAppDescription = findViewById(R.id.tvAppDescription);
    }
    
    /**
     * Setup language selection spinner
     */
    private void setupLanguageSpinner() {
        // Setup spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.language_options, R.layout.spinner_item_white);
        adapter.setDropDownViewResource(R.layout.spinner_item_white);
        spinnerLanguage.setAdapter(adapter);
        
        // Set current language selection
        String currentLanguage = languageManager.getCurrentLanguage();
        String[] languageCodes = getResources().getStringArray(R.array.language_codes);
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(currentLanguage)) {
                spinnerLanguage.setSelection(i);
                break;
            }
        }
        
        // Set selection listener
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] codes = getResources().getStringArray(R.array.language_codes);
                if (position < codes.length) {
                    String selectedLanguage = codes[position];
                    
                    // Update language if different from current
                    if (!selectedLanguage.equals(languageManager.getCurrentLanguage())) {
                        languageManager.setLanguage(selectedLanguage);
                        
                        // Recreate activity to apply language change
                        recreate();
                    }
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        btnContinue.setOnClickListener(v -> {
            // Navigate to MainActivity
            Intent intent = new Intent(AppIntroActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close this activity
        });
    }
    
    /**
     * Update all text content based on current language
     */
    private void updateTexts() {
        try {
            // Update subtitle
            tvAppSubtitle.setText(getString(R.string.app_subtitle));
            
            // Update features section
            tvFeaturesTitle.setText(getString(R.string.app_features_title));
            tvFeature1.setText(getString(R.string.feature_ai_detection));
            tvFeature2.setText(getString(R.string.feature_weather_forecast));
            tvFeature3.setText(getString(R.string.feature_market_prices));
            tvFeature4.setText(getString(R.string.feature_multilingual_chat));
            tvFeature5.setText(getString(R.string.feature_ngo_support));
            
            // Update about section
            tvAboutTitle.setText(getString(R.string.app_about_title));
            tvAppDescription.setText(getString(R.string.app_description));
            
            // Update button text
            btnContinue.setText(getString(R.string.continue_to_app));
            
        } catch (Exception e) {
            // Handle any string resource errors gracefully
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Update texts when returning to activity
        updateTexts();
    }
    
    @Override
    public void onBackPressed() {
        // On intro page, back press should exit the app
        // This prevents going back to login after successful authentication
        moveTaskToBack(true);
    }
}