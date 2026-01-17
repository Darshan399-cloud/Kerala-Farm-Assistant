package com.keralafarmers.agrinextai.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.ChatAdapter;
import com.keralafarmers.agrinextai.models.Chat;
import com.keralafarmers.agrinextai.services.ChatbotService;
import com.keralafarmers.agrinextai.utils.LanguageManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Chatbot Activity for multilingual farming assistance
 * Provides AI-powered responses to farming questions in English, Hindi, and Malayalam
 */
public class ChatbotActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private ChatAdapter chatAdapter;
    private ChatbotService chatbotService;
    private LanguageManager languageManager;
    private List<Chat> chatHistory;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_chatbot);
        
        setupToolbar();
        initializeViews();
        initializeServices();
        loadChatHistory();
        showWelcomeMessage();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.farm_assistant_chat));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        
        // Setup RecyclerView
        chatHistory = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatHistory, this);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);
        
        // Setup send button click listener
        btnSend.setOnClickListener(v -> sendMessage());
        
        // Send message on Enter key press
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }
    
    /**
     * Initialize services
     */
    private void initializeServices() {
        chatbotService = new ChatbotService(this);
    }
    
    /**
     * Load chat history
     */
    private void loadChatHistory() {
        chatbotService.getChatHistory(1, new ChatbotService.ChatHistoryCallback() {
            @Override
            public void onSuccess(List<Chat> chats) {
                runOnUiThread(() -> {
                    chatHistory.clear();
                    chatHistory.addAll(chats);
                    chatAdapter.notifyDataSetChanged();
                    scrollToBottom();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ChatbotActivity.this, "Error loading chat history: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Show welcome message
     */
    private void showWelcomeMessage() {
        String welcomeMessage = getWelcomeMessageInCurrentLanguage();
        
        Chat welcomeChat = new Chat();
        welcomeChat.setMessage(welcomeMessage);
        welcomeChat.setIsFromUser(false);
        welcomeChat.setTimestamp(System.currentTimeMillis());
        welcomeChat.setLanguage(languageManager.getCurrentLanguage());
        
        chatHistory.add(welcomeChat);
        chatAdapter.notifyItemInserted(chatHistory.size() - 1);
        scrollToBottom();
    }
    
    /**
     * Get welcome message in current language
     */
    private String getWelcomeMessageInCurrentLanguage() {
        String currentLang = languageManager.getCurrentLanguage();
        
        switch (currentLang) {
            case LanguageManager.LANGUAGE_HINDI:
                return "नमस्ते! मैं आपका कृषि सहायक हूं। आप मुझसे खेती, फसल, मौसम और बाजार के बारे में कोई भी सवाल पूछ सकते हैं।";
            case LanguageManager.LANGUAGE_MALAYALAM:
                return "ഹലോ! ഞാൻ നിങ്ങളുടെ കാർഷിക സഹായിയാണ്. കൃഷി, വിളകൾ, കാലാവസ്ഥ, വിപണി എന്നിവയെക്കുറിച്ച് എന്തെങ്കിലും ചോദ്യങ്ങൾ ചോദിക്കാൻ മടിക്കരുത്.";
            default:
                return "Hello! I'm your farming assistant. You can ask me anything about agriculture, crops, weather, and market prices. How can I help you today?";
        }
    }
    
    /**
     * Send message to chatbot
     */
    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // Clear input
        etMessage.setText("");
        
        // Add user message to chat
        Chat userChat = new Chat();
        userChat.setMessage(message);
        userChat.setIsFromUser(true);
        userChat.setTimestamp(System.currentTimeMillis());
        userChat.setLanguage(languageManager.getCurrentLanguage());
        
        chatHistory.add(userChat);
        chatAdapter.notifyItemInserted(chatHistory.size() - 1);
        scrollToBottom();
        
        // Show typing indicator
        showTypingIndicator();
        
        // Get response from chatbot service
        chatbotService.sendMessage(1, message, languageManager.getCurrentLanguage(), new ChatbotService.ChatResponseCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> {
                    hideTypingIndicator();
                    
                    // Add bot response to chat
                    Chat botChat = new Chat();
                    botChat.setMessage(response);
                    botChat.setIsFromUser(false);
                    botChat.setTimestamp(System.currentTimeMillis());
                    botChat.setLanguage(languageManager.getCurrentLanguage());
                    
                    chatHistory.add(botChat);
                    chatAdapter.notifyItemInserted(chatHistory.size() - 1);
                    scrollToBottom();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    hideTypingIndicator();
                    
                    // Show error response
                    Chat errorChat = new Chat();
                    errorChat.setMessage(getErrorMessageInCurrentLanguage());
                    errorChat.setIsFromUser(false);
                    errorChat.setTimestamp(System.currentTimeMillis());
                    errorChat.setLanguage(languageManager.getCurrentLanguage());
                    
                    chatHistory.add(errorChat);
                    chatAdapter.notifyItemInserted(chatHistory.size() - 1);
                    scrollToBottom();
                });
            }
        });
    }
    
    /**
     * Get error message in current language
     */
    private String getErrorMessageInCurrentLanguage() {
        String currentLang = languageManager.getCurrentLanguage();
        
        switch (currentLang) {
            case LanguageManager.LANGUAGE_HINDI:
                return "क्षमा करें, मुझे आपका उत्तर देने में कुछ समस्या हो रही है। कृपया बाद में पुनः प्रयास करें।";
            case LanguageManager.LANGUAGE_MALAYALAM:
                return "ക്ഷമിക്കണം, നിങ്ങളുടെ ചോദ്യത്തിന് ഉത്തരം നൽകുന്നതിൽ എനിക്ക് പ്രശ്‌നമുണ്ട്. ദയവായി പിന്നീട് വീണ്ടും ശ്രമിക്കുക.";
            default:
                return "Sorry, I'm having trouble responding right now. Please try again later.";
        }
    }
    
    /**
     * Show typing indicator
     */
    private void showTypingIndicator() {
        Chat typingChat = new Chat();
        typingChat.setMessage("Typing...");
        typingChat.setIsFromUser(false);
        typingChat.setTimestamp(System.currentTimeMillis());
        typingChat.setIsTyping(true);
        
        chatHistory.add(typingChat);
        chatAdapter.notifyItemInserted(chatHistory.size() - 1);
        scrollToBottom();
    }
    
    /**
     * Hide typing indicator
     */
    private void hideTypingIndicator() {
        // Remove typing indicator if it exists
        for (int i = chatHistory.size() - 1; i >= 0; i--) {
            if (chatHistory.get(i).isTyping()) {
                chatHistory.remove(i);
                chatAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }
    
    /**
     * Scroll to bottom of chat
     */
    private void scrollToBottom() {
        if (chatHistory.size() > 0) {
            recyclerViewChat.smoothScrollToPosition(chatHistory.size() - 1);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
