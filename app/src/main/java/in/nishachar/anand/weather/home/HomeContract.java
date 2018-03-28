package in.nishachar.anand.weather.home;

import android.support.annotation.NonNull;

import in.nishachar.anand.weather.models.WeatherResponse;

/**
 * Created by anand on 12/03/18.
 */

public interface HomeContract {
    /**
     * interface to make UI calls
     */
    interface View {
        /**
         * method to show current weather data.
         *
         * @param weatherResponse current weather response.
         */
        void showWeatherData(WeatherResponse weatherResponse);
    }

    /**
     * interface to make user actions.
     */
    interface UserActionsListener {
        /**
         * method to load chat log.
         *
         * @param city query city.
         * @param key  api key
         */
        void fetchCurrentWeather(@NonNull String key, String city);

        /**
         * method to load forecast
         * @param city query city.
         * @param key  api key.
         * @param days number of days.
         */
        void fetchForeCastWeather(@NonNull String key, String city, Integer days);
    }
}
