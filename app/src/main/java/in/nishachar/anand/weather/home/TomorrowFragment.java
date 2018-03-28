package in.nishachar.anand.weather.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.anand.weather.R;
import in.nishachar.anand.weather.models.ForecastDay;
import in.nishachar.anand.weather.models.WeatherResponse;
import in.nishachar.anand.weather.shared.APIClient;
import in.nishachar.anand.weather.shared.ApiService;
import in.nishachar.anand.weather.shared.GlideApp;

/**
 * A TomorrowFragment fragment containing a tomorrow's view.
 */
public class TomorrowFragment extends Fragment implements HomeContract.View {
    @BindView(R.id.weatherCard)
    View weatherCard;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.weatherLayout)
    View weatherLayout;

    @BindView(R.id.cityText)
    AppCompatTextView cityText;

    @BindView(R.id.temperatureText)
    AppCompatTextView temperatureText;

    @BindView(R.id.windSpeedText)
    AppCompatTextView windSpeedText;

    @BindView(R.id.humidityText)
    AppCompatTextView humidityText;

    @BindView(R.id.weatherImage)
    AppCompatImageView weatherImage;

    @BindView(R.id.statusText)
    AppCompatTextView statusText;

    @BindView(R.id.dateText)
    AppCompatTextView dateText;

    private String city;

    public TomorrowFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TomorrowFragment newInstance(String city) {
        TomorrowFragment tomorrowFragment = new TomorrowFragment();
        Bundle args = new Bundle();
        args.putString(HomeActivity.CITY, city);
        tomorrowFragment.setArguments(args);
        return tomorrowFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tomorrow, container, false);

        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if (args != null) {
            city = args.getString(HomeActivity.CITY);
        }

        AnimationDrawable animationDrawable = (AnimationDrawable) weatherLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        return rootView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        HomeContract.UserActionsListener mUserActionsListener = new HomePresenter(APIClient.getClient().create(ApiService.class), this);

        if (city != null)
            mUserActionsListener.fetchForeCastWeather(getString(R.string.api_key), city, 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWeatherData(final WeatherResponse weatherResponse) {
        if (getActivity() == null) {
            return;
        }

        if (weatherResponse != null) {
            final List<ForecastDay> forecastDays = weatherResponse.getForecast().getForecastDays();

            final SimpleDateFormat receivedFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            final SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, MMMM dd", Locale.ENGLISH);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (forecastDays.size() == 2) {
                        ForecastDay forecastDay = forecastDays.get(1);
                        if (forecastDay != null) {
                            cityText.setText(weatherResponse.getLocation().getName());
                            temperatureText.setText(getString(R.string.temp_format, forecastDay.getDay().getAvgtempC()));

                            humidityText.setText(getString(R.string.humidity_format, forecastDay.getDay().getAvghumidity()));
                            windSpeedText.setText(getString(R.string.wind_speed_format, forecastDay.getDay().getMaxwindKph()));

                            try {
                                Date date = receivedFormat.parse(forecastDay.getDate());
                                dateText.setText(newFormat.format(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            statusText.setText(forecastDay.getDay().getCondition().getText());

                            GlideApp.with(getContext())
                                    .load("http:" + forecastDay.getDay().getCondition().getIcon())
                                    .into(weatherImage);

                            weatherCard.animate()
                                    .scaleX(1)
                                    .scaleY(1)
                                    .alpha(1)
                                    .setInterpolator(new BounceInterpolator())
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            progressBar.setVisibility(View.GONE);
                                            weatherCard.setVisibility(View.VISIBLE);
                                        }
                                    });

                        }
                    }
                }
            });
        }
    }
}
