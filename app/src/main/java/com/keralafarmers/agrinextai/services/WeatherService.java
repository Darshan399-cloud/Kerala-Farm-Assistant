package com.keralafarmers.agrinextai.services;

import android.content.Context;
import android.os.AsyncTask;
import com.keralafarmers.agrinextai.database.AppDatabase;
import com.keralafarmers.agrinextai.database.WeatherDao;
import com.keralafarmers.agrinextai.models.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Weather Service for handling weather data operations
 * Note: This is a mock implementation. In production, integrate with actual weather APIs like OpenWeatherMap
 */
public class WeatherService {
    
    private Context context;
    private WeatherDao weatherDao;
    private SimpleDateFormat dateFormat;
    private Random random;
    
    public WeatherService(Context context) {
        this.context = context;
        this.weatherDao = AppDatabase.getInstance(context).weatherDao();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.random = new Random();
    }
    
    /**
     * Interface for weather callbacks
     */
    public interface WeatherCallback {
        void onSuccess(List<Weather> weatherList);
        void onError(String error);
    }
    
    /**
     * Interface for single weather data callback
     */
    public interface SingleWeatherCallback {
        void onSuccess(Weather weather);
        void onError(String error);
    }
    
    /**
     * Get weather data for past 5 days and future 5 days
     * @param latitude User's latitude
     * @param longitude User's longitude
     * @param callback Weather callback
     */
    public void getWeatherData(double latitude, double longitude, WeatherCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private List<Weather> weatherList;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    weatherList = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    
                    // Get past 5 days weather
                    for (int i = 5; i >= 1; i--) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.add(Calendar.DAY_OF_YEAR, -i);
                        String date = dateFormat.format(calendar.getTime());
                        
                        Weather weather = weatherDao.getWeatherByDate(date);
                        if (weather == null) {
                            weather = generateMockWeatherData(date, latitude, longitude, true);
                            weatherDao.insertWeather(weather);
                        }
                        weatherList.add(weather);
                    }
                    
                    // Get current day weather
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    String currentDate = dateFormat.format(calendar.getTime());
                    Weather currentWeather = weatherDao.getWeatherByDate(currentDate);
                    if (currentWeather == null) {
                        currentWeather = generateMockWeatherData(currentDate, latitude, longitude, false);
                        weatherDao.insertWeather(currentWeather);
                    }
                    weatherList.add(currentWeather);
                    
                    // Get future 5 days weather
                    for (int i = 1; i <= 5; i++) {
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.add(Calendar.DAY_OF_YEAR, i);
                        String date = dateFormat.format(calendar.getTime());
                        
                        Weather weather = weatherDao.getWeatherByDate(date);
                        if (weather == null) {
                            weather = generateMockWeatherData(date, latitude, longitude, false);
                            weatherDao.insertWeather(weather);
                        }
                        weatherList.add(weather);
                    }
                    
                    return "SUCCESS";
                    
                } catch (Exception e) {
                    return "Error loading weather data: " + e.getMessage();
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if ("SUCCESS".equals(result)) {
                    callback.onSuccess(weatherList);
                } else {
                    callback.onError(result);
                }
            }
        }.execute();
    }
    
    /**
     * Get current weather data
     * @param latitude User's latitude
     * @param longitude User's longitude
     * @param callback Single weather callback
     */
    public void getCurrentWeather(double latitude, double longitude, SingleWeatherCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private Weather currentWeather;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String currentDate = dateFormat.format(System.currentTimeMillis());
                    currentWeather = weatherDao.getWeatherByDate(currentDate);
                    
                    if (currentWeather == null) {
                        currentWeather = generateMockWeatherData(currentDate, latitude, longitude, false);
                        weatherDao.insertWeather(currentWeather);
                    }
                    
                    return "SUCCESS";
                    
                } catch (Exception e) {
                    return "Error loading current weather: " + e.getMessage();
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if ("SUCCESS".equals(result)) {
                    callback.onSuccess(currentWeather);
                } else {
                    callback.onError(result);
                }
            }
        }.execute();
    }
    
    /**
     * Generate mock weather data for demonstration
     * In production, replace this with actual API calls
     * @param date Date for weather data
     * @param latitude Latitude
     * @param longitude Longitude
     * @param isPast Whether this is past data
     * @return Mock weather object
     */
    private Weather generateMockWeatherData(String date, double latitude, double longitude, boolean isPast) {
        Weather weather = new Weather();
        weather.setDate(date);
        weather.setLatitude(latitude);
        weather.setLongitude(longitude);
        weather.setLocation("Kerala, India");
        
        // Generate realistic weather data for Kerala
        String[] conditions = {"sunny", "partly_cloudy", "cloudy", "rainy", "thunderstorm"};
        String[] descriptions = {"Clear sky", "Partly cloudy", "Cloudy", "Light rain", "Thunderstorm"};
        
        int conditionIndex = random.nextInt(conditions.length);
        weather.setWeatherCondition(conditions[conditionIndex]);
        weather.setWeatherDescription(descriptions[conditionIndex]);
        
        // Temperature ranges typical for Kerala (24-35°C)
        double baseTemp = 24 + random.nextDouble() * 11; // 24-35°C
        weather.setTemperature(Math.round(baseTemp * 10.0) / 10.0);
        weather.setMinTemperature(Math.round((baseTemp - 2 - random.nextDouble() * 3) * 10.0) / 10.0);
        weather.setMaxTemperature(Math.round((baseTemp + 2 + random.nextDouble() * 3) * 10.0) / 10.0);
        
        // Humidity typical for Kerala (60-90%)
        weather.setHumidity(60 + random.nextInt(31));
        
        // Rainfall (0-50mm for demonstration)
        if (conditionIndex >= 3) { // rainy or thunderstorm
            weather.setRainfall(Math.round((random.nextDouble() * 50) * 10.0) / 10.0);
        } else {
            weather.setRainfall(0.0);
        }
        
        // Wind speed (5-25 km/h)
        weather.setWindSpeed(Math.round((5 + random.nextDouble() * 20) * 10.0) / 10.0);
        
        weather.setWeatherIcon(getWeatherIcon(conditions[conditionIndex]));
        
        return weather;
    }
    
    /**
     * Get weather icon based on condition
     * @param condition Weather condition
     * @return Icon identifier
     */
    private String getWeatherIcon(String condition) {
        switch (condition) {
            case "sunny":
                return "☀️";
            case "partly_cloudy":
                return "⛅";
            case "cloudy":
                return "☁️";
            case "rainy":
                return "🌧️";
            case "thunderstorm":
                return "⛈️";
            default:
                return "🌤️";
        }
    }
    
    /**
     * Get weather advice based on conditions
     * @param weather Weather object
     * @return Farming advice string
     */
    public String getWeatherAdvice(Weather weather) {
        StringBuilder advice = new StringBuilder();
        
        // Temperature advice
        if (weather.getTemperature() > 32) {
            advice.append("🌡️ High temperature today. Ensure adequate irrigation. ");
        } else if (weather.getTemperature() < 20) {
            advice.append("🌡️ Cool weather. Good for leafy vegetables. ");
        }
        
        // Rainfall advice
        if (weather.getRainfall() > 20) {
            advice.append("🌧️ Heavy rain expected. Check drainage systems. ");
        } else if (weather.getRainfall() > 0) {
            advice.append("🌧️ Light rain expected. Good for transplanting. ");
        } else {
            advice.append("☀️ No rain expected. Plan irrigation accordingly. ");
        }
        
        // Humidity advice
        if (weather.getHumidity() > 80) {
            advice.append("💧 High humidity. Watch for fungal diseases. ");
        }
        
        // Wind advice
        if (weather.getWindSpeed() > 20) {
            advice.append("💨 Strong winds expected. Secure tall plants. ");
        }
        
        return advice.toString();
    }
    
    /**
     * Clean old weather data (older than 30 days)
     */
    public void cleanOldWeatherData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                long thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L);
                weatherDao.deleteOldWeatherData(thirtyDaysAgo);
                return null;
            }
        }.execute();
    }
}