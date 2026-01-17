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
 * User Registration Activity
 * Handles new user registration for the app
 */
public class RegisterActivity extends AppCompatActivity {
    
    private TextInputEditText etFullName, etEmail, etPhone, etLocation, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;
    
    private AuthService authService;
    private LanguageManager languageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager and apply saved language
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_register);
        
        // Initialize services
        authService = new AuthService(this);
        
        initializeViews();
        setupClickListeners();
    }
    
    /**
     * Initialize all views
     */
    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
    }
    
    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> handleRegistration());
        tvLogin.setOnClickListener(v -> navigateToLogin());
    }
    
    /**
     * Handle user registration
     */
    private void handleRegistration() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        // Validation
        if (fullName.isEmpty()) {
            etFullName.setError("Please enter your full name");
            etFullName.requestFocus();
            return;
        }
        
        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.invalid_email));
            etEmail.requestFocus();
            return;
        }
        
        if (phone.isEmpty()) {
            etPhone.setError("Please enter phone number");
            etPhone.requestFocus();
            return;
        }
        
        if (location.isEmpty()) {
            etLocation.setError("Please enter farm location");
            etLocation.requestFocus();
            return;
        }
        
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError(getString(R.string.password_too_short));
            etPassword.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }
        
        // Show loading state
        showLoading(true);
        
        // Register user
        authService.registerUser(fullName, email, phone, location, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                Toast.makeText(RegisterActivity.this, "Registration successful! Welcome to Kerala Farm Community!", Toast.LENGTH_LONG).show();
                navigateToAppIntro();
            }
            
            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Show/hide loading state
     */
    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
            btnRegister.setText(getString(R.string.loading));
        } else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            btnRegister.setText(getString(R.string.register));
        }
    }
    
    /**
     * Navigate to app intro after successful registration
     */
    private void navigateToAppIntro() {
        Intent intent = new Intent(this, AppIntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Navigate back to login
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        navigateToLogin();
        super.onBackPressed();
    }
}
