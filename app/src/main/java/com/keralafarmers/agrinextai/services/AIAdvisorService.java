package com.keralafarmers.agrinextai.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.keralafarmers.agrinextai.database.AppDatabase;
import com.keralafarmers.agrinextai.database.PlantDiseaseDao;
import com.keralafarmers.agrinextai.models.PlantDisease;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * AI Advisor Service for plant disease detection and treatment recommendations
 * Note: This is a mock implementation. In production, integrate with actual ML models
 */
public class AIAdvisorService {
    
    private Context context;
    private PlantDiseaseDao plantDiseaseDao;
    private Random random;
    private Map<String, DiseaseInfo> diseaseDatabase;
    
    public AIAdvisorService(Context context) {
        this.context = context;
        this.plantDiseaseDao = AppDatabase.getInstance(context).plantDiseaseDao();
        this.random = new Random();
        initializeDiseaseDatabase();
    }
    
    /**
     * Interface for disease detection callbacks
     */
    public interface DiseaseDetectionCallback {
        void onSuccess(PlantDisease plantDisease);
        void onError(String error);
    }
    
    /**
     * Interface for disease history callbacks
     */
    public interface DiseaseHistoryCallback {
        void onSuccess(List<PlantDisease> diseaseHistory);
        void onError(String error);
    }
    
    /**
     * Detect plant disease from image
     * @param imageBitmap Plant image bitmap
     * @param imagePath Path to saved image
     * @param userId User ID
     * @param callback Detection callback
     */
    public void detectPlantDisease(Bitmap imageBitmap, String imagePath, int userId, DiseaseDetectionCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private PlantDisease detectedDisease;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Mock AI detection - analyze image properties
                    detectedDisease = performMockDetection(imagePath, userId);
                    
                    // Save to database
                    long diseaseId = plantDiseaseDao.insertPlantDisease(detectedDisease);
                    detectedDisease.setId((int) diseaseId);
                    
                    return "SUCCESS";
                    
                } catch (Exception e) {
                    return "Detection failed: " + e.getMessage();
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if ("SUCCESS".equals(result)) {
                    callback.onSuccess(detectedDisease);
                } else {
                    callback.onError(result);
                }
            }
        }.execute();
    }
    
    /**
     * Get disease detection history for user
     * @param userId User ID
     * @param callback History callback
     */
    public void getDiseaseHistory(int userId, DiseaseHistoryCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private List<PlantDisease> diseaseHistory;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    diseaseHistory = plantDiseaseDao.getPlantDiseasesByUser(userId);
                    return "SUCCESS";
                } catch (Exception e) {
                    return "Failed to load history: " + e.getMessage();
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if ("SUCCESS".equals(result)) {
                    callback.onSuccess(diseaseHistory);
                } else {
                    callback.onError(result);
                }
            }
        }.execute();
    }
    
    /**
     * Perform mock disease detection
     * In production, replace with actual ML model inference
     * @param imagePath Image path
     * @param userId User ID
     * @return PlantDisease object with detection results
     */
    private PlantDisease performMockDetection(String imagePath, int userId) {
        PlantDisease plantDisease = new PlantDisease();
        
        // Randomly select a disease from our database
        String[] diseaseKeys = diseaseDatabase.keySet().toArray(new String[0]);
        String selectedDisease = diseaseKeys[random.nextInt(diseaseKeys.length)];
        DiseaseInfo diseaseInfo = diseaseDatabase.get(selectedDisease);
        
        // Set detection data
        plantDisease.setImagePath(imagePath);
        plantDisease.setUserId(userId);
        plantDisease.setDiseaseName(diseaseInfo.nameEn);
        plantDisease.setDiseaseNameHindi(diseaseInfo.nameHi);
        plantDisease.setDiseaseNameMalayalam(diseaseInfo.nameMl);
        
        plantDisease.setDescription(diseaseInfo.descriptionEn);
        plantDisease.setDescriptionHindi(diseaseInfo.descriptionHi);
        plantDisease.setDescriptionMalayalam(diseaseInfo.descriptionMl);
        
        plantDisease.setSymptoms(diseaseInfo.symptomsEn);
        plantDisease.setSymptomsHindi(diseaseInfo.symptomsHi);
        plantDisease.setSymptomsMalayalam(diseaseInfo.symptomsMl);
        
        plantDisease.setTreatment(diseaseInfo.treatmentEn);
        plantDisease.setTreatmentHindi(diseaseInfo.treatmentHi);
        plantDisease.setTreatmentMalayalam(diseaseInfo.treatmentMl);
        
        plantDisease.setFertilizer(diseaseInfo.fertilizerEn);
        plantDisease.setFertilizerHindi(diseaseInfo.fertilizerHi);
        plantDisease.setFertilizerMalayalam(diseaseInfo.fertilizerMl);
        
        plantDisease.setWaterRequirement(diseaseInfo.waterRequirementEn);
        plantDisease.setWaterRequirementHindi(diseaseInfo.waterRequirementHi);
        plantDisease.setWaterRequirementMalayalam(diseaseInfo.waterRequirementMl);
        
        plantDisease.setApplicationMethod(diseaseInfo.applicationMethodEn);
        plantDisease.setApplicationMethodHindi(diseaseInfo.applicationMethodHi);
        plantDisease.setApplicationMethodMalayalam(diseaseInfo.applicationMethodMl);
        
        plantDisease.setPrevention(diseaseInfo.preventionEn);
        plantDisease.setPreventionHindi(diseaseInfo.preventionHi);
        plantDisease.setPreventionMalayalam(diseaseInfo.preventionMl);
        
        // Set confidence level (0.7 to 0.95 for demo)
        plantDisease.setConfidenceLevel(0.7 + (random.nextDouble() * 0.25));
        
        plantDisease.setCropType(diseaseInfo.cropType);
        
        return plantDisease;
    }
    
    /**
     * Initialize disease database with common Kerala farming diseases
     */
    private void initializeDiseaseDatabase() {
        diseaseDatabase = new HashMap<>();
        
        // Rice Blast Disease
        diseaseDatabase.put("rice_blast", new DiseaseInfo(
            "Rice Blast", "चावल का झुलसा रोग", "നെല്ലിന്റെ ബ്ലാസ്റ്റ് രോഗം",
            "Fungal disease affecting rice leaves and stems", 
            "चावल की पत्तियों और तने को प्रभावित करने वाला फंगल रोग",
            "നെല്ലിന്റെ ഇലകളും തണ്ടും ബാധിക്കുന്ന ഫംഗസ് രോഗം",
            "Gray-green lesions on leaves, wilting", 
            "पत्तियों पर धूसर-हरे धब्बे, मुरझाना", 
            "ഇലകളിൽ ചാരനിറത്തിലുള്ള പാടുകൾ, വാടൽ",
            "Apply fungicide, remove affected parts", 
            "कवकनाशी का प्रयोग करें, प्रभावित भागों को हटाएं",
            "ഫംഗിസൈഡ് പ്രയോഗിക്കുക, രോഗബാധിതമായ ഭാഗങ്ങൾ നീക്കം ചെയ്യുക",
            "Potash fertilizer, balanced NPK", 
            "पोटाश उर्वरक, संतुलित एनपीके",
            "പൊട്ടാഷ് വള, സമതുലിത എൻപികെ",
            "Moderate watering, avoid waterlogging", 
            "मध्यम पानी, जल भराव से बचें",
            "മിതമായ നനവ്, വെള്ളക്കെട്ട് ഒഴിവാക്കുക",
            "Spray in early morning or evening", 
            "सुबह जल्दी या शाम को स्प्रे करें",
            "പുലർച്ചെ അല്ലെങ്കിൽ വൈകുന്നേരം സ്പ്രേ ചെയ്യുക",
            "Keep fields dry, proper drainage", 
            "खेतों को सूखा रखें, उचित जल निकासी",
            "വയലുകൾ വരണ്ടതാക്കി വയ്ക്കുക, നല്ല വെള്ളം തെറി",
            "Rice"
        ));
        
        // Bacterial Leaf Blight
        diseaseDatabase.put("bacterial_blight", new DiseaseInfo(
            "Bacterial Leaf Blight", "जीवाणु पत्ती अंगमारी", "ബാക്ടീരിയൽ ഇല ദ്രവീകരണം",
            "Bacterial disease causing yellowing of rice leaves", 
            "चावल की पत्तियों को पीला करने वाला जीवाणु रोग",
            "നെല്ലിന്റെ ഇലകൾ മഞ്ഞളാകുന്ന ബാക്റ്റീരിയ രോഗം",
            "Yellow streaks along leaf veins", 
            "पत्ती की नसों के साथ पीली धारियां", 
            "ഇലകളുടെ ഞരമ്പുകളിൽ മഞ്ഞ വരകൾ",
            "Copper-based fungicide, pruning", 
            "तांबा आधारित कवकनाशी, छंटाई",
            "കോപ്പർ അടിസ്ഥാനത്തിലുള്ള ഫംഗിസൈഡ്, വള്ളി മുറിക്കുക",
            "Zinc sulphate, urea fertilizer", 
            "जिंक सल्फेट, यूरिया उर्वरक",
            "സിങ്ക് സൾഫേറ്റ്, യൂറിയ വള",
            "Controlled irrigation, avoid overhead watering", 
            "नियंत्रित सिंचाई, ऊपरी पानी से बचें",
            "നിയന്ത്രിത നനയാത്, മുകളിൽ നിന്നുള്ള വെള്ളം ഒഴിവാക്കുക",
            "Apply during cool hours", 
            "ठंडे घंटों में लगाएं",
            "തണുത്ത സമയത്ത് പ്രയോഗിക്കുക",
            "Use resistant varieties, clean farming", 
            "प्रतिरोधी किस्मों का उपयोग, स्वच्छ खेती",
            "പ്രതിരോധശേഷിയുള്ള ഇനങ്ങൾ, വൃത്തിയുള്ള കൃഷി",
            "Rice"
        ));
        
        // Brown Spot Disease
        diseaseDatabase.put("brown_spot", new DiseaseInfo(
            "Brown Spot Disease", "भूरा धब्बा रोग", "തവിട്ടു പുള്ളി രോഗം",
            "Fungal disease causing brown spots on rice leaves", 
            "चावल की पत्तियों पर भूरे धब्बे पैदा करने वाला फंगल रोग",
            "നെല്ലിന്റെ ഇലകളിൽ തവിട്ടു പുള്ളികൾ ഉണ്ടാക്കുന്ന ഫംഗസ് രോഗം",
            "Brown circular spots with yellow halo", 
            "पीले रंग की चारों ओर भूरे गोल धब्बे", 
            "മഞ്ഞ വലയത്തോടുകൂടിയ തവിട്ട് വൃത്താകാര പുള്ളികൾ",
            "Propiconazole fungicide, crop rotation", 
            "प्रोपिकोनाजोल कवकनाशी, फसल चक्र",
            "പ്രൊപികൊനാസോൾ ഫംഗിസൈഡ്, വിള ഭ്രമണം",
            "Balanced NPK with micronutrients", 
            "सूक्ष्म पोषक तत्वों के साथ संतुलित एनपीके",
            "മൈക്രോ ന്യൂട്രിയന്റുകൾ സഹിതമുള്ള സമതുലിത എൻപികെ",
            "Regular irrigation, good drainage", 
            "नियमित सिंचाई, अच्छी जल निकासी",
            "പതിവ് നനയാത്, നല്ല വെള്ളം തെറി",
            "Spray at 15-day intervals", 
            "15 दिन के अंतराल पर स्प्रे करें",
            "15 ദിവസത്തെ ഇടവേളയിൽ സ്പ്രേ ചെയ്യുക",
            "Seed treatment, avoid water stress", 
            "बीज उपचार, पानी के तनाव से बचें",
            "വിത്ത് ചികിത്സ, വെള്ളത്തിന്റെ സമ്മർദം ഒഴിവാക്കുക",
            "Rice"
        ));
        
        // Add Cardamom and Pepper diseases for Kerala
        
        // Cardamom Leaf Spot
        diseaseDatabase.put("cardamom_leaf_spot", new DiseaseInfo(
            "Cardamom Leaf Spot", "इलायची पत्ती धब्बा", "ഏലക്ക ഇല പുള്ളി",
            "Fungal disease affecting cardamom leaves", 
            "इलायची की पत्तियों को प्रभावित करने वाला फंगल रोग",
            "ഏലക്കയുടെ ഇലകളെ ബാധിക്കുന്ന ഫംഗസ് രോഗം",
            "Water-soaked lesions turning brown", 
            "भूरे रंग में बदलने वाले पानी से भीगे घाव", 
            "തവിട്ടുനിറമാകുന്ന വെള്ളം നനഞ്ഞ മുറിവുകൾ",
            "Bordeaux mixture, mancozeb spray", 
            "बोर्डो मिश्रण, मैंकोजेब स्प्रे",
            "ബോർഡോ മിശ്രിതം, മാൻകോസെബ് സ്പ്രേ",
            "Organic compost, phosphorous fertilizer", 
            "जैविक खाद, फास्फोरस उर्वरक",
            "ജൈവ കമ്പോസ്റ്റ്, ഫോസ്ഫറസ് വള",
            "Drip irrigation, avoid leaf wetness", 
            "ड्रिप सिंचाई, पत्ती की नमी से बचें",
            "ഡ്രിപ്പ് ജലസേചനം, ഇലകളുടെ നനവ് ഒഴിവാക്കുക",
            "Apply during dry weather", 
            "सूखे मौसम में लगाएं",
            "വരണ്ട കാലാവസ്ഥയിൽ പ്രയോഗിക്കുക",
            "Proper spacing, remove debris", 
            "उचित दूरी, मलबा हटाना",
            "ശരിയായ അകലം, അവശിഷ്ടങ്ങൾ നീക്കം ചെയ്യുക",
            "Cardamom"
        ));
        
        // Black Pepper Quick Wilt
        diseaseDatabase.put("pepper_quick_wilt", new DiseaseInfo(
            "Black Pepper Quick Wilt", "काली मिर्च त्वरित मुरझाना", "കുരുമുളക് പെട്ടെന്നുള്ള വാടൽ",
            "Soil-borne fungal disease affecting pepper vines", 
            "मिर्च की बेलों को प्रभावित करने वाला मिट्टी जनित फंगल रोग",
            "കുരുമുളക് വള്ളികളെ ബാധിക്കുന്ന മണ്ണിൽ നിന്നുള്ള ഫംഗസ് രോഗം",
            "Yellowing of leaves, wilting of vines", 
            "पत्तियों का पीला होना, बेलों का मुरझाना", 
            "ഇലകൾ മഞ്ഞളാകൽ, വള്ളികളുടെ വാടൽ",
            "Trichoderma, copper oxychloride", 
            "ट्राइकोडर्मा, कॉपर ऑक्सीक्लोराइड",
            "ട്രൈക്കോഡെർമ, കോപ്പർ ഓക്സിക്ലോറൈഡ്",
            "Biocompost, neem cake", 
            "बायोकंपोस्ट, नीम केक",
            "ബയോകമ്പോസ്റ്റ്, വേപ്പ് പിണ്ണാക്ക്",
            "Controlled irrigation, proper drainage", 
            "नियंत्रित सिंचाई, उचित जल निकासी",
            "നിയന്ത്രിത ജലസേചനം, ശരിയായ വെള്ളം തെറി",
            "Soil application, drench method", 
            "मिट्टी में डालना, भिगोना विधि",
            "മണ്ണിൽ പ്രയോഗിക്കൽ, നനയാത്ത രീതി",
            "Plant resistant varieties, soil treatment", 
            "प्रतिरोधी किस्में लगाना, मिट्टी का उपचार",
            "പ്രതിരോധശേഷിയുള്ള ഇനങ്ങൾ നടുക, മണ്ണ് ചികിത്സ",
            "Black Pepper"
        ));
    }
    
    /**
     * Helper class to store disease information in multiple languages
     */
    private static class DiseaseInfo {
        String nameEn, nameHi, nameMl;
        String descriptionEn, descriptionHi, descriptionMl;
        String symptomsEn, symptomsHi, symptomsMl;
        String treatmentEn, treatmentHi, treatmentMl;
        String fertilizerEn, fertilizerHi, fertilizerMl;
        String waterRequirementEn, waterRequirementHi, waterRequirementMl;
        String applicationMethodEn, applicationMethodHi, applicationMethodMl;
        String preventionEn, preventionHi, preventionMl;
        String cropType;
        
        DiseaseInfo(String nameEn, String nameHi, String nameMl,
                   String descriptionEn, String descriptionHi, String descriptionMl,
                   String symptomsEn, String symptomsHi, String symptomsMl,
                   String treatmentEn, String treatmentHi, String treatmentMl,
                   String fertilizerEn, String fertilizerHi, String fertilizerMl,
                   String waterRequirementEn, String waterRequirementHi, String waterRequirementMl,
                   String applicationMethodEn, String applicationMethodHi, String applicationMethodMl,
                   String preventionEn, String preventionHi, String preventionMl,
                   String cropType) {
            this.nameEn = nameEn;
            this.nameHi = nameHi;
            this.nameMl = nameMl;
            this.descriptionEn = descriptionEn;
            this.descriptionHi = descriptionHi;
            this.descriptionMl = descriptionMl;
            this.symptomsEn = symptomsEn;
            this.symptomsHi = symptomsHi;
            this.symptomsMl = symptomsMl;
            this.treatmentEn = treatmentEn;
            this.treatmentHi = treatmentHi;
            this.treatmentMl = treatmentMl;
            this.fertilizerEn = fertilizerEn;
            this.fertilizerHi = fertilizerHi;
            this.fertilizerMl = fertilizerMl;
            this.waterRequirementEn = waterRequirementEn;
            this.waterRequirementHi = waterRequirementHi;
            this.waterRequirementMl = waterRequirementMl;
            this.applicationMethodEn = applicationMethodEn;
            this.applicationMethodHi = applicationMethodHi;
            this.applicationMethodMl = applicationMethodMl;
            this.preventionEn = preventionEn;
            this.preventionHi = preventionHi;
            this.preventionMl = preventionMl;
            this.cropType = cropType;
        }
    }
    
    /**
     * Get disease information in specified language
     */
    public String getDiseaseNameByLanguage(PlantDisease disease, String language) {
        switch (language) {
            case "hi": return disease.getDiseaseNameHindi();
            case "ml": return disease.getDiseaseNameMalayalam();
            default: return disease.getDiseaseName();
        }
    }
    
    public String getDescriptionByLanguage(PlantDisease disease, String language) {
        switch (language) {
            case "hi": return disease.getDescriptionHindi();
            case "ml": return disease.getDescriptionMalayalam();
            default: return disease.getDescription();
        }
    }
    
    public String getTreatmentByLanguage(PlantDisease disease, String language) {
        switch (language) {
            case "hi": return disease.getTreatmentHindi();
            case "ml": return disease.getTreatmentMalayalam();
            default: return disease.getTreatment();
        }
    }
    
    public String getFertilizerByLanguage(PlantDisease disease, String language) {
        switch (language) {
            case "hi": return disease.getFertilizerHindi();
            case "ml": return disease.getFertilizerMalayalam();
            default: return disease.getFertilizer();
        }
    }
    
    /**
     * Clean old disease detection records
     */
    public void cleanOldRecords() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                long sixMonthsAgo = System.currentTimeMillis() - (6 * 30 * 24 * 60 * 60 * 1000L);
                plantDiseaseDao.deleteOldRecords(sixMonthsAgo);
                return null;
            }
        }.execute();
    }
}
