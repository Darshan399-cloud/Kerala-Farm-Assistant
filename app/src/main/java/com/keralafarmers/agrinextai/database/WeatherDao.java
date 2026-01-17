package com.keralafarmers.agrinextai.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.keralafarmers.agrinextai.models.Weather;

import java.util.List;

/**
 * Data Access Object for Weather entity
 */
@Dao
public interface WeatherDao {
    
    /**
     * Insert weather data
     * @param weather Weather object to insert
     * @return The ID of the inserted weather data
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWeather(Weather weather);
    
    /**
     * Insert multiple weather records
     * @param weatherList List of weather objects
     * @return List of inserted IDs
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertWeatherList(List<Weather> weatherList);
    
    /**
     * Update weather data
     * @param weather Weather object to update
     * @return Number of rows affected
     */
    @Update
    int updateWeather(Weather weather);
    
    /**
     * Delete weather data
     * @param weather Weather object to delete
     * @return Number of rows affected
     */
    @Delete
    int deleteWeather(Weather weather);
    
    /**
     * Get weather data by date
     * @param date Date in YYYY-MM-DD format
     * @return Weather object if found
     */
    @Query("SELECT * FROM weather_data WHERE date = :date LIMIT 1")
    Weather getWeatherByDate(String date);
    
    /**
     * Get weather data for date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of weather data
     */
    @Query("SELECT * FROM weather_data WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    List<Weather> getWeatherByDateRange(String startDate, String endDate);
    
    /**
     * Get past 5 days weather data
     * @param currentDate Current date
     * @return List of weather data
     */
    @Query("SELECT * FROM weather_data WHERE date <= :currentDate ORDER BY date DESC LIMIT 5")
    List<Weather> getPast5DaysWeather(String currentDate);
    
    /**
     * Get future 5 days weather data
     * @param currentDate Current date
     * @return List of weather data
     */
    @Query("SELECT * FROM weather_data WHERE date > :currentDate ORDER BY date ASC LIMIT 5")
    List<Weather> getFuture5DaysWeather(String currentDate);
    
    /**
     * Get all weather data ordered by date
     * @return List of all weather data
     */
    @Query("SELECT * FROM weather_data ORDER BY date DESC")
    List<Weather> getAllWeather();
    
    /**
     * Get latest weather data
     * @return Latest weather record
     */
    @Query("SELECT * FROM weather_data ORDER BY timestamp DESC LIMIT 1")
    Weather getLatestWeather();
    
    /**
     * Get weather data by location
     * @param location Location name
     * @return List of weather data for location
     */
    @Query("SELECT * FROM weather_data WHERE location = :location ORDER BY date DESC")
    List<Weather> getWeatherByLocation(String location);
    
    /**
     * Delete old weather data (older than specified days)
     * @param timestamp Cutoff timestamp
     * @return Number of rows deleted
     */
    @Query("DELETE FROM weather_data WHERE timestamp < :timestamp")
    int deleteOldWeatherData(long timestamp);
    
    /**
     * Get weather count
     * @return Total number of weather records
     */
    @Query("SELECT COUNT(*) FROM weather_data")
    int getWeatherCount();
    
    /**
     * Clear all weather data
     * @return Number of rows deleted
     */
    @Query("DELETE FROM weather_data")
    int clearAllWeatherData();
}