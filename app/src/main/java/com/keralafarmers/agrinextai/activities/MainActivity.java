package com.keralafarmers.agrinextai.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.services.AuthService;
import com.keralafarmers.agrinextai.utils.LanguageManager;

/**
 * Main Activity for Kerala Farm Assistant App
 * Contains navigation to all major features
 */
public class MainActivity extends AppCompatActivity {

    private AuthService authService;
    private LanguageManager languageManager;

    private CardView cardWeather, cardAdvisor, cardMarketPrices, cardChatbot, cardTraceability, cardNgoSupport, cardGovernmentScheme, cardSatellite;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize language manager and apply saved language
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();

        setContentView(R.layout.activity_main);

        // Initialize services
        authService = new AuthService(this);

        // Check if user is logged in
        if (!authService.isUserLoggedIn()) {
            navigateToLogin();
            return;
        }

        setupToolbar();
        initializeViews();
        setupClickListeners();
        updateWelcomeText();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cardWeather = findViewById(R.id.cardWeather);
        cardAdvisor = findViewById(R.id.cardAdvisor);
        cardMarketPrices = findViewById(R.id.cardMarketPrices);
        cardChatbot = findViewById(R.id.cardChatbot);
        cardTraceability = findViewById(R.id.cardTraceability);
        cardNgoSupport = findViewById(R.id.cardNgoSupport);
        cardGovernmentScheme = findViewById(R.id.cardGovernmentScheme);
        cardSatellite = findViewById(R.id.cardSatellite);
    }

    /**
     * Setup click listeners for all cards
     */
    private void setupClickListeners() {
        cardWeather.setOnClickListener(v -> {
            // Navigate to Weather module
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
        });

        cardAdvisor.setOnClickListener(v -> {
            // Navigate to AI Advisor module
            Intent intent = new Intent(MainActivity.this, AIAdvisorActivity.class);
            startActivity(intent);
        });

        cardMarketPrices.setOnClickListener(v -> {
            // Navigate to Market Prices module
            Intent intent = new Intent(MainActivity.this, MarketPriceActivity.class);
            startActivity(intent);
        });
        
        cardChatbot.setOnClickListener(v -> {
            // Navigate to Chatbot module
            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
            startActivity(intent);
        });
        
        cardTraceability.setOnClickListener(v -> {
            // Navigate to Traceability module
            Intent intent = new Intent(MainActivity.this, TraceabilityActivity.class);
            startActivity(intent);
        });
        
        cardNgoSupport.setOnClickListener(v -> {
            // Navigate to NGO Support module
            Intent intent = new Intent(MainActivity.this, NGOSupportActivity.class);
            startActivity(intent);
        });
        
        cardGovernmentScheme.setOnClickListener(v -> {
            // Navigate to Government Scheme module
            Intent intent = new Intent(MainActivity.this, GovernmentSchemeActivity.class);
            startActivity(intent);
        });
        
        cardSatellite.setOnClickListener(v -> {
            // Navigate to Satellite module
            Intent intent = new Intent(MainActivity.this, SatelliteActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Update welcome text with user name
     */
    private void updateWelcomeText() {
        String userName = authService.getSessionManager().getUserName();
        if (userName != null && !userName.isEmpty()) {
            String welcomeText = getString(R.string.welcome_user, userName);
            tvWelcome.setText(welcomeText);
        } else {
            tvWelcome.setText(getString(R.string.welcome_farmer));
        }
    }

    /**
     * Navigate to login activity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Toast.makeText(this, "Profile coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            handleLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle user logout
     */
    private void handleLogout() {
        authService.logoutUser();
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        navigateToLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if session expired
        if (authService.getSessionManager().clearExpiredSession()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        // Update welcome text in case user profile was updated
        updateWelcomeText();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Navigate back to App Intro instead of exiting app
        navigateToAppIntro();
    }
    
    /**
     * Navigate back to App Intro Activity
     */
    private void navigateToAppIntro() {
        Intent intent = new Intent(this, AppIntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
