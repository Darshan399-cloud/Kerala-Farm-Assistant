package com.keralafarmers.agrinextai.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.User;
import com.keralafarmers.agrinextai.services.AuthService;
import com.keralafarmers.agrinextai.utils.LanguageManager;

/**
 * Login Activity for Kerala Farm Assistant App
 * Handles user authentication with multilingual support
 */
public class LoginActivity extends AppCompatActivity {
    
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private TextView tvLanguageEn, tvLanguageHi, tvLanguageMl;
    private ProgressBar progressBar;
    
    private AuthService authService;
    private LanguageManager languageManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager and apply saved language
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_login);
        
        // Initialize services
        authService = new AuthService(this);
        
        // Check if user is already logged in
        if (authService.isUserLoggedIn()) {
            navigateToAppIntro();
            return;
        }
        
        initializeViews();
        setupClickListeners();
    }
    
    /**
     * Initialize all views
     */
    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        
        // Language switcher views
        tvLanguageEn = findViewById(R.id.tvLanguageEn);
        tvLanguageHi = findViewById(R.id.tvLanguageHi);
        tvLanguageMl = findViewById(R.id.tvLanguageMl);
        
        // Update language UI
        updateLanguageUI();
    }
    
    /**
     * Setup click listeners for all interactive elements
     */
    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> navigateToRegister());
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
        
        // Language switcher listeners
        tvLanguageEn.setOnClickListener(v -> changeLanguage(LanguageManager.LANGUAGE_ENGLISH));
        tvLanguageHi.setOnClickListener(v -> changeLanguage(LanguageManager.LANGUAGE_HINDI));
        tvLanguageMl.setOnClickListener(v -> changeLanguage(LanguageManager.LANGUAGE_MALAYALAM));
    }
    
    /**
     * Handle user login
     */
    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Basic validation
        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.invalid_email));
            etEmail.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.password_too_short));
            etPassword.requestFocus();
            return;
        }
        
        // Show loading state
        showLoading(true);
        
        // Perform login
        authService.loginUser(email, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                
                // Apply user's preferred language
                if (user.getPreferredLanguage() != null) {
                    languageManager.setLanguage(user.getPreferredLanguage());
                }
                
                Toast.makeText(LoginActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                navigateToAppIntro();
            }
            
            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Navigate to registration activity
     */
    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    
    /**
     * Handle forgot password (placeholder for now)
     */
    private void handleForgotPassword() {
        Toast.makeText(this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Navigate to app intro after successful login
     */
    private void navigateToAppIntro() {
        Intent intent = new Intent(this, AppIntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Change app language
     * @param languageCode Language code to switch to
     */
    private void changeLanguage(String languageCode) {
        if (!languageCode.equals(languageManager.getCurrentLanguage())) {
            languageManager.setLanguage(languageCode);
            languageManager.restartActivity(this);
        }
    }
    
    /**
     * Update language selection UI
     */
    private void updateLanguageUI() {
        String currentLanguage = languageManager.getCurrentLanguage();
        
        // Reset all language views
        tvLanguageEn.setAlpha(0.6f);
        tvLanguageHi.setAlpha(0.6f);
        tvLanguageMl.setAlpha(0.6f);
        
        // Highlight current language
        switch (currentLanguage) {
            case LanguageManager.LANGUAGE_ENGLISH:
                tvLanguageEn.setAlpha(1.0f);
                break;
            case LanguageManager.LANGUAGE_HINDI:
                tvLanguageHi.setAlpha(1.0f);
                break;
            case LanguageManager.LANGUAGE_MALAYALAM:
                tvLanguageMl.setAlpha(1.0f);
                break;
        }
    }
    
    /**
     * Show/hide loading state
     * @param show True to show loading, false to hide
     */
    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            btnLogin.setText(getString(R.string.loading));
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            btnLogin.setText(getString(R.string.login));
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Clear expired sessions
        if (authService.getSessionManager().clearExpiredSession()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
        }
        
        // Update language UI in case it was changed
        updateLanguageUI();
    }
    
    @Override
    public void onBackPressed() {
        // Close app when back is pressed on login screen
        finishAffinity();
        super.onBackPressed();
    }
}