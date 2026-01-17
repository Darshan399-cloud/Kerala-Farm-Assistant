package com.keralafarmers.agrinextai.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.keralafarmers.agrinextai.models.MarketPrice;

import java.util.List;

/**
 * Data Access Object (DAO) for MarketPrice entity
 * Provides database operations for market price data
 */
@Dao
public interface MarketPriceDao {

    /**
     * Insert a new market price record
     * @param marketPrice MarketPrice object to insert
     * @return The row ID of the inserted record
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMarketPrice(MarketPrice marketPrice);

    /**
     * Insert multiple market price records
     * @param marketPrices List of MarketPrice objects to insert
     * @return List of row IDs
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertMarketPrices(List<MarketPrice> marketPrices);

    /**
     * Update an existing market price record
     * @param marketPrice MarketPrice object with updated data
     * @return Number of rows affected
     */
    @Update
    int updateMarketPrice(MarketPrice marketPrice);

    /**
     * Delete a market price record
     * @param marketPrice MarketPrice object to delete
     * @return Number of rows deleted
     */
    @Delete
    int deleteMarketPrice(MarketPrice marketPrice);

    /**
     * Get all active market prices ordered by crop name
     * @return List of all active MarketPrice records
     */
    @Query("SELECT * FROM market_prices WHERE is_active = 1 ORDER BY crop_name ASC")
    List<MarketPrice> getAllMarketPrices();

    /**
     * Get market prices for a specific crop
     * @param cropName Name of the crop
     * @return List of MarketPrice records for the specified crop
     */
    @Query("SELECT * FROM market_prices WHERE crop_name = :cropName AND is_active = 1 ORDER BY price_date DESC")
    List<MarketPrice> getMarketPricesByCrop(String cropName);

    /**
     * Get market prices for a specific district
     * @param district Name of the district
     * @return List of MarketPrice records for the specified district
     */
    @Query("SELECT * FROM market_prices WHERE district = :district AND is_active = 1 ORDER BY crop_name ASC")
    List<MarketPrice> getMarketPricesByDistrict(String district);

    /**
     * Get market prices for a specific market
     * @param marketName Name of the market
     * @return List of MarketPrice records for the specified market
     */
    @Query("SELECT * FROM market_prices WHERE market_name = :marketName AND is_active = 1 ORDER BY crop_name ASC")
    List<MarketPrice> getMarketPricesByMarket(String marketName);

    /**
     * Get latest price for a specific crop in a specific market
     * @param cropName Name of the crop
     * @param marketName Name of the market
     * @return Latest MarketPrice record
     */
    @Query("SELECT * FROM market_prices WHERE crop_name = :cropName AND market_name = :marketName AND is_active = 1 ORDER BY price_date DESC LIMIT 1")
    MarketPrice getLatestPrice(String cropName, String marketName);

    /**
     * Get market prices with upward trend
     * @return List of MarketPrice records with upward trend
     */
    @Query("SELECT * FROM market_prices WHERE trend = 'UP' AND is_active = 1 ORDER BY change_percentage DESC")
    List<MarketPrice> getPricesWithUpTrend();

    /**
     * Get market prices with downward trend
     * @return List of MarketPrice records with downward trend
     */
    @Query("SELECT * FROM market_prices WHERE trend = 'DOWN' AND is_active = 1 ORDER BY change_percentage ASC")
    List<MarketPrice> getPricesWithDownTrend();

    /**
     * Get market prices within a price range
     * @param minPrice Minimum price per kg
     * @param maxPrice Maximum price per kg
     * @return List of MarketPrice records within the price range
     */
    @Query("SELECT * FROM market_prices WHERE price_per_kg BETWEEN :minPrice AND :maxPrice AND is_active = 1 ORDER BY price_per_kg ASC")
    List<MarketPrice> getPricesInRange(double minPrice, double maxPrice);

    /**
     * Get organic crop prices
     * @return List of organic MarketPrice records
     */
    @Query("SELECT * FROM market_prices WHERE is_organic = 1 AND is_active = 1 ORDER BY crop_name ASC")
    List<MarketPrice> getOrganicPrices();

    /**
     * Get prices by quality grade
     * @param qualityGrade Quality grade (A, B, C)
     * @return List of MarketPrice records for the specified grade
     */
    @Query("SELECT * FROM market_prices WHERE quality_grade = :qualityGrade AND is_active = 1 ORDER BY crop_name ASC")
    List<MarketPrice> getPricesByGrade(String qualityGrade);

    /**
     * Get recent price updates (last 24 hours)
     * @param timestamp Timestamp for 24 hours ago
     * @return List of recently updated MarketPrice records
     */
    @Query("SELECT * FROM market_prices WHERE last_updated > :timestamp AND is_active = 1 ORDER BY last_updated DESC")
    List<MarketPrice> getRecentUpdates(long timestamp);

    /**
     * Get unique districts
     * @return List of unique district names
     */
    @Query("SELECT DISTINCT district FROM market_prices WHERE is_active = 1 ORDER BY district ASC")
    List<String> getUniqueDistricts();

    /**
     * Get unique markets
     * @return List of unique market names
     */
    @Query("SELECT DISTINCT market_name FROM market_prices WHERE is_active = 1 ORDER BY market_name ASC")
    List<String> getUniqueMarkets();

    /**
     * Get unique crop names
     * @return List of unique crop names
     */
    @Query("SELECT DISTINCT crop_name FROM market_prices WHERE is_active = 1 ORDER BY crop_name ASC")
    List<String> getUniqueCrops();

    /**
     * Get highest priced crops
     * @param limit Number of records to return
     * @return List of highest priced crops
     */
    @Query("SELECT * FROM market_prices WHERE is_active = 1 ORDER BY price_per_kg DESC LIMIT :limit")
    List<MarketPrice> getHighestPricedCrops(int limit);

    /**
     * Get lowest priced crops
     * @param limit Number of records to return
     * @return List of lowest priced crops
     */
    @Query("SELECT * FROM market_prices WHERE is_active = 1 ORDER BY price_per_kg ASC LIMIT :limit")
    List<MarketPrice> getLowestPricedCrops(int limit);

    /**
     * Search market prices by crop name (partial match)
     * @param searchQuery Search query
     * @return List of matching MarketPrice records
     */
    @Query("SELECT * FROM market_prices WHERE (crop_name LIKE '%' || :searchQuery || '%' OR crop_name_hi LIKE '%' || :searchQuery || '%' OR crop_name_ml LIKE '%' || :searchQuery || '%') AND is_active = 1 ORDER BY crop_name ASC")
    List<MarketPrice> searchMarketPrices(String searchQuery);

    /**
     * Get a specific market price record by ID
     * @param id The ID of the market price record
     * @return MarketPrice object or null if not found
     */
    @Query("SELECT * FROM market_prices WHERE id = :id")
    MarketPrice getMarketPriceById(int id);

    /**
     * Delete old price records (older than specified timestamp)
     * @param timestamp Cutoff timestamp
     * @return Number of rows deleted
     */
    @Query("DELETE FROM market_prices WHERE last_updated < :timestamp")
    int deleteOldRecords(long timestamp);

    /**
     * Delete all market price records
     * @return Number of rows deleted
     */
    @Query("DELETE FROM market_prices")
    int deleteAllMarketPrices();

    /**
     * Get price count for a specific crop
     * @param cropName Name of the crop
     * @return Count of price records for the crop
     */
    @Query("SELECT COUNT(*) FROM market_prices WHERE crop_name = :cropName AND is_active = 1")
    int getPriceCountByCrop(String cropName);

    /**
     * Get average price for a specific crop across all markets
     * @param cropName Name of the crop
     * @return Average price per kg
     */
    @Query("SELECT AVG(price_per_kg) FROM market_prices WHERE crop_name = :cropName AND is_active = 1")
    double getAveragePrice(String cropName);

    /**
     * Deactivate old records for a crop and market (for price updates)
     * @param cropName Name of the crop
     * @param marketName Name of the market
     * @return Number of rows updated
     */
    @Query("UPDATE market_prices SET is_active = 0 WHERE crop_name = :cropName AND market_name = :marketName AND is_active = 1")
    int deactivateOldRecords(String cropName, String marketName);
}