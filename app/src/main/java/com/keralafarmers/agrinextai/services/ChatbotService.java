package com.keralafarmers.agrinextai.services;

import android.content.Context;
import android.util.Log;

import com.keralafarmers.agrinextai.database.AppDatabase;
import com.keralafarmers.agrinextai.database.ChatDao;
import com.keralafarmers.agrinextai.models.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multilingual Chatbot Service for farming assistance
 * Provides intelligent responses to farming queries in English, Hindi, and Malayalam
 */
public class ChatbotService {
    private static final String TAG = "ChatbotService";
    private ChatDao chatDao;
    private ExecutorService executorService;
    private Context context;
    private Random random;

    // Knowledge base for farming queries
    private static final Map<String, List<FarmingKnowledge>> KNOWLEDGE_BASE = new HashMap<>();

    static {
        initializeKnowledgeBase();
    }

    /**
     * Constructor for ChatbotService
     * @param context Application context
     */
    public ChatbotService(Context context) {
        this.context = context;
        AppDatabase database = AppDatabase.getInstance(context);
        this.chatDao = database.chatDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.random = new Random();
    }

    /**
     * Simplified send message for ChatbotActivity
     * @param userId User ID
     * @param message User message
     * @param language Language code
     * @param callback Callback to receive response
     */
    public void sendMessage(int userId, String message, String language, ChatResponseCallback callback) {
        executorService.execute(() -> {
            try {
                // Generate response directly
                String response = generateResponse(message, language);
                if (callback != null) {
                    callback.onResponse(response);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error generating response", e);
                if (callback != null) {
                    callback.onError("Failed to generate response: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * Send message to chatbot and get response
     * @param userId User ID
     * @param message User message
     * @param language Language code ("en", "hi", "ml")
     * @param sessionId Session ID (null for new session)
     * @param callback Callback to receive response
     */
    public void sendMessage(int userId, String message, String language, String sessionId, ChatCallback callback) {
        executorService.execute(() -> {
            try {
                // Create session ID if not provided
                String finalSessionId = sessionId;
                if (finalSessionId == null || finalSessionId.isEmpty()) {
                    finalSessionId = UUID.randomUUID().toString();
                }

                // Save user message
                Chat userMessage = new Chat(userId, finalSessionId, message, Chat.MESSAGE_TYPE_USER, language);
                long userMessageId = chatDao.insertChat(userMessage);
                userMessage.setId((int) userMessageId);

                // Generate bot response
                String response = generateResponse(message, language);
                String category = categorizeMessage(message);
                double confidence = calculateConfidence(message, response);

                // Create bot response
                Chat botResponse = new Chat(userId, finalSessionId, response, Chat.MESSAGE_TYPE_BOT, language);
                botResponse.setResponse(response);
                botResponse.setCategory(category);
                botResponse.setConfidenceScore(confidence);
                botResponse.setContext(message); // Store user question as context

                long botMessageId = chatDao.insertChat(botResponse);
                botResponse.setId((int) botMessageId);

                if (callback != null) {
                    callback.onSuccess(userMessage, botResponse, finalSessionId);
                }

                Log.d(TAG, "Message processed successfully. Response: " + response);

            } catch (Exception e) {
                Log.e(TAG, "Error processing message", e);
                if (callback != null) {
                    callback.onError("Failed to process message: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Get conversation history for a user
     * @param userId User ID
     * @param callback Callback to receive chat history
     */
    public void getChatHistory(int userId, ChatHistoryCallback callback) {
        executorService.execute(() -> {
            try {
                List<Chat> chatHistory = chatDao.getChatsByUser(userId);
                if (callback != null) {
                    callback.onSuccess(chatHistory);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting chat history", e);
                if (callback != null) {
                    callback.onError("Failed to get chat history");
                }
            }
        });
    }

    /**
     * Get conversation for a specific session
     * @param sessionId Session ID
     * @param callback Callback to receive session chat
     */
    public void getSessionChat(String sessionId, ChatHistoryCallback callback) {
        executorService.execute(() -> {
            try {
                List<Chat> sessionChat = chatDao.getChatsBySession(sessionId);
                if (callback != null) {
                    callback.onSuccess(sessionChat);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting session chat", e);
                if (callback != null) {
                    callback.onError("Failed to get session chat");
                }
            }
        });
    }

    /**
     * Provide feedback on bot response
     * @param chatId Chat ID of bot response
     * @param isHelpful Whether the response was helpful
     * @param callback Callback for feedback result
     */
    public void provideFeedback(int chatId, boolean isHelpful, FeedbackCallback callback) {
        executorService.execute(() -> {
            try {
                int updated = chatDao.updateFeedback(chatId, isHelpful);
                if (callback != null) {
                    callback.onSuccess(updated > 0);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error providing feedback", e);
                if (callback != null) {
                    callback.onError("Failed to provide feedback");
                }
            }
        });
    }

    /**
     * Search chat history
     * @param userId User ID
     * @param query Search query
     * @param callback Callback to receive search results
     */
    public void searchChats(int userId, String query, ChatHistoryCallback callback) {
        executorService.execute(() -> {
            try {
                List<Chat> searchResults = chatDao.searchChats(userId, query);
                if (callback != null) {
                    callback.onSuccess(searchResults);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error searching chats", e);
                if (callback != null) {
                    callback.onError("Search failed");
                }
            }
        });
    }

    /**
     * Generate intelligent response based on user message and language
     * @param message User message
     * @param language Language code
     * @return Generated response
     */
    private String generateResponse(String message, String language) {
        String normalizedMessage = message.toLowerCase().trim();
        
        // Check knowledge base for matching responses
        for (String category : KNOWLEDGE_BASE.keySet()) {
            List<FarmingKnowledge> knowledgeList = KNOWLEDGE_BASE.get(category);
            for (FarmingKnowledge knowledge : knowledgeList) {
                if (matchesKeywords(normalizedMessage, knowledge.keywords)) {
                    return getResponseByLanguage(knowledge, language);
                }
            }
        }

        // Default responses if no match found
        return getDefaultResponse(language);
    }

    /**
     * Check if message matches any keywords
     * @param message User message
     * @param keywords List of keywords to match
     * @return true if any keyword matches
     */
    private boolean matchesKeywords(String message, List<String> keywords) {
        for (String keyword : keywords) {
            if (message.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get response in specified language
     * @param knowledge FarmingKnowledge object
     * @param language Language code
     * @return Response in specified language
     */
    private String getResponseByLanguage(FarmingKnowledge knowledge, String language) {
        switch (language.toLowerCase()) {
            case "hi":
                return knowledge.responseHi;
            case "ml":
                return knowledge.responseMl;
            case "en":
            default:
                return knowledge.responseEn;
        }
    }

    /**
     * Get default response when no match found
     * @param language Language code
     * @return Default response in specified language
     */
    private String getDefaultResponse(String language) {
        switch (language.toLowerCase()) {
            case "hi":
                return "मुझे खुशी होगी कि मैं आपकी मदद कर सकूं! कृपया अपना खेती से संबंधित प्रश्न अधिक विस्तार से पूछें।";
            case "ml":
                return "നിങ്ങളെ സഹായിക്കാൻ കഴിഞ്ഞതിൽ സന്തോഷം! കൃപയാ നിങ്ങളുടെ കൃഷി സംബന്ധിയായ ചോദ്യം കൂടുതൽ വിശദമായി ചോദിക്കുക.";
            case "en":
            default:
                return "I'd be happy to help you! Please ask your farming question in more detail so I can provide you with the best guidance.";
        }
    }

    /**
     * Categorize user message
     * @param message User message
     * @return Category of the message
     */
    private String categorizeMessage(String message) {
        String normalizedMessage = message.toLowerCase();
        
        if (containsAny(normalizedMessage, "pest", "insect", "bug", "worm", "disease")) {
            return Chat.CATEGORY_PEST_CONTROL;
        }
        if (containsAny(normalizedMessage, "fertilizer", "manure", "compost", "nutrient")) {
            return Chat.CATEGORY_FERTILIZER;
        }
        if (containsAny(normalizedMessage, "water", "irrigation", "drought", "rain")) {
            return Chat.CATEGORY_IRRIGATION;
        }
        if (containsAny(normalizedMessage, "soil", "earth", "ground")) {
            return Chat.CATEGORY_SOIL;
        }
        if (containsAny(normalizedMessage, "weather", "climate", "season")) {
            return Chat.CATEGORY_WEATHER;
        }
        if (containsAny(normalizedMessage, "harvest", "crop", "yield")) {
            return Chat.CATEGORY_HARVEST;
        }
        if (containsAny(normalizedMessage, "price", "market", "sell")) {
            return Chat.CATEGORY_MARKET;
        }
        
        return Chat.CATEGORY_GENERAL;
    }

    /**
     * Check if message contains any of the specified words
     * @param message Message to check
     * @param words Words to search for
     * @return true if any word is found
     */
    private boolean containsAny(String message, String... words) {
        for (String word : words) {
            if (message.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculate confidence score for the response
     * @param question User question
     * @param response Generated response
     * @return Confidence score (0.0 to 1.0)
     */
    private double calculateConfidence(String question, String response) {
        // Simple confidence calculation based on response specificity
        if (response.length() > 100 && !response.contains("I'd be happy to help")) {
            return 0.8 + (random.nextDouble() * 0.15); // 80-95% for specific responses
        } else if (response.length() > 50) {
            return 0.6 + (random.nextDouble() * 0.2); // 60-80% for moderate responses
        } else {
            return 0.4 + (random.nextDouble() * 0.2); // 40-60% for generic responses
        }
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
     * Initialize knowledge base with farming information
     */
    private static void initializeKnowledgeBase() {
        // Rice farming knowledge
        List<FarmingKnowledge> riceKnowledge = new ArrayList<>();
        riceKnowledge.add(new FarmingKnowledge(
            java.util.Arrays.asList("rice", "paddy", "cultivation", "planting"),
            "For rice cultivation, prepare the field by puddling, maintain 2-3 inches of standing water, and plant 20-25 day old seedlings with 15cm spacing.",
            "धान की खेती के लिए खेत को तैयार करें, 2-3 इंच पानी बनाए रखें, और 20-25 दिन पुराने पौधे 15 सेमी की दूरी पर लगाएं।",
            "നെല്ലുകൃഷിക്കായി വയൽ തയ്യാറാക്കി 2-3 ഇഞ്ച് വെള്ളം നിലനിർത്തി 20-25 ദിവസം പഴക്കമുള്ള തൈകൾ 15 സെമീ അകലത്തിൽ നടുക."
        ));

        // Pest control knowledge
        List<FarmingKnowledge> pestKnowledge = new ArrayList<>();
        pestKnowledge.add(new FarmingKnowledge(
            java.util.Arrays.asList("pest", "insect", "control", "spray"),
            "Use integrated pest management: neem oil spray, beneficial insects, crop rotation, and targeted pesticides only when necessary.",
            "एकीकृत कीट प्रबंधन का उपयोग करें: नीम तेल स्प्रे, लाभकारी कीड़े, फसल चक्रण, और केवल आवश्यक होने पर लक्षित कीटनाशक।",
            "സംയോജിത കീടനിയന്ത്രണം ഉപയോഗിക്കുക: വേപ്പെണ്ണ സ്പ്രേ, ഗുണകരമായ പ്രാണികൾ, വിള ഭ്രമണം, ആവശ്യമുള്ളപ്പോൾ മാത്രം കീടനാശിനി."
        ));

        // Fertilizer knowledge
        List<FarmingKnowledge> fertilizerKnowledge = new ArrayList<>();
        fertilizerKnowledge.add(new FarmingKnowledge(
            java.util.Arrays.asList("fertilizer", "nutrient", "manure", "compost"),
            "Apply balanced NPK fertilizer: 120kg N, 60kg P2O5, 40kg K2O per hectare. Use organic compost and green manure for soil health.",
            "संतुलित एनपीके उर्वरक डालें: 120 किग्रा नाइट्रोजन, 60 किग्रा फास्फोरस, 40 किग्रा पोटाश प्रति हेक्टेयर। मिट्टी के स्वास्थ्य के लिए कंपोस्ट का उपयोग करें।",
            "സമതുലിത എൻപികെ വള പ്രയോഗിക്കുക: ഹെക്ടറിന് 120 കിലോ നൈട്രജൻ, 60 കിലോ ഫോസ്ഫറസ്, 40 കിലോ പൊട്ടാഷ്. മണ്ണിന്റെ ആരോഗ്യത്തിന് കമ്പോസ്റ്റ് ഉപയോഗിക്കുക."
        ));

        KNOWLEDGE_BASE.put("RICE", riceKnowledge);
        KNOWLEDGE_BASE.put("PEST_CONTROL", pestKnowledge);
        KNOWLEDGE_BASE.put("FERTILIZER", fertilizerKnowledge);
        
        // Add more knowledge categories...
    }

    // Callback interfaces
    public interface ChatCallback {
        void onSuccess(Chat userMessage, Chat botResponse, String sessionId);
        void onError(String error);
    }

    public interface ChatHistoryCallback {
        void onSuccess(List<Chat> chatHistory);
        void onError(String error);
    }

    public interface FeedbackCallback {
        void onSuccess(boolean success);
        void onError(String error);
    }
    
    // Simplified callback for ChatbotActivity
    public interface ChatResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    // Inner class for farming knowledge
    private static class FarmingKnowledge {
        List<String> keywords;
        String responseEn, responseHi, responseMl;

        FarmingKnowledge(List<String> keywords, String responseEn, String responseHi, String responseMl) {
            this.keywords = keywords;
            this.responseEn = responseEn;
            this.responseHi = responseHi;
            this.responseMl = responseMl;
        }
    }
}