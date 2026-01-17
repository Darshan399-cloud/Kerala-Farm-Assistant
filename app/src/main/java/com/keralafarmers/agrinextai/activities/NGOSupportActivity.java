package com.keralafarmers.agrinextai.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.adapters.NGOAdapter;
import com.keralafarmers.agrinextai.models.NGO;
import com.keralafarmers.agrinextai.utils.LanguageManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for NGO Support - connects farmers with helpful NGOs
 * Provides list of NGOs with contact information and services
 */
public class NGOSupportActivity extends AppCompatActivity implements NGOAdapter.OnNGOClickListener {
    
    private RecyclerView recyclerViewNGOs;
    private FloatingActionButton fabRequestHelp;
    private NGOAdapter adapter;
    private LanguageManager languageManager;
    private List<NGO> ngoList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize language manager
        languageManager = new LanguageManager(this);
        languageManager.applySavedLanguage();
        
        setContentView(R.layout.activity_ngo_support);
        
        setupToolbar();
        initializeViews();
        loadNGOData();
    }
    
    /**
     * Setup toolbar with back button
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.ngo_support));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * Initialize views
     */
    private void initializeViews() {
        recyclerViewNGOs = findViewById(R.id.recyclerViewNGOs);
        fabRequestHelp = findViewById(R.id.fabRequestHelp);
        
        // Setup RecyclerView
        ngoList = new ArrayList<>();
        adapter = new NGOAdapter(ngoList, this, this);
        recyclerViewNGOs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNGOs.setAdapter(adapter);
        
        // Setup FAB click listener
        fabRequestHelp.setOnClickListener(v -> requestHelp());
    }
    
    /**
     * Load NGO data
     */
    private void loadNGOData() {
        List<NGO> ngos = new ArrayList<>();
        
        // Kerala-based Agricultural NGOs
        NGO ngo1 = new NGO();
        ngo1.setId(1);
        ngo1.setName("Kerala Farmer Producer Organization");
        ngo1.setDescription("Supporting farmers with modern agricultural techniques, seed distribution, and market linkage");
        ngo1.setServices("Seeds, Fertilizers, Training, Market Access");
        ngo1.setLocation("Thiruvananthapuram, Kerala");
        ngo1.setPhoneNumber("+91-9876543210");
        ngo1.setEmail("info@keralafpo.org");
        ngo1.setWebsite("https://keralafpo.org");
        ngo1.setCategory("Agricultural Support");
        ngo1.setRating(4.8f);
        ngo1.setVerified(true);
        ngos.add(ngo1);
        
        NGO ngo2 = new NGO();
        ngo2.setId(2);
        ngo2.setName("Organic Farming Foundation Kerala");
        ngo2.setDescription("Promoting sustainable organic farming practices and certification support");
        ngo2.setServices("Organic Certification, Training, Bio-fertilizers");
        ngo2.setLocation("Kochi, Kerala");
        ngo2.setPhoneNumber("+91-9876543211");
        ngo2.setEmail("contact@offk.org");
        ngo2.setWebsite("https://organickerala.org");
        ngo2.setCategory("Organic Farming");
        ngo2.setRating(4.6f);
        ngo2.setVerified(true);
        ngos.add(ngo2);
        
        NGO ngo3 = new NGO();
        ngo3.setId(3);
        ngo3.setName("Kerala Agricultural Development Society");
        ngo3.setDescription("Financial assistance, crop insurance, and technical support for small farmers");
        ngo3.setServices("Financial Aid, Insurance, Equipment");
        ngo3.setLocation("Palakkad, Kerala");
        ngo3.setPhoneNumber("+91-9876543212");
        ngo3.setEmail("help@kads.org");
        ngo3.setWebsite("https://kads.gov.in");
        ngo3.setCategory("Financial Support");
        ngo3.setRating(4.7f);
        ngo3.setVerified(true);
        ngos.add(ngo3);
        
        NGO ngo4 = new NGO();
        ngo4.setId(4);
        ngo4.setName("Spice Board Kerala Support");
        ngo4.setDescription("Specialized support for spice farmers including cardamom, pepper, and turmeric");
        ngo4.setServices("Spice Certification, Quality Testing, Export Support");
        ngo4.setLocation("Idukki, Kerala");
        ngo4.setPhoneNumber("+91-9876543213");
        ngo4.setEmail("spices@kerala.gov.in");
        ngo4.setWebsite("https://spiceboard.kerala.gov.in");
        ngo4.setCategory("Spice Farming");
        ngo4.setRating(4.5f);
        ngo4.setVerified(true);
        ngos.add(ngo4);
        
        NGO ngo5 = new NGO();
        ngo5.setId(5);
        ngo5.setName("Women Farmers Collective Kerala");
        ngo5.setDescription("Empowering women farmers with training, microfinance, and collective farming");
        ngo5.setServices("Women Training, Microfinance, Collective Farming");
        ngo5.setLocation("Kozhikode, Kerala");
        ngo5.setPhoneNumber("+91-9876543214");
        ngo5.setEmail("women@farmerskerala.org");
        ngo5.setWebsite("https://womenfarming.kerala.gov.in");
        ngo5.setCategory("Women Empowerment");
        ngo5.setRating(4.9f);
        ngo5.setVerified(true);
        ngos.add(ngo5);
        
        NGO ngo6 = new NGO();
        ngo6.setId(6);
        ngo6.setName("Kerala Climate Resilient Agriculture");
        ngo6.setDescription("Climate-smart farming techniques and disaster recovery support");
        ngo6.setServices("Climate Training, Disaster Recovery, Resilient Seeds");
        ngo6.setLocation("Kannur, Kerala");
        ngo6.setPhoneNumber("+91-9876543215");
        ngo6.setEmail("climate@keralaagriculture.org");
        ngo6.setWebsite("https://climateagriculture.kerala.gov.in");
        ngo6.setCategory("Climate Support");
        ngo6.setRating(4.4f);
        ngo6.setVerified(true);
        ngos.add(ngo6);
        
        // Update adapter data
        ngoList.clear();
        ngoList.addAll(ngos);
        adapter.notifyDataSetChanged();
    }
    
    /**
     * Request help from NGOs
     */
    private void requestHelp() {
        String helpMessage = getString(R.string.help_message);
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.farming_support_request));
        shareIntent.putExtra(Intent.EXTRA_TEXT, helpMessage);
        
        Intent chooser = Intent.createChooser(shareIntent, getString(R.string.request_help_from_ngos));
        startActivity(chooser);
    }
    
    @Override
    public void onNGOClick(NGO ngo) {
        // Show NGO details or contact options
        showContactOptions(ngo);
    }
    
    @Override
    public void onCallClick(NGO ngo) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + ngo.getPhoneNumber()));
        startActivity(callIntent);
    }
    
    @Override
    public void onEmailClick(NGO ngo) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + ngo.getEmail()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.inquiry_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_template, ngo.getName()));
        
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, getString(R.string.no_email_app), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onWebsiteClick(NGO ngo) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ngo.getWebsite()));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, getString(R.string.no_browser_found), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Show contact options for NGO
     */
    private void showContactOptions(NGO ngo) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.contact_ngo, ngo.getName()))
                .setMessage("🏢 " + ngo.getDescription() + "\n\n" +
                           "📍 " + getString(R.string.location_label) + " " + ngo.getLocation() + "\n" +
                           "🛠️ " + getString(R.string.services_label) + " " + ngo.getServices() + "\n\n" +
                           getString(R.string.choose_contact_method))
                .setPositiveButton(getString(R.string.call_button), (dialog, which) -> onCallClick(ngo))
                .setNeutralButton(getString(R.string.email_button), (dialog, which) -> onEmailClick(ngo))
                .setNegativeButton(getString(R.string.website_button), (dialog, which) -> onWebsiteClick(ngo))
                .show();
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