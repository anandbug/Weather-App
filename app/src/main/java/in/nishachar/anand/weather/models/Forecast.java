package in.nishachar.anand.weather.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anand on 12/03/18.
 */

public class Forecast {
    @SerializedName("forecastday")
    @Expose
    private List<ForecastDay> forecastDays = null;

    public List<ForecastDay> getForecastDays() {
        return forecastDays;
    }

    public void setForecastday(List<ForecastDay> forecastDays) {
        this.forecastDays = forecastDays;
    }

}
