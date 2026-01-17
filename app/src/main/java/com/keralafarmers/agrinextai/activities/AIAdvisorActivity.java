package com.keralafarmers.agrinextai.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.DiseaseHistoryAdapter;
import com.keralafarmers.agrinextai.models.PlantDisease;
import com.keralafarmers.agrinextai.services.AIAdvisorService;
import com.keralafarmers.agrinextai.services.AuthService;
import com.keralafarmers.agrinextai.utils.LanguageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * AI Advisor Activity for plant disease detection and treatment recommendations
 * Features: Camera integration, disease detection, multilingual TTS, treatment advice
 */
public class AIAdvisorActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_STORAGE_PERMISSION = 1002;
    private static final int REQUEST_IMAGE_CAPTURE = 1003;
    private static final int REQUEST_IMAGE_SELECT = 1004;
    
    // UI Components
    private ImageView ivPlantImage;
    private MaterialButton btnTakePhoto, btnSelectImage, btnAnalyze;
    private MaterialButton btnViewTreatment, btnSpeakAdvice;
    private CardView cardProgress, cardResults, cardTreatment;
    private TextView tvConfidence, tvDiseaseName, tvDiseaseDescription;
    private TextView tvTreatment, tvFertilizer, tvWaterRequirement, tvApplicationMethod;
    private RecyclerView rvRecentDetections;
    private TextView tvNoDetections;
    private Spinner spinnerLanguage;
    private ImageView ivFertilizerImage;
    
    // Services and Data
    private AIAdvisorService aiAdvisorService;
    private AuthService authService;
    private LanguageManager languageManager;
    private TextToSpeech textToSpeech;
    private DiseaseHistoryAdapter diseaseHistoryAdapter;
    
    // Current state
    private String currentImagePath;
    private Bitmap currentImageBitmap;
    private PlantDisease currentDetectedDisease;
    private List<PlantDisease> diseaseHistory;
    private boolean isTTSReady = false;
    private String currentLanguage = "en";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_ai_advisor);
        
        initializeComponents();
        setupToolbar();
        setupLanguageSpinner();
        setupRecyclerView();
        checkPermissions();
        loadRecentDetections();
    }
    
    /**
     * Initialize all components and services
     */
    private void initializeComponents() {
        // Initialize UI components
        ivPlantImage = findViewById(R.id.ivPlantImage);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        btnViewTreatment = findViewById(R.id.btnViewTreatment);
        btnSpeakAdvice = findViewById(R.id.btnSpeakAdvice);
        
        cardProgress = findViewById(R.id.cardProgress);
        cardResults = findViewById(R.id.cardResults);
        cardTreatment = findViewById(R.id.cardTreatment);
        
        tvConfidence = findViewById(R.id.tvConfidence);
        tvDiseaseName = findViewById(R.id.tvDiseaseName);
        tvDiseaseDescription = findViewById(R.id.tvDiseaseDescription);
        tvTreatment = findViewById(R.id.tvTreatment);
        tvFertilizer = findViewById(R.id.tvFertilizer);
        tvWaterRequirement = findViewById(R.id.tvWaterRequirement);
        tvApplicationMethod = findViewById(R.id.tvApplicationMethod);
        
        rvRecentDetections = findViewById(R.id.rvRecentDetections);
        tvNoDetections = findViewById(R.id.tvNoDetections);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        ivFertilizerImage = findViewById(R.id.ivFertilizerImage);
        
        // Initialize services
        aiAdvisorService = new AIAdvisorService(this);
        authService = new AuthService(this);
        textToSpeech = new TextToSpeech(this, this);
        
        // Initialize data
        diseaseHistory = new ArrayList<>();
        
        // Set click listeners
        btnTakePhoto.setOnClickListener(v -> openCamera());
        btnSelectImage.setOnClickListener(v -> openGallery());
        btnAnalyze.setOnClickListener(v -> analyzeImage());
        btnViewTreatment.setOnClickListener(v -> toggleTreatmentView());
        btnSpeakAdvice.setOnClickListener(v -> speakRecommendation());
    }
    
    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.advisor));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    /**
     * Setup language selection spinner
     */
    private void setupLanguageSpinner() {
        // Setup spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.language_options, R.layout.spinner_item_black);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_black);
        spinnerLanguage.setAdapter(adapter);
        
        // Set current language selection
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
                    currentLanguage = codes[position];
                    // Update language manager
                    languageManager.setLanguage(currentLanguage);
                    
                    // Refresh current display if disease is detected
                    if (currentDetectedDisease != null) {
                        displayDetectionResults(currentDetectedDisease);
                        if (cardTreatment.getVisibility() == View.VISIBLE) {
                            displayTreatmentRecommendations(currentDetectedDisease);
                        }
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
     * Setup RecyclerView for disease history
     */
    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRecentDetections.setLayoutManager(layoutManager);
        diseaseHistoryAdapter = new DiseaseHistoryAdapter(this, diseaseHistory, this::onDiseaseHistoryClick);
        rvRecentDetections.setAdapter(diseaseHistoryAdapter);
    }
    
    /**
     * Check and request necessary permissions
     */
    private void checkPermissions() {
        String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_CAMERA_PERMISSION);
        }
    }
    
    /**
     * Open camera to take photo
     */
    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.camera_permission_required), Toast.LENGTH_LONG).show();
            return;
        }
        
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create image file
            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    
    /**
     * Open gallery to select image
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }
    
    /**
     * Create image file for camera capture
     */
    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "PLANT_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentImagePath = image.getAbsolutePath();
            return image;
        } catch (IOException ex) {
            Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    
    /**
     * Save bitmap to file
     */
    private String saveBitmapToFile(Bitmap bitmap) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "PLANT_" + timeStamp + ".jpg";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(storageDir, fileName);
            
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    
    /**
     * Analyze the selected/captured image
     */
    private void analyzeImage() {
        if (currentImageBitmap == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Show progress
        showAnalysisProgress(true);
        
        // Get current user ID
        int userId = authService.getSessionManager().getUserId();
        
        // Detect plant disease
        aiAdvisorService.detectPlantDisease(currentImageBitmap, currentImagePath, userId,
                new AIAdvisorService.DiseaseDetectionCallback() {
                    @Override
                    public void onSuccess(PlantDisease plantDisease) {
                        showAnalysisProgress(false);
                        currentDetectedDisease = plantDisease;
                        displayDetectionResults(plantDisease);
                        loadRecentDetections(); // Refresh history
                    }
                    
                    @Override
                    public void onError(String error) {
                        showAnalysisProgress(false);
                        Toast.makeText(AIAdvisorActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    /**
     * Display disease detection results
     */
    private void displayDetectionResults(PlantDisease plantDisease) {
        
        // Show results card
        cardResults.setVisibility(View.VISIBLE);
        
        // Set confidence level
        int confidence = (int) (plantDisease.getConfidenceLevel() * 100);
        tvConfidence.setText(confidence + "%");
        
        // Set disease information
        tvDiseaseName.setText(plantDisease.getLocalizedDiseaseName(currentLanguage));
        tvDiseaseDescription.setText(plantDisease.getLocalizedDescription(currentLanguage));
        
        // Update confidence badge color based on confidence level
        if (confidence >= 80) {
            tvConfidence.setBackgroundResource(R.drawable.confidence_badge);
        } else if (confidence >= 60) {
            tvConfidence.setBackgroundColor(ContextCompat.getColor(this, R.color.warning));
        } else {
            tvConfidence.setBackgroundColor(ContextCompat.getColor(this, R.color.error));
        }
    }
    
    /**
     * Toggle treatment view visibility
     */
    private void toggleTreatmentView() {
        if (currentDetectedDisease == null) return;
        
        boolean isVisible = cardTreatment.getVisibility() == View.VISIBLE;
        cardTreatment.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        
        if (!isVisible) {
            displayTreatmentRecommendations(currentDetectedDisease);
        }
        
        btnViewTreatment.setText(isVisible ? 
                getString(R.string.view_treatment) : 
                getString(R.string.hide_treatment));
    }
    
    /**
     * Display treatment recommendations
     */
    private void displayTreatmentRecommendations(PlantDisease plantDisease) {
        
        tvTreatment.setText(plantDisease.getLocalizedTreatment(currentLanguage));
        tvFertilizer.setText(getLocalizedText(plantDisease.getFertilizer(), 
                plantDisease.getFertilizerHindi(), plantDisease.getFertilizerMalayalam(), currentLanguage));
        tvWaterRequirement.setText(getLocalizedText(plantDisease.getWaterRequirement(),
                plantDisease.getWaterRequirementHindi(), plantDisease.getWaterRequirementMalayalam(), currentLanguage));
        tvApplicationMethod.setText(getLocalizedText(plantDisease.getApplicationMethod(),
                plantDisease.getApplicationMethodHindi(), plantDisease.getApplicationMethodMalayalam(), currentLanguage));
                
        // Display fertilizer image based on disease type
        displayFertilizerImage(plantDisease);
    }
    
    /**
     * Display appropriate fertilizer image based on disease
     */
    private void displayFertilizerImage(PlantDisease plantDisease) {
        int imageResource = getFertilizerImageResource(plantDisease.getDiseaseName());
        if (imageResource != 0) {
            ivFertilizerImage.setImageResource(imageResource);
            ivFertilizerImage.setVisibility(View.VISIBLE);
        } else {
            ivFertilizerImage.setVisibility(View.GONE);
        }
    }
    
    /**
     * Get fertilizer image resource based on disease name
     */
    private int getFertilizerImageResource(String diseaseName) {
        if (diseaseName == null) return 0;
        
        String disease = diseaseName.toLowerCase();
        
        if (disease.contains("blast") || disease.contains("blight")) {
            return R.drawable.ic_fertilizer_fungicide;
        } else if (disease.contains("leaf") || disease.contains("spot")) {
            return R.drawable.ic_fertilizer_organic;
        } else if (disease.contains("nutrient") || disease.contains("deficiency")) {
            return R.drawable.ic_fertilizer_npk;
        } else if (disease.contains("root") || disease.contains("wilt")) {
            return R.drawable.ic_fertilizer_liquid;
        } else {
            // Default to NPK fertilizer
            return R.drawable.ic_fertilizer_npk;
        }
    }
    
    /**
     * Get localized text based on current language
     */
    private String getLocalizedText(String english, String hindi, String malayalam, String language) {
        switch (language) {
            case "hi":
                return hindi != null ? hindi : english;
            case "ml":
                return malayalam != null ? malayalam : english;
            default:
                return english;
        }
    }
    
    /**
     * Speak recommendation using Text-to-Speech
     */
    private void speakRecommendation() {
        if (currentDetectedDisease == null) {
            Toast.makeText(this, "No disease detected to speak about", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!isTTSReady) {
            Toast.makeText(this, "Text-to-Speech not ready", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Set TTS language based on current selection
        Locale locale;
        switch (currentLanguage) {
            case "hi":
                locale = new Locale("hi", "IN");
                break;
            case "ml":
                locale = new Locale("ml", "IN");
                break;
            default:
                locale = Locale.ENGLISH;
                break;
        }
        
        int result = textToSpeech.setLanguage(locale);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to English
            textToSpeech.setLanguage(Locale.ENGLISH);
            // But keep the current language for text retrieval
        }
        
        // Ensure slower speech rate is maintained
        textToSpeech.setSpeechRate(0.7f);
        textToSpeech.setPitch(1.0f);
        
        // Prepare text to speak
        StringBuilder textToSpeak = new StringBuilder();
        textToSpeak.append(getString(R.string.disease_detected)).append(": ");
        textToSpeak.append(currentDetectedDisease.getLocalizedDiseaseName(currentLanguage)).append(". ");
        textToSpeak.append(getString(R.string.treatment_recommendation)).append(": ");
        textToSpeak.append(currentDetectedDisease.getLocalizedTreatment(currentLanguage));
        
        // Add fertilizer information
        String fertilizerInfo = getLocalizedText(
                currentDetectedDisease.getFertilizer(),
                currentDetectedDisease.getFertilizerHindi(),
                currentDetectedDisease.getFertilizerMalayalam(),
                currentLanguage
        );
        if (fertilizerInfo != null && !fertilizerInfo.isEmpty()) {
            textToSpeak.append(". ").append(getString(R.string.fertilizer_recommendation)).append(": ");
            textToSpeak.append(fertilizerInfo);
        }
        
        // Speak the text
        textToSpeech.speak(textToSpeak.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        
        Toast.makeText(this, getString(R.string.speaking_recommendation), Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Load recent disease detections
     */
    private void loadRecentDetections() {
        int userId = authService.getSessionManager().getUserId();
        
        aiAdvisorService.getDiseaseHistory(userId, new AIAdvisorService.DiseaseHistoryCallback() {
            @Override
            public void onSuccess(List<PlantDisease> diseaseList) {
                diseaseHistory.clear();
                // Show only recent 5 detections
                int limit = Math.min(diseaseList.size(), 5);
                for (int i = 0; i < limit; i++) {
                    diseaseHistory.add(diseaseList.get(i));
                }
                
                diseaseHistoryAdapter.notifyDataSetChanged();
                
                if (diseaseHistory.isEmpty()) {
                    tvNoDetections.setVisibility(View.VISIBLE);
                    rvRecentDetections.setVisibility(View.GONE);
                } else {
                    tvNoDetections.setVisibility(View.GONE);
                    rvRecentDetections.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onError(String error) {
                Toast.makeText(AIAdvisorActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Handle disease history item click
     */
    private void onDiseaseHistoryClick(PlantDisease plantDisease) {
        currentDetectedDisease = plantDisease;
        displayDetectionResults(plantDisease);
        
        // Load image if exists
        if (plantDisease.getImagePath() != null && !plantDisease.getImagePath().isEmpty()) {
            File imageFile = new File(plantDisease.getImagePath());
            if (imageFile.exists()) {
                currentImageBitmap = BitmapFactory.decodeFile(plantDisease.getImagePath());
                ivPlantImage.setImageBitmap(currentImageBitmap);
                btnAnalyze.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * Show/hide analysis progress
     */
    private void showAnalysisProgress(boolean show) {
        cardProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        btnAnalyze.setEnabled(!show);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    // Image captured from camera
                    if (currentImagePath != null) {
                        currentImageBitmap = BitmapFactory.decodeFile(currentImagePath);
                        ivPlantImage.setImageBitmap(currentImageBitmap);
                        btnAnalyze.setVisibility(View.VISIBLE);
                    }
                    break;
                    
                case REQUEST_IMAGE_SELECT:
                    // Image selected from gallery
                    if (data != null && data.getData() != null) {
                        try {
                            Uri imageUri = data.getData();
                            currentImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            currentImagePath = saveBitmapToFile(currentImageBitmap);
                            ivPlantImage.setImageBitmap(currentImageBitmap);
                            btnAnalyze.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            
            if (!allPermissionsGranted) {
                Toast.makeText(this, "Permissions are required for camera and storage access", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isTTSReady = true;
            // Set default language
            textToSpeech.setLanguage(Locale.ENGLISH);
            // Set slower speech rate for better comprehension
            textToSpeech.setSpeechRate(0.7f); // 0.7x normal speed
            textToSpeech.setPitch(1.0f); // Normal pitch
        } else {
            Toast.makeText(this, "Text-to-Speech initialization failed", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}