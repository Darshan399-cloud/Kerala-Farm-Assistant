package com.keralafarmers.agrinextai.services;

import android.content.Context;
import android.util.Log;
import com.keralafarmers.agrinextai.database.AppDatabase;
import com.keralafarmers.agrinextai.database.HarvestCardDao;
import com.keralafarmers.agrinextai.models.HarvestCard;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service for managing harvest cards and QR code traceability
 */
public class TraceabilityService {
    private static final String TAG = "TraceabilityService";
    private HarvestCardDao harvestCardDao;
    private ExecutorService executorService;
    private Context context;

    public TraceabilityService(Context context) {
        this.context = context;
        AppDatabase database = AppDatabase.getInstance(context);
        this.harvestCardDao = database.harvestCardDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Get all harvest cards for a user
     */
    public void getAllHarvestCards(int userId, HarvestCardCallback callback) {
        executorService.execute(() -> {
            try {
                List<HarvestCard> cards = harvestCardDao.getHarvestCardsByUser(userId);
                if (callback != null) {
                    callback.onSuccess(cards);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting harvest cards", e);
                if (callback != null) {
                    callback.onError("Failed to get harvest cards");
                }
            }
        });
    }

    /**
     * Save new harvest card
     */
    public void saveHarvestCard(HarvestCard card, SaveHarvestCardCallback callback) {
        executorService.execute(() -> {
            try {
                // Generate unique QR code
                String qrCode = "QR" + System.currentTimeMillis();
                card.setQrCode(qrCode);
                
                // Set creation timestamp
                card.setCreatedDate(System.currentTimeMillis());
                card.setUpdatedDate(System.currentTimeMillis());
                card.setActive(true);
                
                // Insert card into database
                long id = harvestCardDao.insertHarvestCard(card);
                card.setId((int) id);
                
                if (callback != null) {
                    callback.onSuccess(card);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error saving harvest card", e);
                if (callback != null) {
                    callback.onError("Failed to save harvest card: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Generate QR code for harvest card
     */
    public void generateQRCode(HarvestCard card, QRGenerationCallback callback) {
        executorService.execute(() -> {
            try {
                // Generate unique QR code
                String qrCode = "QR" + System.currentTimeMillis();
                String filePath = "qr_codes/" + qrCode + ".png";
                
                // Update card with QR code
                card.setQrCode(qrCode);
                harvestCardDao.updateHarvestCard(card);
                
                if (callback != null) {
                    callback.onSuccess(qrCode, filePath);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error generating QR code", e);
                if (callback != null) {
                    callback.onError("Failed to generate QR code");
                }
            }
        });
    }

    // Callback interfaces
    public interface HarvestCardCallback {
        void onSuccess(List<HarvestCard> cards);
        void onError(String error);
    }
    
    public interface SaveHarvestCardCallback {
        void onSuccess(HarvestCard savedCard);
        void onError(String error);
    }

    public interface QRGenerationCallback {
        void onSuccess(String qrCode, String filePath);
        void onError(String error);
    }
}