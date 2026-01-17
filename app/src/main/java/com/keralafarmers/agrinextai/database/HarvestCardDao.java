package com.keralafarmers.agrinextai.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.keralafarmers.agrinextai.models.HarvestCard;

import java.util.List;

/**
 * Data Access Object (DAO) for HarvestCard entity
 * Provides database operations for harvest traceability data
 */
@Dao
public interface HarvestCardDao {

    /**
     * Insert a new harvest card
     * @param harvestCard HarvestCard object to insert
     * @return The row ID of the inserted record
     */
    @Insert
    long insertHarvestCard(HarvestCard harvestCard);

    /**
     * Insert multiple harvest cards
     * @param harvestCards List of HarvestCard objects to insert
     * @return List of row IDs
     */
    @Insert
    List<Long> insertHarvestCards(List<HarvestCard> harvestCards);

    /**
     * Update an existing harvest card
     * @param harvestCard HarvestCard object with updated data
     * @return Number of rows affected
     */
    @Update
    int updateHarvestCard(HarvestCard harvestCard);

    /**
     * Delete a harvest card
     * @param harvestCard HarvestCard object to delete
     * @return Number of rows deleted
     */
    @Delete
    int deleteHarvestCard(HarvestCard harvestCard);

    /**
     * Get all active harvest cards for a user ordered by harvest date (most recent first)
     * @param userId User ID
     * @return List of HarvestCard records
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsByUser(int userId);

    /**
     * Get harvest card by card ID (QR code identifier)
     * @param cardId Unique card identifier
     * @return HarvestCard object or null if not found
     */
    @Query("SELECT * FROM harvest_cards WHERE card_id = :cardId AND is_active = 1")
    HarvestCard getHarvestCardByCardId(String cardId);

    /**
     * Get harvest cards by crop name
     * @param userId User ID
     * @param cropName Name of the crop
     * @return List of HarvestCard records for the specified crop
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND crop_name = :cropName AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsByCrop(int userId, String cropName);

    /**
     * Get harvest cards by verification status
     * @param userId User ID
     * @param verificationStatus Verification status
     * @return List of HarvestCard records with specified verification status
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND verification_status = :verificationStatus AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsByVerificationStatus(int userId, String verificationStatus);

    /**
     * Get organic harvest cards
     * @param userId User ID
     * @return List of organic HarvestCard records
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND is_organic = 1 AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getOrganicHarvestCards(int userId);

    /**
     * Get harvest cards by quality grade
     * @param userId User ID
     * @param qualityGrade Quality grade (A, B, C)
     * @return List of HarvestCard records for the specified grade
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND quality_grade = :qualityGrade AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsByGrade(int userId, String qualityGrade);

    /**
     * Get harvest cards within a date range
     * @param userId User ID
     * @param startDate Start date timestamp
     * @param endDate End date timestamp
     * @return List of HarvestCard records within the date range
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND harvest_date BETWEEN :startDate AND :endDate AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsInDateRange(int userId, long startDate, long endDate);

    /**
     * Get recent harvest cards (last 30 days)
     * @param userId User ID
     * @param thirtyDaysAgo Timestamp for 30 days ago
     * @return List of recent HarvestCard records
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND harvest_date > :thirtyDaysAgo AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getRecentHarvestCards(int userId, long thirtyDaysAgo);

    /**
     * Get harvest cards with high revenue
     * @param userId User ID
     * @param minRevenue Minimum revenue threshold
     * @return List of high revenue HarvestCard records
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND total_revenue >= :minRevenue AND is_active = 1 ORDER BY total_revenue DESC")
    List<HarvestCard> getHighRevenueHarvestCards(int userId, double minRevenue);

    /**
     * Get harvest cards by farm location
     * @param userId User ID
     * @param farmLocation Farm location
     * @return List of HarvestCard records for the specified location
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND farm_location = :farmLocation AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsByLocation(int userId, String farmLocation);

    /**
     * Search harvest cards by crop name or variety
     * @param userId User ID
     * @param searchQuery Search query
     * @return List of matching HarvestCard records
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND (crop_name LIKE '%' || :searchQuery || '%' OR crop_variety LIKE '%' || :searchQuery || '%') AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> searchHarvestCards(int userId, String searchQuery);

    /**
     * Get unique crop names for a user
     * @param userId User ID
     * @return List of unique crop names
     */
    @Query("SELECT DISTINCT crop_name FROM harvest_cards WHERE user_id = :userId AND is_active = 1 ORDER BY crop_name ASC")
    List<String> getUniqueCrops(int userId);

    /**
     * Get unique farm locations for a user
     * @param userId User ID
     * @return List of unique farm locations
     */
    @Query("SELECT DISTINCT farm_location FROM harvest_cards WHERE user_id = :userId AND is_active = 1 ORDER BY farm_location ASC")
    List<String> getUniqueLocations(int userId);

    /**
     * Get harvest card count for a user
     * @param userId User ID
     * @return Count of harvest cards
     */
    @Query("SELECT COUNT(*) FROM harvest_cards WHERE user_id = :userId AND is_active = 1")
    int getHarvestCardCount(int userId);

    /**
     * Get harvest card count by crop
     * @param userId User ID
     * @param cropName Name of the crop
     * @return Count of harvest cards for the crop
     */
    @Query("SELECT COUNT(*) FROM harvest_cards WHERE user_id = :userId AND crop_name = :cropName AND is_active = 1")
    int getHarvestCardCountByCrop(int userId, String cropName);

    /**
     * Get total quantity harvested for a user
     * @param userId User ID
     * @return Total quantity harvested across all cards
     */
    @Query("SELECT SUM(quantity_harvested) FROM harvest_cards WHERE user_id = :userId AND is_active = 1")
    double getTotalQuantityHarvested(int userId);

    /**
     * Get total revenue for a user
     * @param userId User ID
     * @return Total revenue across all harvest cards
     */
    @Query("SELECT SUM(total_revenue) FROM harvest_cards WHERE user_id = :userId AND is_active = 1")
    double getTotalRevenue(int userId);

    /**
     * Get average profit margin for a user
     * @param userId User ID
     * @return Average profit margin percentage
     */
    @Query("SELECT AVG(profit_margin) FROM harvest_cards WHERE user_id = :userId AND profit_margin > 0 AND is_active = 1")
    double getAverageProfitMargin(int userId);

    /**
     * Get harvest cards with QR code data
     * @param userId User ID
     * @return List of HarvestCard records that have QR codes
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND qr_code_data IS NOT NULL AND qr_code_data != '' AND is_active = 1 ORDER BY harvest_date DESC")
    List<HarvestCard> getHarvestCardsWithQR(int userId);

    /**
     * Get harvest cards by carbon footprint range
     * @param userId User ID
     * @param maxFootprint Maximum carbon footprint threshold
     * @return List of environmentally friendly HarvestCard records
     */
    @Query("SELECT * FROM harvest_cards WHERE user_id = :userId AND carbon_footprint <= :maxFootprint AND is_active = 1 ORDER BY carbon_footprint ASC")
    List<HarvestCard> getEcoFriendlyHarvestCards(int userId, double maxFootprint);

    /**
     * Get a specific harvest card by ID
     * @param id Harvest card ID
     * @return HarvestCard object
     */
    @Query("SELECT * FROM harvest_cards WHERE id = :id")
    HarvestCard getHarvestCardById(int id);

    /**
     * Update verification status of a harvest card
     * @param cardId Card ID
     * @param verificationStatus New verification status
     * @return Number of rows updated
     */
    @Query("UPDATE harvest_cards SET verification_status = :verificationStatus, updated_date = :updatedDate WHERE card_id = :cardId")
    int updateVerificationStatus(String cardId, String verificationStatus, long updatedDate);

    /**
     * Update QR code data for a harvest card
     * @param cardId Card ID
     * @param qrCodeData Base64 encoded QR code data
     * @return Number of rows updated
     */
    @Query("UPDATE harvest_cards SET qr_code_data = :qrCodeData, updated_date = :updatedDate WHERE card_id = :cardId")
    int updateQRCodeData(String cardId, String qrCodeData, long updatedDate);

    /**
     * Delete old harvest cards (older than specified timestamp)
     * @param timestamp Cutoff timestamp
     * @return Number of rows deleted
     */
    @Query("DELETE FROM harvest_cards WHERE created_date < :timestamp")
    int deleteOldHarvestCards(long timestamp);

    /**
     * Deactivate harvest cards instead of deleting
     * @param userId User ID
     * @param cardIds List of card IDs to deactivate
     * @return Number of rows updated
     */
    @Query("UPDATE harvest_cards SET is_active = 0, updated_date = :updatedDate WHERE user_id = :userId AND card_id IN (:cardIds)")
    int deactivateHarvestCards(int userId, List<String> cardIds, long updatedDate);

    /**
     * Get harvest statistics for dashboard
     * @param userId User ID
     * @return Count, total quantity, total revenue as a custom query result
     */
    @Query("SELECT COUNT(*) as count, SUM(quantity_harvested) as totalQuantity, SUM(total_revenue) as totalRevenue FROM harvest_cards WHERE user_id = :userId AND is_active = 1")
    HarvestStats getHarvestStats(int userId);

    /**
     * Get monthly harvest summary for a specific year
     * @param userId User ID
     * @param startYear Start of year timestamp
     * @param endYear End of year timestamp
     * @return List of monthly harvest data
     */
    @Query("SELECT strftime('%m', datetime(harvest_date/1000, 'unixepoch')) as month, COUNT(*) as count, SUM(quantity_harvested) as quantity FROM harvest_cards WHERE user_id = :userId AND harvest_date BETWEEN :startYear AND :endYear AND is_active = 1 GROUP BY month ORDER BY month")
    List<MonthlyHarvestData> getMonthlyHarvestSummary(int userId, long startYear, long endYear);

    // Inner classes for query results
    class HarvestStats {
        public int count;
        public double totalQuantity;
        public double totalRevenue;
    }

    class MonthlyHarvestData {
        public String month;
        public int count;
        public double quantity;
    }
}