package com.keralafarmers.agrinextai.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.keralafarmers.agrinextai.models.User;
import com.keralafarmers.agrinextai.models.Weather;
import com.keralafarmers.agrinextai.models.PlantDisease;
import com.keralafarmers.agrinextai.models.MarketPrice;
import com.keralafarmers.agrinextai.models.Chat;
import com.keralafarmers.agrinextai.models.HarvestCard;

/**
 * Room Database class for Kerala Farm Assistant App
 * Contains all the entities and provides access to DAOs
 */
@Database(
    entities = {User.class, Weather.class, PlantDisease.class, MarketPrice.class, Chat.class, HarvestCard.class},
    version = 6,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "kerala_farm_assistant_db";
    private static volatile AppDatabase INSTANCE;
    
    /**
     * Get the User DAO
     * @return UserDao instance
     */
    public abstract UserDao userDao();
    
    /**
     * Get the Weather DAO
     * @return WeatherDao instance
     */
    public abstract WeatherDao weatherDao();
    
    /**
     * Get the PlantDisease DAO
     * @return PlantDiseaseDao instance
     */
    public abstract PlantDiseaseDao plantDiseaseDao();
    
    /**
     * Get the MarketPrice DAO
     * @return MarketPriceDao instance
     */
    public abstract MarketPriceDao marketPriceDao();
    
    /**
     * Get the Chat DAO
     * @return ChatDao instance
     */
    public abstract ChatDao chatDao();
    
    /**
     * Get the HarvestCard DAO
     * @return HarvestCardDao instance
     */
    public abstract HarvestCardDao harvestCardDao();
    
    /**
     * Get singleton instance of the database
     * @param context Application context
     * @return AppDatabase instance
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    .addCallback(roomCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * Database callback for initialization
     */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Database created callback
            // You can add initial data here if needed
        }
        
        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Database opened callback
        }
    };
    
    /**
     * Close the database instance
     */
    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
    
    /**
     * Clear all tables in the database
     */
    public void clearAllTables() {
        // Implementation will be handled by Room automatically
        // This method can be used for clearing all tables if needed
    }
}