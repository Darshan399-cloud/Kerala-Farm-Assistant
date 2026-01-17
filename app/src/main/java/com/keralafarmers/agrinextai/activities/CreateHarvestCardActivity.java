package com.keralafarmers.agrinextai.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.HarvestCard;
import com.keralafarmers.agrinextai.services.TraceabilityService;
import com.keralafarmers.agrinextai.utils.LanguageManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for creating new harvest cards
 * Allows farmers to input harvest information and generate QR codes
 */
public class CreateHarvestCardActivity extends AppCompatActivity {
    
    private TextInputEditText etCropName, etVariety, etFarmerName, etFarmLocation;
    private TextInputEditText etQuantity, etUnit, etQualityGrade;
    private TextInputEditText etPlantingDate, etHarvestDate;
    private MaterialButton btnSave, btnCancel;
    private LanguageManager languageManager;
    private TraceabilityService traceabilityService;
    private SimpleDateFormat dateFormat;
    private long plantingDateMillis = 0;
    private long harvestDateMillis = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_create_harvest_card);
        
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        traceabilityService = new TraceabilityService(this);
        
        setupToolbar();
        initializeViews();
        setupClickListeners();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.create_harvest_card));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        etCropName = findViewById(R.id.etCropName);
        etVariety = findViewById(R.id.etVariety);
        etFarmerName = findViewById(R.id.etFarmerName);
        etFarmLocation = findViewById(R.id.etFarmLocation);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etQualityGrade = findViewById(R.id.etQualityGrade);
        etPlantingDate = findViewById(R.id.etPlantingDate);
        etHarvestDate = findViewById(R.id.etHarvestDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        
        // Set default values
        etUnit.setText("kg");
        etQualityGrade.setText("Grade A");
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveHarvestCard());
        btnCancel.setOnClickListener(v -> finish());
        
        // Date picker for planting date
        etPlantingDate.setOnClickListener(v -> showDatePicker(true));
        
        // Date picker for harvest date
        etHarvestDate.setOnClickListener(v -> showDatePicker(false));
    }
    
    /**
     * Show date picker dialog
     * @param isPlantingDate true for planting date, false for harvest date
     */
    private void showDatePicker(boolean isPlantingDate) {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                long selectedDate = calendar.getTimeInMillis();
                String formattedDate = dateFormat.format(new Date(selectedDate));
                
                if (isPlantingDate) {
                    plantingDateMillis = selectedDate;
                    etPlantingDate.setText(formattedDate);
                } else {
                    harvestDateMillis = selectedDate;
                    etHarvestDate.setText(formattedDate);
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }
    
    /**
     * Save harvest card
     */
    private void saveHarvestCard() {
        String cropName = etCropName.getText().toString().trim();
        String variety = etVariety.getText().toString().trim();
        String farmerName = etFarmerName.getText().toString().trim();
        String farmLocation = etFarmLocation.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String qualityGrade = etQualityGrade.getText().toString().trim();
        
        // Validation
        if (cropName.isEmpty()) {
            etCropName.setError("Please enter crop name");
            etCropName.requestFocus();
            return;
        }
        
        if (farmerName.isEmpty()) {
            etFarmerName.setError("Please enter farmer name");
            etFarmerName.requestFocus();
            return;
        }
        
        if (farmLocation.isEmpty()) {
            etFarmLocation.setError("Please enter farm location");
            etFarmLocation.requestFocus();
            return;
        }
        
        if (quantityStr.isEmpty()) {
            etQuantity.setError("Please enter quantity");
            etQuantity.requestFocus();
            return;
        }
        
        if (harvestDateMillis == 0) {
            etHarvestDate.setError("Please select harvest date");
            etHarvestDate.requestFocus();
            return;
        }
        
        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Please enter valid quantity");
            etQuantity.requestFocus();
            return;
        }
        
        // Create harvest card object
        HarvestCard harvestCard = new HarvestCard();
        harvestCard.setUserId(1); // Default user ID
        harvestCard.setCropName(cropName);
        harvestCard.setVariety(variety.isEmpty() ? "Standard" : variety);
        harvestCard.setFarmerName(farmerName);
        harvestCard.setFarmLocation(farmLocation);
        harvestCard.setQuantityHarvested(quantity);
        harvestCard.setUnit(unit.isEmpty() ? "kg" : unit);
        harvestCard.setQualityGrade(qualityGrade.isEmpty() ? "Grade A" : qualityGrade);
        
        if (plantingDateMillis != 0) {
            harvestCard.setPlantingDate(plantingDateMillis);
        }
        harvestCard.setHarvestDate(harvestDateMillis);
        
        // Set some default values
        harvestCard.setOrganicCertified(false);
        harvestCard.setTreatmentsApplied("Standard farming practices");
        harvestCard.setStorageCondition("Room temperature");
        harvestCard.setFarmerId("KL" + System.currentTimeMillis() % 100000); // Generate farmer ID
        
        // Show loading state
        btnSave.setEnabled(false);
        btnSave.setText("Creating Card...");
        
        // Save to database using TraceabilityService
        traceabilityService.saveHarvestCard(harvestCard, new TraceabilityService.SaveHarvestCardCallback() {
            @Override
            public void onSuccess(HarvestCard savedCard) {
                runOnUiThread(() -> {
                    // Add card to static list for immediate display
                    TraceabilityActivity.addNewHarvestCard(savedCard);
                    
                    Toast.makeText(CreateHarvestCardActivity.this, 
                        "✅ Harvest Card Created Successfully!\n" +
                        "🌾 Crop: " + savedCard.getCropName() + "\n" +
                        "📱 QR Code: " + savedCard.getQrCode(), 
                        Toast.LENGTH_LONG).show();
                    
                    setResult(RESULT_OK);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    btnSave.setEnabled(true);
                    btnSave.setText("Save Card");
                    Toast.makeText(CreateHarvestCardActivity.this, 
                        "❌ Error creating harvest card: " + error, 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
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
