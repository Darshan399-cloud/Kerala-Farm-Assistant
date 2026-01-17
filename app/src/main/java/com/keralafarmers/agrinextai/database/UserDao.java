package com.keralafarmers.agrinextai.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.keralafarmers.agrinextai.models.User;

import java.util.List;

/**
 * Data Access Object for User entity
 * Handles all database operations for User table
 */
@Dao
public interface UserDao {
    
    /**
     * Insert a new user
     * @param user User object to insert
     * @return The ID of the inserted user
     */
    @Insert
    long insertUser(User user);
    
    /**
     * Update an existing user
     * @param user User object to update
     * @return Number of rows affected
     */
    @Update
    int updateUser(User user);
    
    /**
     * Delete a user
     * @param user User object to delete
     * @return Number of rows affected
     */
    @Delete
    int deleteUser(User user);
    
    /**
     * Get user by email and password hash (for login)
     * @param email User email
     * @param passwordHash Hashed password
     * @return User object if found, null otherwise
     */
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash AND isActive = 1")
    User getUserByEmailAndPassword(String email, String passwordHash);
    
    /**
     * Get user by email (to check if email exists)
     * @param email User email
     * @return User object if found, null otherwise
     */
    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);
    
    /**
     * Get all users (admin function)
     * @return List of all users
     */
    @Query("SELECT * FROM users")
    List<User> getAllUsers();
    
    /**
     * Get active users only
     * @return List of active users
     */
    @Query("SELECT * FROM users WHERE isActive = 1")
    List<User> getActiveUsers();
    
    /**
     * Update user's last login time
     * @param userId User ID
     * @param timestamp Login timestamp
     * @return Number of rows affected
     */
    @Query("UPDATE users SET lastLoginAt = :timestamp WHERE id = :userId")
    int updateLastLogin(int userId, long timestamp);
    
    /**
     * Update user's preferred language
     * @param userId User ID
     * @param language Language code
     * @return Number of rows affected
     */
    @Query("UPDATE users SET preferredLanguage = :language WHERE id = :userId")
    int updateUserLanguage(int userId, String language);
    
    /**
     * Update user's password hash
     * @param userId User ID
     * @param newPasswordHash New password hash
     * @return Number of rows affected
     */
    @Query("UPDATE users SET passwordHash = :newPasswordHash WHERE id = :userId")
    int updateUserPassword(int userId, String newPasswordHash);
    
    /**
     * Deactivate user account
     * @param userId User ID
     * @return Number of rows affected
     */
    @Query("UPDATE users SET isActive = 0 WHERE id = :userId")
    int deactivateUser(int userId);
    
    /**
     * Reactivate user account
     * @param userId User ID
     * @return Number of rows affected
     */
    @Query("UPDATE users SET isActive = 1 WHERE id = :userId")
    int reactivateUser(int userId);
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return Count of users with this email
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);
    
    /**
     * Get user count for statistics
     * @return Total number of users
     */
    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();
}