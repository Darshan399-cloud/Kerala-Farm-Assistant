package com.keralafarmers.agrinextai.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.keralafarmers.agrinextai.models.Chat;

import java.util.List;

/**
 * Data Access Object (DAO) for Chat entity
 * Provides database operations for chatbot conversation history
 */
@Dao
public interface ChatDao {

    /**
     * Insert a new chat message
     * @param chat Chat object to insert
     * @return The row ID of the inserted record
     */
    @Insert
    long insertChat(Chat chat);

    /**
     * Insert multiple chat messages
     * @param chats List of Chat objects to insert
     * @return List of row IDs
     */
    @Insert
    List<Long> insertChats(List<Chat> chats);

    /**
     * Update an existing chat record
     * @param chat Chat object with updated data
     * @return Number of rows affected
     */
    @Update
    int updateChat(Chat chat);

    /**
     * Delete a chat record
     * @param chat Chat object to delete
     * @return Number of rows deleted
     */
    @Delete
    int deleteChat(Chat chat);

    /**
     * Get all chat messages for a user ordered by timestamp
     * @param userId User ID
     * @return List of Chat records
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId ORDER BY timestamp ASC")
    List<Chat> getChatsByUser(int userId);

    /**
     * Get chat messages for a specific session
     * @param sessionId Session ID
     * @return List of Chat records for the session
     */
    @Query("SELECT * FROM chats WHERE session_id = :sessionId ORDER BY timestamp ASC")
    List<Chat> getChatsBySession(String sessionId);

    /**
     * Get chat messages by category
     * @param userId User ID
     * @param category Message category
     * @return List of Chat records for the category
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND category = :category ORDER BY timestamp DESC")
    List<Chat> getChatsByCategory(int userId, String category);

    /**
     * Get chat messages by language
     * @param userId User ID
     * @param language Language code
     * @return List of Chat records in the specified language
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND language = :language ORDER BY timestamp DESC")
    List<Chat> getChatsByLanguage(int userId, String language);

    /**
     * Get recent chat messages (last 24 hours)
     * @param userId User ID
     * @param timestamp Timestamp for 24 hours ago
     * @return List of recent Chat records
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND timestamp > :timestamp ORDER BY timestamp ASC")
    List<Chat> getRecentChats(int userId, long timestamp);

    /**
     * Get user messages only
     * @param userId User ID
     * @return List of user Chat records
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND message_type = 'USER' ORDER BY timestamp DESC")
    List<Chat> getUserMessages(int userId);

    /**
     * Get bot responses only
     * @param userId User ID
     * @return List of bot Chat records
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND message_type = 'BOT' ORDER BY timestamp DESC")
    List<Chat> getBotMessages(int userId);

    /**
     * Search chat messages by content
     * @param userId User ID
     * @param searchQuery Search query
     * @return List of matching Chat records
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND (message LIKE '%' || :searchQuery || '%' OR response LIKE '%' || :searchQuery || '%') ORDER BY timestamp DESC")
    List<Chat> searchChats(int userId, String searchQuery);

    /**
     * Get helpful messages (user feedback)
     * @param userId User ID
     * @return List of Chat records marked as helpful
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND is_helpful = 1 ORDER BY timestamp DESC")
    List<Chat> getHelpfulMessages(int userId);

    /**
     * Get unhelpful messages (user feedback)
     * @param userId User ID
     * @return List of Chat records marked as unhelpful
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND is_helpful = 0 ORDER BY timestamp DESC")
    List<Chat> getUnhelpfulMessages(int userId);

    /**
     * Get messages with high confidence
     * @param userId User ID
     * @param minConfidence Minimum confidence score
     * @return List of high confidence Chat records
     */
    @Query("SELECT * FROM chats WHERE user_id = :userId AND confidence_score >= :minConfidence ORDER BY confidence_score DESC")
    List<Chat> getHighConfidenceMessages(int userId, double minConfidence);

    /**
     * Get unique session IDs for a user
     * @param userId User ID
     * @return List of unique session IDs
     */
    @Query("SELECT DISTINCT session_id FROM chats WHERE user_id = :userId GROUP BY session_id ORDER BY MAX(timestamp) DESC")
    List<String> getSessionIds(int userId);

    /**
     * Get unique categories used by a user
     * @param userId User ID
     * @return List of unique categories
     */
    @Query("SELECT DISTINCT category FROM chats WHERE user_id = :userId AND category IS NOT NULL ORDER BY category ASC")
    List<String> getUniqueCategories(int userId);

    /**
     * Get chat count for a user
     * @param userId User ID
     * @return Count of chat messages
     */
    @Query("SELECT COUNT(*) FROM chats WHERE user_id = :userId")
    int getChatCountByUser(int userId);

    /**
     * Get chat count by category
     * @param userId User ID
     * @param category Message category
     * @return Count of messages in the category
     */
    @Query("SELECT COUNT(*) FROM chats WHERE user_id = :userId AND category = :category")
    int getChatCountByCategory(int userId, String category);

    /**
     * Get the last message in a session
     * @param sessionId Session ID
     * @return Latest Chat record in the session
     */
    @Query("SELECT * FROM chats WHERE session_id = :sessionId ORDER BY timestamp DESC LIMIT 1")
    Chat getLastMessageInSession(String sessionId);

    /**
     * Get a specific chat record by ID
     * @param id Chat ID
     * @return Chat record
     */
    @Query("SELECT * FROM chats WHERE id = :id")
    Chat getChatById(int id);

    /**
     * Delete old chat messages (older than specified timestamp)
     * @param timestamp Cutoff timestamp
     * @return Number of rows deleted
     */
    @Query("DELETE FROM chats WHERE timestamp < :timestamp")
    int deleteOldChats(long timestamp);

    /**
     * Delete all chats for a user
     * @param userId User ID
     * @return Number of rows deleted
     */
    @Query("DELETE FROM chats WHERE user_id = :userId")
    int deleteAllChatsByUser(int userId);

    /**
     * Delete chats for a specific session
     * @param sessionId Session ID
     * @return Number of rows deleted
     */
    @Query("DELETE FROM chats WHERE session_id = :sessionId")
    int deleteChatsBySession(String sessionId);

    /**
     * Update feedback for a chat message
     * @param chatId Chat ID
     * @param isHelpful Whether the message was helpful
     * @return Number of rows updated
     */
    @Query("UPDATE chats SET is_helpful = :isHelpful WHERE id = :chatId")
    int updateFeedback(int chatId, boolean isHelpful);

    /**
     * Get average confidence score for a user
     * @param userId User ID
     * @return Average confidence score
     */
    @Query("SELECT AVG(confidence_score) FROM chats WHERE user_id = :userId AND message_type = 'BOT'")
    double getAverageConfidenceScore(int userId);

    /**
     * Get most used categories
     * @param userId User ID
     * @param limit Number of categories to return
     * @return List of most used categories
     */
    @Query("SELECT category FROM chats WHERE user_id = :userId AND category IS NOT NULL GROUP BY category ORDER BY COUNT(*) DESC LIMIT :limit")
    List<String> getMostUsedCategories(int userId, int limit);
}