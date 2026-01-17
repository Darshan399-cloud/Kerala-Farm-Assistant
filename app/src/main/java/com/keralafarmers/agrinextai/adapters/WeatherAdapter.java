package com.keralafarmers.agrinextai.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.keralafarmers.agrinextai.R;
import com.keralafarmers.agrinextai.models.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying weather data in RecyclerView
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    
    private Context context;
    private List<Weather> weatherList;
    private SimpleDateFormat inputFormat;
    private SimpleDateFormat outputFormat;
    private SimpleDateFormat dayFormat;
    
    public WeatherAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
        this.inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.outputFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
        this.dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        
        // Format date
        try {
            Date date = inputFormat.parse(weather.getDate());
            if (date != null) {
                String formattedDate = outputFormat.format(date);
                String dayName = getDayName(date);
                
                holder.tvDate.setText(formattedDate);
                holder.tvDay.setText(dayName);
            }
        } catch (ParseException e) {
            holder.tvDate.setText(weather.getDate());
            holder.tvDay.setText("");
        }
        
        // Set weather icon
        holder.tvWeatherIcon.setText(weather.getWeatherIcon());
        
        // Set temperatures
        holder.tvMaxTemp.setText(String.format(Locale.getDefault(), "%.0f°", weather.getMaxTemperature()));
        holder.tvMinTemp.setText(String.format(Locale.getDefault(), "%.0f°", weather.getMinTemperature()));
        
        // Set weather details
        holder.tvHumidity.setText(String.format(Locale.getDefault(), "%d%%", weather.getHumidity()));
        
        if (weather.getRainfall() > 0) {
            holder.tvRainfall.setText(String.format(Locale.getDefault(), "%.1fmm", weather.getRainfall()));
        } else {
            holder.tvRainfall.setText("0mm");
        }
        
        holder.tvWindSpeed.setText(String.format(Locale.getDefault(), "%.0f km/h", weather.getWindSpeed()));
    }
    
    @Override
    public int getItemCount() {
        return weatherList != null ? weatherList.size() : 0;
    }
    
    /**
     * Get day name for the date
     * @param date Date object
     * @return Day name or relative name (Today, Tomorrow, Yesterday)
     */
    private String getDayName(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar weatherDate = Calendar.getInstance();
        weatherDate.setTime(date);
        
        // Check if it's today
        if (isSameDay(today, weatherDate)) {
            return context.getString(R.string.today);
        }
        
        // Check if it's tomorrow
        today.add(Calendar.DAY_OF_YEAR, 1);
        if (isSameDay(today, weatherDate)) {
            return context.getString(R.string.tomorrow);
        }
        
        // Check if it's yesterday
        today.add(Calendar.DAY_OF_YEAR, -2);
        if (isSameDay(today, weatherDate)) {
            return context.getString(R.string.yesterday);
        }
        
        // Return day name
        return dayFormat.format(date);
    }
    
    /**
     * Check if two calendars represent the same day
     * @param cal1 First calendar
     * @param cal2 Second calendar
     * @return True if same day
     */
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Update weather list data
     * @param newWeatherList New weather list
     */
    public void updateWeatherList(List<Weather> newWeatherList) {
        this.weatherList = newWeatherList;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for weather items
     */
    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDay, tvWeatherIcon, tvMaxTemp, tvMinTemp;
        TextView tvHumidity, tvRainfall, tvWindSpeed;
        
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvWeatherIcon = itemView.findViewById(R.id.tvWeatherIcon);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvRainfall = itemView.findViewById(R.id.tvRainfall);
            tvWindSpeed = itemView.findViewById(R.id.tvWindSpeed);
        }
    }
}