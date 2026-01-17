package com.keralafarmers.agrinextai.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.keralafarmers.agrinextai.models.PlantDisease;

import java.util.List;

/**
 * Data Access Object for PlantDisease entity
 */
@Dao
public interface PlantDiseaseDao {
    
    /**
     * Insert plant disease detection result
     * @param plantDisease PlantDisease object to insert
     * @return The ID of the inserted record
     */
    @Insert
    long insertPlantDisease(PlantDisease plantDisease);
    
    /**
     * Update plant disease record
     * @param plantDisease PlantDisease object to update
     * @return Number of rows affected
     */
    @Update
    int updatePlantDisease(PlantDisease plantDisease);
    
    /**
     * Delete plant disease record
     * @param plantDisease PlantDisease object to delete
     * @return Number of rows affected
     */
    @Delete
    int deletePlantDisease(PlantDisease plantDisease);
    
    /**
     * Get all plant disease detections for a user
     * @param userId User ID
     * @return List of plant disease records
     */
    @Query("SELECT * FROM plant_diseases WHERE userId = :userId ORDER BY detectionDate DESC")
    List<PlantDisease> getPlantDiseasesByUser(int userId);
    
    /**
     * Get plant disease by ID
     * @param diseaseId Disease ID
     * @return PlantDisease object
     */
    @Query("SELECT * FROM plant_diseases WHERE id = :diseaseId")
    PlantDisease getPlantDiseaseById(int diseaseId);
    
    /**
     * Get recent plant disease detections (last 30 days)
     * @param userId User ID
     * @param timestamp Timestamp 30 days ago
     * @return List of recent detections
     */
    @Query("SELECT * FROM plant_diseases WHERE userId = :userId AND detectionDate > :timestamp ORDER BY detectionDate DESC")
    List<PlantDisease> getRecentPlantDiseases(int userId, long timestamp);
    
    /**
     * Get recent plant disease detections (last 30 days) - without userId filter
     * @param timestamp Timestamp 30 days ago
     * @return List of recent detections
     */
    @Query("SELECT * FROM plant_diseases WHERE detectionDate > :timestamp ORDER BY detectionDate DESC")
    List<PlantDisease> getRecentPlantDiseases(long timestamp);
    
    /**
     * Get plant diseases by crop type
     * @param userId User ID
     * @param cropType Crop type
     * @return List of diseases for specific crop
     */
    @Query("SELECT * FROM plant_diseases WHERE userId = :userId AND cropType = :cropType ORDER BY detectionDate DESC")
    List<PlantDisease> getPlantDiseasesByCrop(int userId, String cropType);
    
    /**
     * Get plant diseases by crop type (without userId filter)
     * @param cropType Crop type
     * @return List of diseases for specific crop
     */
    @Query("SELECT * FROM plant_diseases WHERE cropType = :cropType ORDER BY detectionDate DESC")
    List<PlantDisease> getPlantDiseasesByCrop(String cropType);
    
    /**
     * Get plant diseases with high confidence (>= 0.8)
     * @param userId User ID
     * @return List of high confidence detections
     */
    @Query("SELECT * FROM plant_diseases WHERE userId = :userId AND confidenceLevel >= 0.8 ORDER BY detectionDate DESC")
    List<PlantDisease> getHighConfidenceDiseases(int userId);
    
    /**
     * Get plant diseases with high confidence - without userId filter
     * @param confidenceThreshold Minimum confidence threshold
     * @return List of high confidence detections
     */
    @Query("SELECT * FROM plant_diseases WHERE confidenceLevel >= :confidenceThreshold ORDER BY confidenceLevel DESC, detectionDate DESC")
    List<PlantDisease> getHighConfidenceDiseases(float confidenceThreshold);
    
    /**
     * Delete old plant disease records (older than specified timestamp)
     * @param timestamp Cutoff timestamp
     * @return Number of rows deleted
     */
    @Query("DELETE FROM plant_diseases WHERE detectionDate < :timestamp")
    int deleteOldRecords(long timestamp);
    
    /**
     * Get disease detection count for user
     * @param userId User ID
     * @return Count of detections
     */
    @Query("SELECT COUNT(*) FROM plant_diseases WHERE userId = :userId")
    int getDiseaseCountByUser(int userId);
    
    /**
     * Get all plant diseases (for admin purposes)
     * @return List of all plant disease records
     */
    @Query("SELECT * FROM plant_diseases ORDER BY detectionDate DESC")
    List<PlantDisease> getAllPlantDiseases();
}