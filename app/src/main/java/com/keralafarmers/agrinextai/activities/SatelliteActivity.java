package com.keralafarmers.agrinextai.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.utils.LanguageManager;

/**
 * Activity for Satellite View - displays farm area layout and land measurement
 */
public class SatelliteActivity extends AppCompatActivity {
    
    private LanguageManager languageManager;
    private CardView cardAreaMeasurement, cardSoilHealth, cardCropMonitoring, cardWeatherData;
    private TextView tvTotalArea, tvCultivableArea, tvSoilType, tvMoistureLevel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_satellite);
        
        setupToolbar();
        initializeViews();
        setupClickListeners();
        loadSampleData();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.satellite_view));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        cardAreaMeasurement = findViewById(R.id.cardAreaMeasurement);
        cardSoilHealth = findViewById(R.id.cardSoilHealth);
        cardCropMonitoring = findViewById(R.id.cardCropMonitoring);
        cardWeatherData = findViewById(R.id.cardWeatherData);
        
        tvTotalArea = findViewById(R.id.tvTotalArea);
        tvCultivableArea = findViewById(R.id.tvCultivableArea);
        tvSoilType = findViewById(R.id.tvSoilType);
        tvMoistureLevel = findViewById(R.id.tvMoistureLevel);
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        cardAreaMeasurement.setOnClickListener(v -> {
            Toast.makeText(this, "Area measurement feature coming soon!", Toast.LENGTH_SHORT).show();
            // TODO: Open satellite map for area measurement
        });
        
        cardSoilHealth.setOnClickListener(v -> {
            Toast.makeText(this, "Soil health analysis coming soon!", Toast.LENGTH_SHORT).show();
            // TODO: Show soil health analysis
        });
        
        cardCropMonitoring.setOnClickListener(v -> {
            Toast.makeText(this, "Crop monitoring feature coming soon!", Toast.LENGTH_SHORT).show();
            // TODO: Show crop monitoring data
        });
        
        cardWeatherData.setOnClickListener(v -> {
            Toast.makeText(this, "Weather data visualization coming soon!", Toast.LENGTH_SHORT).show();
            // TODO: Show weather overlay on satellite view
        });
    }
    
    /**
     * Load sample farm data
     */
    private void loadSampleData() {
        // Sample data for demonstration
        tvTotalArea.setText("2.5 Acres");
        tvCultivableArea.setText("2.1 Acres");
        tvSoilType.setText("Laterite Soil");
        tvMoistureLevel.setText("65% (Good)");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}