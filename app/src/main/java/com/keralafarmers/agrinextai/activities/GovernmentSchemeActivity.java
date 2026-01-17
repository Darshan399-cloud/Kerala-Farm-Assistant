package com.keralafarmers.agrinextai.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.GovernmentSchemeAdapter;
import com.keralafarmers.agrinextai.models.GovernmentScheme;
import com.keralafarmers.agrinextai.utils.LanguageManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Government Schemes - displays available government farming schemes and subsidies
 */
public class GovernmentSchemeActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewSchemes;
    private GovernmentSchemeAdapter adapter;
    private LanguageManager languageManager;
    private List<GovernmentScheme> schemeList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_government_scheme);
        
        setupToolbar();
        initializeViews();
        loadSchemeData();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.government_schemes));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        recyclerViewSchemes = findViewById(R.id.recyclerViewSchemes);
        
        // Setup RecyclerView
        schemeList = new ArrayList<>();
        adapter = new GovernmentSchemeAdapter(schemeList, this);
        recyclerViewSchemes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSchemes.setAdapter(adapter);
    }
    
    /**
     * Load government scheme data
     */
    private void loadSchemeData() {
        List<GovernmentScheme> schemes = new ArrayList<>();
        
        // Sample government schemes for Kerala farmers
        GovernmentScheme scheme1 = new GovernmentScheme();
        scheme1.setId(1);
        scheme1.setName("Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)");
        scheme1.setDescription("Financial support of Rs. 6000 per year to small and marginal farmers");
        scheme1.setEligibility("Small and marginal farmers with cultivable land up to 2 hectares");
        scheme1.setBenefit("Rs. 2000 every 4 months (Total Rs. 6000/year)");
        scheme1.setApplicationProcess("Online application through PM-KISAN portal or local authorities");
        scheme1.setContactInfo("Toll-free: 155261, Website: pmkisan.gov.in");
        scheme1.setCategory("Direct Benefit Transfer");
        schemes.add(scheme1);
        
        GovernmentScheme scheme2 = new GovernmentScheme();
        scheme2.setId(2);
        scheme2.setName("Kerala State Farming Equipment Subsidy");
        scheme2.setDescription("Subsidy for purchasing modern farming equipment and machinery");
        scheme2.setEligibility("All registered farmers in Kerala");
        scheme2.setBenefit("Up to 50% subsidy on approved farming equipment");
        scheme2.setApplicationProcess("Apply through Kerala Agriculture Department");
        scheme2.setContactInfo("Kerala Agriculture Department: 0471-2301170");
        scheme2.setCategory("Equipment Subsidy");
        schemes.add(scheme2);
        
        GovernmentScheme scheme3 = new GovernmentScheme();
        scheme3.setId(3);
        scheme3.setName("Organic Farming Promotion Scheme");
        scheme3.setDescription("Support for organic farming certification and practices");
        scheme3.setEligibility("Farmers willing to adopt organic farming methods");
        scheme3.setBenefit("Certification cost support + Rs. 20,000/hectare for 3 years");
        scheme3.setApplicationProcess("Through authorized organic certification agencies");
        scheme3.setContactInfo("Spices Board: 0484-2333610");
        scheme3.setCategory("Organic Farming");
        schemes.add(scheme3);
        
        GovernmentScheme scheme4 = new GovernmentScheme();
        scheme4.setId(4);
        scheme4.setName("Crop Insurance Scheme (PMFBY)");
        scheme4.setDescription("Insurance coverage for crop loss due to natural calamities");
        scheme4.setEligibility("All farmers including tenant farmers and sharecroppers");
        scheme4.setBenefit("Insurance coverage up to sum insured amount");
        scheme4.setApplicationProcess("Through banks or agriculture department");
        scheme4.setContactInfo("Agriculture Insurance Company: 1800-180-1551");
        scheme4.setCategory("Insurance");
        schemes.add(scheme4);
        
        GovernmentScheme scheme5 = new GovernmentScheme();
        scheme5.setId(5);
        scheme5.setName("Kerala Coconut Development Board Subsidy");
        scheme5.setDescription("Support for coconut farming and processing activities");
        scheme5.setEligibility("Coconut farmers and coconut farmer producer organizations");
        scheme5.setBenefit("Various subsidies for planting, processing, and marketing");
        scheme5.setApplicationProcess("Through Coconut Development Board offices");
        scheme5.setContactInfo("Coconut Development Board: 0484-2377266");
        scheme5.setCategory("Crop Specific");
        schemes.add(scheme5);
        
        // Update adapter data
        schemeList.clear();
        schemeList.addAll(schemes);
        adapter.notifyDataSetChanged();
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