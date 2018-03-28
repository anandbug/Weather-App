package in.nishachar.anand.weather.shared;

import java.util.List;

import in.nishachar.anand.weather.models.LocationResponse;
import in.nishachar.anand.weather.models.WeatherResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anand on 12/03/18.
 */

public interface ApiService {

    String BASE_URL = "http://api.apixu.com/v1/";

    @GET("current.json")
    Observable<WeatherResponse> doGetWeatherCurrentResponse(@Query("key") String key, @Query("q") String query);

    @GET("forecast.json")
    Observable<WeatherResponse> doGetWeatherForeCastResponse(@Query("key") String key, @Query("q") String query, @Query("days") Integer days);

    @GET("search.json")
    Observable<List<LocationResponse>> doGetSearchResponse(@Query("key") String key, @Query("q") String query);
}
