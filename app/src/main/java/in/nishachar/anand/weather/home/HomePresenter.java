package in.nishachar.anand.weather.home;

import android.support.annotation.NonNull;
import android.util.Log;

import in.nishachar.anand.weather.models.WeatherResponse;
import in.nishachar.anand.weather.shared.ApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by anand on 12/03/18.
 */

public class HomePresenter implements HomeContract.UserActionsListener {
    private ApiService mApiService;
    private HomeContract.View mHomeView;
    private String TAG = "HomePresenter";

    HomePresenter(@NonNull ApiService mApiService, @NonNull HomeContract.View mHomeView) {
        this.mHomeView = checkNotNull(mHomeView);
        this.mApiService = checkNotNull(mApiService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetchCurrentWeather(@NonNull String key, String city) {
        mApiService.doGetWeatherCurrentResponse(key, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fetchForeCastWeather(@NonNull String city, String key, Integer days) {
        mApiService.doGetWeatherForeCastResponse(city, key, days)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private DisposableObserver<WeatherResponse> getObserver(){
        return new DisposableObserver<WeatherResponse>() {

            @Override
            public void onNext(@NonNull WeatherResponse weatherResponse) {
                mHomeView.showWeatherData(weatherResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error" + e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
            }
        };
    }
}
