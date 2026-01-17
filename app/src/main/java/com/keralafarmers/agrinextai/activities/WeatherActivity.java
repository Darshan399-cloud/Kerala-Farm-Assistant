package com.keralafarmers.agrinextai.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.WeatherAdapter;
import com.keralafarmers.agrinextai.models.Weather;
import com.keralafarmers.agrinextai.services.WeatherService;
import com.keralafarmers.agrinextai.utils.LanguageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Weather Activity for displaying weather information
 * Shows current weather, past 5 days, and future 5 days forecast
 */
public class WeatherActivity extends AppCompatActivity {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final long REFRESH_INTERVAL_MS = 60 * 1000L; // 1 minute (tweak as needed)
    
    // UI Components
    private TextView tvWeatherIcon, tvCurrentTemp, tvWeatherDescription, tvLocation, tvCurrentDate;
    private TextView tvHumidity, tvRainfall, tvWindSpeed, tvWeatherAdvice;
    private RecyclerView rvPastWeather, rvFutureWeather;
    private MaterialButton btnRefresh;
    private ProgressBar progressBar;
    
    // Services and Adapters
    private WeatherService weatherService;
    private LanguageManager languageManager;
    private WeatherAdapter pastWeatherAdapter, futureWeatherAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler autoRefreshHandler;
    private final Runnable autoRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            refreshWeatherData();
            autoRefreshHandler.postDelayed(this, REFRESH_INTERVAL_MS);
        }
    };
    
    // Data
    private List<Weather> pastWeatherList, futureWeatherList;
    private double currentLatitude = 10.8505; // Default to Kerala coordinates
    private double currentLongitude = 76.2711;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_weather);
        
        initializeComponents();
        setupToolbar();
        setupRecyclerViews();
        checkLocationPermission();
    }
    
    /**
     * Initialize all components and services
     */
    private void initializeComponents() {
        // Initialize UI components
        tvWeatherIcon = findViewById(R.id.tvWeatherIcon);
        tvCurrentTemp = findViewById(R.id.tvCurrentTemp);
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvRainfall = findViewById(R.id.tvRainfall);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvWeatherAdvice = findViewById(R.id.tvWeatherAdvice);
        rvPastWeather = findViewById(R.id.rvPastWeather);
        rvFutureWeather = findViewById(R.id.rvFutureWeather);
        btnRefresh = findViewById(R.id.btnRefresh);
        progressBar = findViewById(R.id.progressBar);
        
        // Initialize services
        weatherService = new WeatherService(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        autoRefreshHandler = new Handler(Looper.getMainLooper());
        
        // Initialize data lists
        pastWeatherList = new ArrayList<>();
        futureWeatherList = new ArrayList<>();
        
        // Set click listeners
        btnRefresh.setOnClickListener(v -> refreshWeatherData());
        
        // Set current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());
        tvCurrentDate.setText(dateFormat.format(new Date()));
    }
    
    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.weather));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    
    /**
     * Setup RecyclerViews for past and future weather
     */
    private void setupRecyclerViews() {
        // Past weather RecyclerView
        LinearLayoutManager pastLayoutManager = new LinearLayoutManager(this);
        rvPastWeather.setLayoutManager(pastLayoutManager);
        pastWeatherAdapter = new WeatherAdapter(this, pastWeatherList);
        rvPastWeather.setAdapter(pastWeatherAdapter);
        
        // Future weather RecyclerView
        LinearLayoutManager futureLayoutManager = new LinearLayoutManager(this);
        rvFutureWeather.setLayoutManager(futureLayoutManager);
        futureWeatherAdapter = new WeatherAdapter(this, futureWeatherList);
        rvFutureWeather.setAdapter(futureWeatherAdapter);
    }
    
    /**
     * Check and request location permission
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }
    
    /**
     * Get user's current location
     */
    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            loadWeatherData();
            return;
        }
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                    }
                    loadWeatherData();
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(this, "Unable to get location. Using default location.", Toast.LENGTH_SHORT).show();
                    loadWeatherData();
                });
    }
    
    /**
     * Load weather data from service
     */
    private void loadWeatherData() {
        showLoading(true);
        
        weatherService.getWeatherData(currentLatitude, currentLongitude, new WeatherService.WeatherCallback() {
            @Override
            public void onSuccess(List<Weather> weatherList) {
                showLoading(false);
                processWeatherData(weatherList);
            }
            
            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(WeatherActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Process weather data and update UI
     * @param weatherList Complete weather list (past + current + future)
     */
    private void processWeatherData(List<Weather> weatherList) {
        if (weatherList == null || weatherList.isEmpty()) {
            return;
        }
        
        // Clear existing data
        pastWeatherList.clear();
        futureWeatherList.clear();
        
        // Find current weather (middle of the list)
        int currentIndex = 5; // Past 5 days + current day = index 5
        Weather currentWeather = null;
        
        if (weatherList.size() > currentIndex) {
            currentWeather = weatherList.get(currentIndex);
            
            // Past weather (first 5 items)
            for (int i = 0; i < 5 && i < weatherList.size(); i++) {
                pastWeatherList.add(weatherList.get(i));
            }
            
            // Future weather (after current weather)
            for (int i = currentIndex + 1; i < weatherList.size(); i++) {
                futureWeatherList.add(weatherList.get(i));
            }
        } else {
            // Fallback if data structure is different
            currentWeather = weatherList.get(0);
            for (int i = 1; i < weatherList.size(); i++) {
                if (i <= 5) {
                    pastWeatherList.add(weatherList.get(i));
                } else {
                    futureWeatherList.add(weatherList.get(i));
                }
            }
        }
        
        // Update current weather UI
        if (currentWeather != null) {
            updateCurrentWeatherUI(currentWeather);
        }
        
        // Update RecyclerViews
        pastWeatherAdapter.updateWeatherList(pastWeatherList);
        futureWeatherAdapter.updateWeatherList(futureWeatherList);
    }
    
    /**
     * Update current weather UI
     * @param currentWeather Current weather data
     */
    private void updateCurrentWeatherUI(Weather currentWeather) {
        tvWeatherIcon.setText(currentWeather.getWeatherIcon());
        tvCurrentTemp.setText(String.format(Locale.getDefault(), "%.0f°C", currentWeather.getTemperature()));
        tvWeatherDescription.setText(currentWeather.getWeatherDescription());
        tvLocation.setText(currentWeather.getLocation());
        
        tvHumidity.setText(String.format(Locale.getDefault(), "%d%%", currentWeather.getHumidity()));
        
        if (currentWeather.getRainfall() > 0) {
            tvRainfall.setText(String.format(Locale.getDefault(), "%.1fmm", currentWeather.getRainfall()));
        } else {
            tvRainfall.setText("0mm");
        }
        
        tvWindSpeed.setText(String.format(Locale.getDefault(), "%.0f km/h", currentWeather.getWindSpeed()));
        
        // Set weather advice
        String advice = weatherService.getWeatherAdvice(currentWeather);
        tvWeatherAdvice.setText(advice);
    }
    
    /**
     * Refresh weather data
     */
    private void refreshWeatherData() {
        // Clean old data first
        weatherService.cleanOldWeatherData();
        
        // Reload weather data
        loadWeatherData();
        
        Toast.makeText(this, getString(R.string.weather_loading), Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Show/hide loading state
     * @param show True to show loading
     */
    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnRefresh.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnRefresh.setEnabled(true);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, getString(R.string.location_permission_required), Toast.LENGTH_LONG).show();
                loadWeatherData(); // Load with default location
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Load weather data when activity resumes
        if (pastWeatherList.isEmpty() && futureWeatherList.isEmpty()) {
            loadWeatherData();
        }
        // Start periodic auto-refresh while activity is visible
        if (autoRefreshHandler != null) {
            autoRefreshHandler.removeCallbacks(autoRefreshRunnable);
            autoRefreshHandler.postDelayed(autoRefreshRunnable, REFRESH_INTERVAL_MS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop auto-refresh when activity goes to background
        if (autoRefreshHandler != null) {
            autoRefreshHandler.removeCallbacks(autoRefreshRunnable);
        }
    }
}