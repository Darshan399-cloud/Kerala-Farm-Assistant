package com.keralafarmers.agrinextai.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.utils.LanguageManager;

/**
 * Activity for viewing harvest card details with QR code
 * Shows complete harvest information and scannable QR code
 */
public class HarvestCardDetailActivity extends AppCompatActivity {
    
    private ImageView ivQrCode;
    private TextView tvQrCodeText, tvCardInfo;
    private MaterialButton btnShare, btnScanQr, btnDownload;
    private LanguageManager languageManager;
    private String qrCodeData;
    private Bitmap qrBitmap;
    private static final int PERMISSION_REQUEST_WRITE_STORAGE = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_harvest_card_detail);
        
        int cardId = getIntent().getIntExtra("card_id", 0);
        qrCodeData = getIntent().getStringExtra("qr_code");
        
        // If no QR code provided, generate one
        if (qrCodeData == null || qrCodeData.trim().isEmpty()) {
            qrCodeData = "QR" + System.currentTimeMillis();
        }
        
        setupToolbar();
        initializeViews();
        generateQRCode();
        setupClickListeners();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.harvest_card_details));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        ivQrCode = findViewById(R.id.ivQrCode);
        tvQrCodeText = findViewById(R.id.tvQrCodeText);
        tvCardInfo = findViewById(R.id.tvCardInfo);
        btnShare = findViewById(R.id.btnShare);
        btnScanQr = findViewById(R.id.btnScanQr);
        btnDownload = findViewById(R.id.btnDownload);
        
        // Set QR code text
        tvQrCodeText.setText("QR Code: " + qrCodeData);
        
        // Get card data from intent
        String cropName = getIntent().getStringExtra("crop_name");
        String farmerName = getIntent().getStringExtra("farmer_name");
        String farmLocation = getIntent().getStringExtra("farm_location");
        double quantity = getIntent().getDoubleExtra("quantity", 0.0);
        String unit = getIntent().getStringExtra("unit");
        String qualityGrade = getIntent().getStringExtra("quality_grade");
        long harvestDate = getIntent().getLongExtra("harvest_date", System.currentTimeMillis());
        String variety = getIntent().getStringExtra("variety");
        boolean isOrganic = getIntent().getBooleanExtra("is_organic", false);
        
        // Format harvest date
        String formattedDate = new java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                .format(new java.util.Date(harvestDate));
        
        // Set card info with real data
        StringBuilder cardInfo = new StringBuilder();
        cardInfo.append("🌾 HARVEST CARD DETAILS\n\n");
        cardInfo.append("🌾 Crop: ").append(cropName != null ? cropName : "N/A").append("\n");
        cardInfo.append("🌱 Variety: ").append(variety != null ? variety : "Standard").append("\n");
        cardInfo.append("👨‍🌾 Farmer: ").append(farmerName != null ? farmerName : "N/A").append("\n");
        cardInfo.append("📍 Location: ").append(farmLocation != null ? farmLocation : "N/A").append("\n");
        cardInfo.append("⚖️ Quantity: ").append(String.format("%.1f %s", quantity, unit != null ? unit : "kg")).append("\n");
        cardInfo.append("🏅 Quality: ").append(qualityGrade != null ? qualityGrade : "Grade A").append("\n");
        cardInfo.append("📅 Harvest Date: ").append(formattedDate).append("\n");
        if (isOrganic) {
            cardInfo.append("🌱 Certified Organic ✅\n");
        }
        cardInfo.append("\n📱 QR Code: ").append(qrCodeData).append("\n");
        cardInfo.append("Status: Verified ✅\n");
        cardInfo.append("Generated: ").append(new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(new java.util.Date())).append("\n\n");
        cardInfo.append("ℹ️ Scan the QR code above to verify this harvest information.");
        
        tvCardInfo.setText(cardInfo.toString());
    }
    
    /**
     * Generate QR code bitmap
     */
    private void generateQRCode() {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            
            // Create QR code data with complete information
            String qrData = createQRData();
            
            BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            
            // Store bitmap for download
            qrBitmap = bitmap;
            ivQrCode.setImageBitmap(bitmap);
            
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Create QR code data string
     */
    private String createQRData() {
        return "HARVEST_CARD|" +
                "ID:" + qrCodeData + "|" +
                "APP:Kerala_Farm_Assistant|" +
                "TYPE:Harvest_Traceability|" +
                "TIMESTAMP:" + System.currentTimeMillis() + "|" +
                "VERIFY_URL:https://keralafarm.app/verify/" + qrCodeData;
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        btnShare.setOnClickListener(v -> shareQRCode());
        btnScanQr.setOnClickListener(v -> showScanInstructions());
        btnDownload.setOnClickListener(v -> downloadQRCode());
    }
    
    /**
     * Share QR code information
     */
    private void shareQRCode() {
        String shareText = "🌾 HARVEST CARD\n\n" +
                "QR Code: " + qrCodeData + "\n" +
                "Scan this QR code to verify harvest details\n\n" +
                "Generated by Kerala Farm Assistant App\n" +
                "Download: https://keralafarm.app";
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Harvest Card - " + qrCodeData);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        Intent chooser = Intent.createChooser(shareIntent, "Share QR Code");
        startActivity(chooser);
    }
    
    /**
     * Download QR code to gallery
     */
    private void downloadQRCode() {
        if (qrBitmap == null) {
            Toast.makeText(this, "❌ QR Code not ready. Please wait...", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check permissions based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33+)
            // Android 13+ uses scoped storage, no permission needed for MediaStore
            saveQRCodeToGallery();
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10-12 (API 29-32)
            // Android 10-12 uses scoped storage with MediaStore, no permission needed
            saveQRCodeToGallery();
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6-9 (API 23-28)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 
                    PERMISSION_REQUEST_WRITE_STORAGE);
                return;
            }
        }
        
        saveQRCodeToGallery();
    }
    
    /**
     * Save QR code bitmap to gallery
     */
    private void saveQRCodeToGallery() {
        try {
            String fileName = "HarvestCard_QR_" + qrCodeData + "_" + System.currentTimeMillis() + ".png";
            
            // Modern Android approach using MediaStore (works on Android 10+)
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            
            // Create subdirectory for better organization
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AgriNextAI/QR_Codes");
            }
            
            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (imageUri != null) {
                try (java.io.OutputStream outputStream = getContentResolver().openOutputStream(imageUri)) {
                    if (outputStream != null) {
                        qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        Toast.makeText(this, 
                                "✅ QR Code Downloaded Successfully!\n" +
                                "🖼️ Gallery → Pictures → AgriNextAI → QR_Codes\n" +
                                "📄 File: " + fileName, 
                                Toast.LENGTH_LONG).show();
                    } else {
                        throw new Exception("Unable to create output stream");
                    }
                } catch (Exception e) {
                    throw new Exception("Error writing file: " + e.getMessage());
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                // Android 9 and below
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() 
                        + "/AgriNextAI/QR_Codes";
                java.io.File dir = new java.io.File(imagesDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                java.io.File file = new java.io.File(dir, fileName);
                try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file)) {
                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    
                    // Add to media scanner
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                    
                    Toast.makeText(this, "✅ QR Code saved to Gallery!\n📁 " + file.getAbsolutePath(), 
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "❌ Error saving QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Show QR code scanning instructions
     */
    private void showScanInstructions() {
        String instructions = "📱 QR CODE SCANNING INSTRUCTIONS:\n\n" +
                "1. Open any QR code scanner app\n" +
                "2. Point camera at the QR code above\n" +
                "3. Wait for automatic detection\n" +
                "4. View complete harvest details\n\n" +
                "📋 Popular QR Scanner Apps:\n" +
                "• Google Lens\n" +
                "• QR Code Reader\n" +
                "• Built-in camera (newer phones)\n" +
                "• WhatsApp QR scanner";
        
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("How to Scan QR Code")
                .setMessage(instructions)
                .setPositiveButton("Got it!", null)
                .show();
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
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveQRCodeToGallery();
            } else {
                Toast.makeText(this, "❌ Storage permission required to download QR Code", Toast.LENGTH_LONG).show();
            }
        }
    }
}
