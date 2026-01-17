package com.keralafarmers.agrinextai.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.MarketPriceAdapter;
import com.keralafarmers.agrinextai.models.MarketPrice;
import com.keralafarmers.agrinextai.services.MarketPriceService;
import com.keralafarmers.agrinextai.utils.LanguageManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Market Price Activity for displaying current crop prices
 * Shows real-time market prices for various crops in Kerala
 */
public class MarketPriceActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewPrices;
    private MarketPriceAdapter adapter;
    private MarketPriceService marketPriceService;
    private LanguageManager languageManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_market_price);
        
        setupToolbar();
        initializeViews();
        initializeServices();
        loadMarketPrices();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.market_prices));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        recyclerViewPrices = findViewById(R.id.recyclerViewPrices);
        recyclerViewPrices.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize adapter with empty list
        adapter = new MarketPriceAdapter(new ArrayList<>(), this);
        recyclerViewPrices.setAdapter(adapter);
    }
    
    /**
     * Initialize services
     */
    private void initializeServices() {
        marketPriceService = new MarketPriceService(this);
    }
    
    /**
     * Load market prices from service
     */
    private void loadMarketPrices() {
        marketPriceService.getAllMarketPrices(new MarketPriceService.MarketPriceCallback() {
            @Override
            public void onSuccess(List<MarketPrice> prices) {
                runOnUiThread(() -> {
                    adapter.updatePrices(prices);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MarketPriceActivity.this, getString(R.string.error_loading_prices, error), Toast.LENGTH_SHORT).show();
                    // Load sample data for demo
                    loadSampleData();
                });
            }
        });
    }
    
    /**
     * Load sample data for demonstration
     */
    private void loadSampleData() {
        List<MarketPrice> samplePrices = new ArrayList<>();
        
        MarketPrice rice = new MarketPrice();
        rice.setCropName("Rice (Paddy)");
        rice.setVariety("IR-64");
        rice.setMinPrice(2800);
        rice.setMaxPrice(3200);
        rice.setModalPrice(3000);
        rice.setUnit("Quintal");
        rice.setMarketName("Kochi Market");
        rice.setLastUpdated(System.currentTimeMillis());
        samplePrices.add(rice);
        
        MarketPrice coconut = new MarketPrice();
        coconut.setCropName("Coconut");
        coconut.setVariety("Tall Variety");
        coconut.setMinPrice(28);
        coconut.setMaxPrice(32);
        coconut.setModalPrice(30);
        coconut.setUnit("Per Piece");
        coconut.setMarketName("Thrissur Market");
        coconut.setLastUpdated(System.currentTimeMillis());
        samplePrices.add(coconut);
        
        MarketPrice banana = new MarketPrice();
        banana.setCropName("Banana");
        banana.setVariety("Robusta");
        banana.setMinPrice(15);
        banana.setMaxPrice(25);
        banana.setModalPrice(20);
        banana.setUnit("Per Dozen");
        banana.setMarketName("Palakkad Market");
        banana.setLastUpdated(System.currentTimeMillis());
        samplePrices.add(banana);
        
        MarketPrice pepper = new MarketPrice();
        pepper.setCropName("Black Pepper");
        pepper.setVariety("Panniyur-1");
        pepper.setMinPrice(450);
        pepper.setMaxPrice(550);
        pepper.setModalPrice(500);
        pepper.setUnit("Per Kg");
        pepper.setMarketName("Idukki Market");
        pepper.setLastUpdated(System.currentTimeMillis());
        samplePrices.add(pepper);
        
        MarketPrice cardamom = new MarketPrice();
        cardamom.setCropName("Cardamom");
        cardamom.setVariety("Malabar");
        cardamom.setMinPrice(1800);
        cardamom.setMaxPrice(2200);
        cardamom.setModalPrice(2000);
        cardamom.setUnit("Per Kg");
        cardamom.setMarketName("Kumily Market");
        cardamom.setLastUpdated(System.currentTimeMillis());
        samplePrices.add(cardamom);
        
        adapter.updatePrices(samplePrices);
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