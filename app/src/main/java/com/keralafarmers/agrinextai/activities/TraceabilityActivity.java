package com.keralafarmers.agrinextai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.HarvestCardAdapter;
import com.keralafarmers.agrinextai.models.HarvestCard;
import com.keralafarmers.agrinextai.services.TraceabilityService;
import com.keralafarmers.agrinextai.utils.LanguageManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Traceability Activity for managing harvest cards and QR codes
 * Allows farmers to create, view, and manage harvest records with QR code generation
 */
public class TraceabilityActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewCards;
    private FloatingActionButton fabAddCard;
    private MaterialButton btnScanQr;
    private HarvestCardAdapter adapter;
    private TraceabilityService traceabilityService;
    private LanguageManager languageManager;
    private List<HarvestCard> harvestCards;
    private static List<HarvestCard> staticHarvestCards = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_traceability);
        
        setupToolbar();
        initializeViews();
        initializeServices();
        loadHarvestCards();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.harvest_card));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        recyclerViewCards = findViewById(R.id.recyclerViewCards);
        fabAddCard = findViewById(R.id.fabAddCard);
        btnScanQr = findViewById(R.id.btnScanQr);
        
        // Setup RecyclerView
        harvestCards = new ArrayList<>();
        adapter = new HarvestCardAdapter(harvestCards, this);
        recyclerViewCards.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCards.setAdapter(adapter);
        
        // Setup FAB click listener
        fabAddCard.setOnClickListener(v -> createNewHarvestCard());
        
        // Setup QR scanner button click listener
        btnScanQr.setOnClickListener(v -> startQRScanner());
    }
    
    /**
     * Initialize services
     */
    private void initializeServices() {
        traceabilityService = new TraceabilityService(this);
    }
    
    /**
     * Load harvest cards from service
     */
    private void loadHarvestCards() {
        traceabilityService.getAllHarvestCards(1, new TraceabilityService.HarvestCardCallback() {
            @Override
            public void onSuccess(List<HarvestCard> cards) {
                runOnUiThread(() -> {
                    harvestCards.clear();
                    harvestCards.addAll(cards);
                    adapter.notifyDataSetChanged();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TraceabilityActivity.this, "Error loading cards: " + error, Toast.LENGTH_SHORT).show();
                    // Load sample data for demo
                    loadSampleData();
                });
            }
        });
    }
    
    /**
     * Load sample data for demonstration
     */
    private void loadSampleData() {
        List<HarvestCard> sampleCards = new ArrayList<>();
        
        // Add static cards from form submissions first
        sampleCards.addAll(staticHarvestCards);
        
        HarvestCard card1 = new HarvestCard();
        card1.setId(1);
        card1.setCropName("Basmati Rice");
        card1.setVariety("Pusa Basmati 1509");
        card1.setFarmLocation("Palakkad District, Kerala");
        card1.setPlantingDate(System.currentTimeMillis() - (120L * 24 * 60 * 60 * 1000)); // 120 days ago
        card1.setHarvestDate(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000)); // 10 days ago
        card1.setQuantityHarvested(250.5);
        card1.setUnit("kg");
        card1.setQualityGrade("Grade A");
        card1.setFarmerName("Rajesh Kumar");
        card1.setFarmerId("KL001234");
        card1.setOrganicCertified(true);
        card1.setTreatmentsApplied("Neem oil, Organic fertilizer");
        card1.setStorageCondition("Cool dry place, 20°C");
        card1.setQrCode("QR001234567890");
        sampleCards.add(card1);
        
        HarvestCard card2 = new HarvestCard();
        card2.setId(2);
        card2.setCropName("Black Pepper");
        card2.setVariety("Panniyur-1");
        card2.setFarmLocation("Idukki District, Kerala");
        card2.setPlantingDate(System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000)); // 1 year ago
        card2.setHarvestDate(System.currentTimeMillis() - (5L * 24 * 60 * 60 * 1000)); // 5 days ago
        card2.setQuantityHarvested(45.0);
        card2.setUnit("kg");
        card2.setQualityGrade("Grade A+");
        card2.setFarmerName("Suresh Menon");
        card2.setFarmerId("KL005678");
        card2.setOrganicCertified(true);
        card2.setTreatmentsApplied("Organic compost, Biofertilizer");
        card2.setStorageCondition("Dry storage, 15°C");
        card2.setQrCode("QR009876543210");
        sampleCards.add(card2);
        
        HarvestCard card3 = new HarvestCard();
        card3.setId(3);
        card3.setCropName("Cardamom");
        card3.setVariety("Malabar");
        card3.setFarmLocation("Kumily, Idukki");
        card3.setPlantingDate(System.currentTimeMillis() - (730L * 24 * 60 * 60 * 1000)); // 2 years ago
        card3.setHarvestDate(System.currentTimeMillis() - (15L * 24 * 60 * 60 * 1000)); // 15 days ago
        card3.setQuantityHarvested(12.5);
        card3.setUnit("kg");
        card3.setQualityGrade("Premium");
        card3.setFarmerName("Anitha Devi");
        card3.setFarmerId("KL009012");
        card3.setOrganicCertified(true);
        card3.setTreatmentsApplied("Organic manure only");
        card3.setStorageCondition("Climate controlled, 18°C");
        card3.setQrCode("QR111222333444");
        sampleCards.add(card3);
        
        harvestCards.clear();
        harvestCards.addAll(sampleCards);
        adapter.notifyDataSetChanged();
    }
    
    /**
     * Create new harvest card
     */
    private void createNewHarvestCard() {
        Intent intent = new Intent(this, CreateHarvestCardActivity.class);
        startActivityForResult(intent, 100);
    }
    
    /**
     * Add new harvest card to static list (for immediate display)
     */
    public static void addNewHarvestCard(HarvestCard card) {
        staticHarvestCards.add(0, card); // Add at beginning for newest first
    }
    
    /**
     * Start QR code scanner
     */
    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("📱 Scan Harvest Card QR Code\n\nAlign the QR code within the frame");
        integrator.setTorchEnabled(false);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }
    
    /**
     * Process scanned QR code result
     */
    private void processQRScanResult(String scannedData) {
        if (scannedData == null || scannedData.trim().isEmpty()) {
            Toast.makeText(this, "❌ No QR code data found", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Check if it's a harvest card QR code
            if (scannedData.startsWith("HARVEST_CARD|")) {
                processHarvestCardQR(scannedData);
            } else {
                // Try to find matching QR code in existing cards
                HarvestCard matchingCard = findCardByQRCode(scannedData);
                if (matchingCard != null) {
                    showCardDetails(matchingCard);
                } else {
                    showGenericQRResult(scannedData);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "❌ Error processing QR code: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Process harvest card specific QR code
     */
    private void processHarvestCardQR(String qrData) {
        try {
            String[] parts = qrData.split("\\|");
            String cardId = "";
            
            for (String part : parts) {
                if (part.startsWith("ID:")) {
                    cardId = part.substring(3);
                    break;
                }
            }
            
            if (!cardId.isEmpty()) {
                HarvestCard matchingCard = findCardByQRCode(cardId);
                if (matchingCard != null) {
                    showCardDetails(matchingCard);
                    Toast.makeText(this, "✅ Harvest Card Found: " + matchingCard.getCropName(), Toast.LENGTH_SHORT).show();
                } else {
                    showQRNotFoundDialog(cardId);
                }
            } else {
                Toast.makeText(this, "❌ Invalid harvest card QR format", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "❌ Error parsing harvest card QR: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Find harvest card by QR code
     */
    private HarvestCard findCardByQRCode(String qrCode) {
        for (HarvestCard card : harvestCards) {
            if (card.getQrCode() != null && card.getQrCode().equals(qrCode)) {
                return card;
            }
        }
        return null;
    }
    
    /**
     * Show card details
     */
    private void showCardDetails(HarvestCard card) {
        Intent intent = new Intent(this, HarvestCardDetailActivity.class);
        intent.putExtra("card_id", card.getId());
        intent.putExtra("qr_code", card.getQrCode());
        startActivity(intent);
    }
    
    /**
     * Show generic QR result
     */
    private void showGenericQRResult(String data) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("📄 QR Code Scanned")
                .setMessage("Content: " + data + "\n\n❌ This QR code is not a recognized harvest card from this app.")
                .setPositiveButton("OK", null)
                .setNeutralButton("Copy Text", (dialog, which) -> {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("QR Code", data);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "📋 Text copied to clipboard", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
    
    /**
     * Show QR not found dialog
     */
    private void showQRNotFoundDialog(String qrCode) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("❌ Harvest Card Not Found")
                .setMessage("QR Code: " + qrCode + "\n\nThis harvest card is not found in your local records. It might be:\n\n• From another farmer\n• An older card that was deleted\n• From a different version of the app")
                .setPositiveButton("OK", null)
                .show();
    }
    
    /**
     * Handle card item clicks
     */
    public void onCardClicked(HarvestCard card) {
        Intent intent = new Intent(this, HarvestCardDetailActivity.class);
        intent.putExtra("card_id", card.getId());
        intent.putExtra("qr_code", card.getQrCode());
        intent.putExtra("crop_name", card.getCropName());
        intent.putExtra("farmer_name", card.getFarmerName());
        intent.putExtra("farm_location", card.getFarmLocation());
        intent.putExtra("quantity", card.getQuantityHarvested());
        intent.putExtra("unit", card.getUnit());
        intent.putExtra("quality_grade", card.getQualityGrade());
        intent.putExtra("harvest_date", card.getHarvestDate());
        intent.putExtra("variety", card.getVariety());
        intent.putExtra("is_organic", card.isOrganicCertified());
        startActivity(intent);
    }
    
    /**
     * Generate QR code for a harvest card
     */
    public void generateQRCode(HarvestCard card) {
        traceabilityService.generateQRCode(card, new TraceabilityService.QRGenerationCallback() {
            @Override
            public void onSuccess(String qrCode, String filePath) {
                runOnUiThread(() -> {
                    Toast.makeText(TraceabilityActivity.this, "QR Code generated successfully!", Toast.LENGTH_SHORT).show();
                    // Refresh the card data
                    loadHarvestCards();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TraceabilityActivity.this, "Error generating QR code: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Share harvest card information
     */
    public void shareHarvestCard(HarvestCard card) {
        String shareText = createShareText(card);
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Harvest Card - " + card.getCropName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        Intent chooser = Intent.createChooser(shareIntent, "Share Harvest Card");
        startActivity(chooser);
    }
    
    /**
     * Create share text for harvest card
     */
    private String createShareText(HarvestCard card) {
        StringBuilder sb = new StringBuilder();
        sb.append("🌾 HARVEST CARD 🌾\n\n");
        sb.append("Crop: ").append(card.getCropName()).append("\n");
        sb.append("Variety: ").append(card.getVariety()).append("\n");
        sb.append("Farmer: ").append(card.getFarmerName()).append("\n");
        sb.append("Location: ").append(card.getFarmLocation()).append("\n");
        sb.append("Quantity: ").append(card.getQuantityHarvested()).append(" ").append(card.getUnit()).append("\n");
        sb.append("Quality: ").append(card.getQualityGrade()).append("\n");
        
        if (card.isOrganicCertified()) {
            sb.append("🌱 Certified Organic\n");
        }
        
        sb.append("QR Code: ").append(card.getQrCode()).append("\n\n");
        sb.append("Generated by Kerala Farm Assistant App");
        
        return sb.toString();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Handle QR scanner result
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "📱 QR scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // Process the scanned QR code
                processQRScanResult(result.getContents());
            }
            return;
        }
        
        // Handle create harvest card result
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Refresh harvest cards when returning from create activity
            loadHarvestCards();
        }
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
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to activity
        loadHarvestCards();
    }
}