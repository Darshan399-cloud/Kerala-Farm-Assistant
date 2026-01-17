package com.keralafarmers.agrinextai.services;

import android.content.Context;
import android.util.Log;

import com.keralafarmers.agrinextai.database.AppDatabase;
import com.keralafarmers.agrinextai.database.MarketPriceDao;
import com.keralafarmers.agrinextai.models.MarketPrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class for managing market price data
 * Handles fetching, caching, and providing market price information
 */
public class MarketPriceService {
    private static final String TAG = "MarketPriceService";
    private MarketPriceDao marketPriceDao;
    private ExecutorService executorService;
    private Random random;
    private Context context;

    // Mock market data for Kerala
    private static final Map<String, CropInfo> CROP_DATABASE = new HashMap<>();

    static {
        // Initialize crop database with Kerala-specific crops
        CROP_DATABASE.put("RICE", new CropInfo(
                "Rice", "चावल", "അരി", "Basmati", 45.0, 55.0
        ));
        CROP_DATABASE.put("COCONUT", new CropInfo(
                "Coconut", "नारियल", "തേങ്ങ", "Dwarf", 25.0, 35.0
        ));
        CROP_DATABASE.put("PEPPER", new CropInfo(
                "Black Pepper", "काली मिर्च", "കുരുമുളക്", "Panniyur", 400.0, 500.0
        ));
        CROP_DATABASE.put("CARDAMOM", new CropInfo(
                "Cardamom", "इलायची", "ഏലം", "Malabar", 800.0, 1200.0
        ));
        CROP_DATABASE.put("RUBBER", new CropInfo(
                "Rubber", "रबर", "റബ്ബർ", "RRII", 180.0, 220.0
        ));
        CROP_DATABASE.put("BANANA", new CropInfo(
                "Banana", "केला", "വാഴ", "Robusta", 15.0, 25.0
        ));
        CROP_DATABASE.put("GINGER", new CropInfo(
                "Ginger", "अदरक", "ഇഞ്ചി", "Rio-de-Janeiro", 60.0, 80.0
        ));
        CROP_DATABASE.put("TURMERIC", new CropInfo(
                "Turmeric", "हल्दी", "മഞ്ഞൾ", "Lakadong", 70.0, 90.0
        ));
        CROP_DATABASE.put("VANILLA", new CropInfo(
                "Vanilla", "वनीला", "വെനീല", "Planifolia", 2000.0, 2500.0
        ));
        CROP_DATABASE.put("COFFEE", new CropInfo(
                "Coffee", "कॉफी", "കോഫി", "Arabica", 350.0, 450.0
        ));
    }

    // Kerala districts and markets
    private static final String[] KERALA_DISTRICTS = {
            "Thiruvananthapuram", "Kollam", "Pathanamthitta", "Alappuzha", "Kottayam",
            "Idukki", "Ernakulam", "Thrissur", "Palakkad", "Malappuram",
            "Kozhikode", "Wayanad", "Kannur", "Kasaragod"
    };

    private static final String[] MARKET_NAMES = {
            "Central Market", "Wholesale Market", "Farmer's Market", "Spice Market",
            "Agricultural Market", "Commodity Exchange", "Local Market"
    };

    private static final String[] QUALITY_GRADES = {"A", "B", "C"};

    /**
     * Constructor for MarketPriceService
     * @param context Application context
     */
    public MarketPriceService(Context context) {
        this.context = context;
        AppDatabase database = AppDatabase.getInstance(context);
        this.marketPriceDao = database.marketPriceDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.random = new Random();
    }

    /**
     * Fetch and update market prices (Mock implementation)
     * @param callback Callback to receive operation result
     */
    public void fetchMarketPrices(MarketPriceCallback callback) {
        executorService.execute(() -> {
            try {
                // Simulate API call delay
                Thread.sleep(2000);

                // Generate mock market prices
                List<MarketPrice> mockPrices = generateMockMarketPrices();

                // Clear old data and insert new prices
                marketPriceDao.deleteAllMarketPrices();
                List<Long> insertedIds = marketPriceDao.insertMarketPrices(mockPrices);

                // Fetch updated prices from database
                List<MarketPrice> updatedPrices = marketPriceDao.getAllMarketPrices();

                if (callback != null) {
                    callback.onSuccess(updatedPrices);
                }

                Log.d(TAG, "Market prices updated successfully. Inserted " + insertedIds.size() + " records.");

            } catch (Exception e) {
                Log.e(TAG, "Error fetching market prices", e);
                if (callback != null) {
                    callback.onError("Failed to fetch market prices: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Get all market prices (alias for getCachedMarketPrices)
     * @param callback Callback to receive prices
     */
    public void getAllMarketPrices(MarketPriceCallback callback) {
        getCachedMarketPrices(callback);
    }
    
    /**
     * Get cached market prices from database
     * @param callback Callback to receive prices
     */
    public void getCachedMarketPrices(MarketPriceCallback callback) {
        executorService.execute(() -> {
            try {
                List<MarketPrice> cachedPrices = marketPriceDao.getAllMarketPrices();
                
                if (cachedPrices.isEmpty()) {
                    // If no cached data, fetch fresh data
                    fetchMarketPrices(callback);
                } else {
                    if (callback != null) {
                        callback.onSuccess(cachedPrices);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting cached prices", e);
                if (callback != null) {
                    callback.onError("Failed to get cached prices: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Get market prices for a specific crop
     * @param cropName Name of the crop
     * @param callback Callback to receive prices
     */
    public void getMarketPricesByCrop(String cropName, MarketPriceCallback callback) {
        executorService.execute(() -> {
            try {
                List<MarketPrice> cropPrices = marketPriceDao.getMarketPricesByCrop(cropName);
                if (callback != null) {
                    callback.onSuccess(cropPrices);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting prices for crop: " + cropName, e);
                if (callback != null) {
                    callback.onError("Failed to get prices for " + cropName);
                }
            }
        });
    }

    /**
     * Get market prices for a specific district
     * @param district Name of the district
     * @param callback Callback to receive prices
     */
    public void getMarketPricesByDistrict(String district, MarketPriceCallback callback) {
        executorService.execute(() -> {
            try {
                List<MarketPrice> districtPrices = marketPriceDao.getMarketPricesByDistrict(district);
                if (callback != null) {
                    callback.onSuccess(districtPrices);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting prices for district: " + district, e);
                if (callback != null) {
                    callback.onError("Failed to get prices for " + district);
                }
            }
        });
    }

    /**
     * Get trending prices (up/down trends)
     * @param trendType "UP" or "DOWN"
     * @param callback Callback to receive prices
     */
    public void getTrendingPrices(String trendType, MarketPriceCallback callback) {
        executorService.execute(() -> {
            try {
                List<MarketPrice> trendingPrices;
                if ("UP".equalsIgnoreCase(trendType)) {
                    trendingPrices = marketPriceDao.getPricesWithUpTrend();
                } else {
                    trendingPrices = marketPriceDao.getPricesWithDownTrend();
                }
                
                if (callback != null) {
                    callback.onSuccess(trendingPrices);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting trending prices", e);
                if (callback != null) {
                    callback.onError("Failed to get trending prices");
                }
            }
        });
    }

    /**
     * Search market prices by crop name
     * @param searchQuery Search query
     * @param callback Callback to receive search results
     */
    public void searchMarketPrices(String searchQuery, MarketPriceCallback callback) {
        executorService.execute(() -> {
            try {
                List<MarketPrice> searchResults = marketPriceDao.searchMarketPrices(searchQuery);
                if (callback != null) {
                    callback.onSuccess(searchResults);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error searching prices", e);
                if (callback != null) {
                    callback.onError("Search failed");
                }
            }
        });
    }

    /**
     * Get unique districts for filter
     * @param callback Callback to receive district list
     */
    public void getUniqueDistricts(DistrictCallback callback) {
        executorService.execute(() -> {
            try {
                List<String> districts = marketPriceDao.getUniqueDistricts();
                if (callback != null) {
                    callback.onSuccess(districts);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting districts", e);
                if (callback != null) {
                    callback.onError("Failed to get districts");
                }
            }
        });
    }

    /**
     * Generate mock market prices for demonstration
     * @return List of mock MarketPrice objects
     */
    private List<MarketPrice> generateMockMarketPrices() {
        List<MarketPrice> mockPrices = new ArrayList<>();
        
        for (String cropKey : CROP_DATABASE.keySet()) {
            CropInfo cropInfo = CROP_DATABASE.get(cropKey);
            
            // Generate prices for multiple districts/markets
            for (int i = 0; i < 3; i++) {
                String district = KERALA_DISTRICTS[random.nextInt(KERALA_DISTRICTS.length)];
                String market = MARKET_NAMES[random.nextInt(MARKET_NAMES.length)];
                String grade = QUALITY_GRADES[random.nextInt(QUALITY_GRADES.length)];
                
                MarketPrice marketPrice = new MarketPrice();
                marketPrice.setCropName(cropInfo.nameEn);
                marketPrice.setCropNameHi(cropInfo.nameHi);
                marketPrice.setCropNameMl(cropInfo.nameMl);
                marketPrice.setVariety(cropInfo.variety);
                marketPrice.setMarketName(market);
                marketPrice.setDistrict(district);
                marketPrice.setQualityGrade(grade);
                
                // Generate price within range
                double basePrice = cropInfo.minPrice + (random.nextDouble() * (cropInfo.maxPrice - cropInfo.minPrice));
                
                // Add grade factor
                double gradeMultiplier = 1.0;
                switch (grade) {
                    case "A":
                        gradeMultiplier = 1.1; // 10% premium
                        break;
                    case "C":
                        gradeMultiplier = 0.9; // 10% discount
                        break;
                }
                
                double currentPrice = basePrice * gradeMultiplier;
                double previousPrice = currentPrice * (0.9 + random.nextDouble() * 0.2); // ±10% variation
                
                marketPrice.setPricePerKg(Math.round(currentPrice * 100.0) / 100.0);
                marketPrice.setPreviousPrice(Math.round(previousPrice * 100.0) / 100.0);
                marketPrice.calculatePriceChange();
                
                marketPrice.setMinPrice(currentPrice * 0.9);
                marketPrice.setMaxPrice(currentPrice * 1.1);
                marketPrice.setOrganic(random.nextBoolean() && random.nextDouble() < 0.3); // 30% chance organic
                marketPrice.setSource("Mock API");
                marketPrice.setPriceDate(System.currentTimeMillis());
                
                mockPrices.add(marketPrice);
            }
        }
        
        return mockPrices;
    }

    /**
     * Clean up resources
     */
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    /**
     * Get crop name by language
     * @param marketPrice MarketPrice object
     * @param language Language code ("en", "hi", "ml")
     * @return Crop name in specified language
     */
    public String getCropNameByLanguage(MarketPrice marketPrice, String language) {
        if (marketPrice == null) return "";

        switch (language.toLowerCase()) {
            case "hi":
                return marketPrice.getCropNameHi() != null ? marketPrice.getCropNameHi() : marketPrice.getCropName();
            case "ml":
                return marketPrice.getCropNameMl() != null ? marketPrice.getCropNameMl() : marketPrice.getCropName();
            case "en":
            default:
                return marketPrice.getCropName();
        }
    }

    // Callback interfaces
    public interface MarketPriceCallback {
        void onSuccess(List<MarketPrice> marketPrices);
        void onError(String error);
    }

    public interface DistrictCallback {
        void onSuccess(List<String> districts);
        void onError(String error);
    }

    // Inner class for crop information
    private static class CropInfo {
        String nameEn, nameHi, nameMl, variety;
        double minPrice, maxPrice;

        CropInfo(String nameEn, String nameHi, String nameMl, String variety, 
                double minPrice, double maxPrice) {
            this.nameEn = nameEn;
            this.nameHi = nameHi;
            this.nameMl = nameMl;
            this.variety = variety;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }
    }
}