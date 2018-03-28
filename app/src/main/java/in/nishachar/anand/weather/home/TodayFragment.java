package in.nishachar.anand.weather.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.anand.weather.R;
import in.nishachar.anand.weather.models.WeatherResponse;
import in.nishachar.anand.weather.shared.APIClient;
import in.nishachar.anand.weather.shared.ApiService;
import in.nishachar.anand.weather.shared.GlideApp;

import static in.nishachar.anand.weather.home.HomeActivity.CITY;

/**
 * A TodayFragment fragment containing a today's view.
 */
public class TodayFragment extends Fragment implements HomeContract.View {

    private String TAG = "TodayFragment";

    @BindView(R.id.weatherCard)
    View weatherCard;

    @BindView(R.id.weatherLayout)
    View weatherLayout;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.cityText)
    AppCompatTextView cityText;

    @BindView(R.id.temperatureText)
    AppCompatTextView temperatureText;

    @BindView(R.id.windSpeedText)
    AppCompatTextView windSpeedText;

    @BindView(R.id.windDegreeText)
    AppCompatTextView windDegreeText;

    @BindView(R.id.humidityText)
    AppCompatTextView humidityText;

    @BindView(R.id.cloudText)
    AppCompatTextView cloudText;

    @BindView(R.id.weatherImage)
    AppCompatImageView weatherImage;

    @BindView(R.id.statusText)
    AppCompatTextView statusText;

    @BindView(R.id.dateText)
    AppCompatTextView dateText;

    private String city;

    private HomeContract.UserActionsListener mUserActionsListener;

    public TodayFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TodayFragment newInstance(String city) {
        TodayFragment todayFragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(HomeActivity.CITY, city);
        todayFragment.setArguments(args);
        return todayFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

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

        setRetainInstance(true);

        mUserActionsListener = new HomePresenter(APIClient.getClient().create(ApiService.class), this);

        if (city != null)
            mUserActionsListener.fetchCurrentWeather(getString(R.string.api_key), city);

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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cityText.setText(weatherResponse.getLocation().getName());
                    temperatureText.setText(getString(R.string.temp_format, weatherResponse.getCurrent().getTempC()));
                    windSpeedText.setText(getString(R.string.wind_speed_format, weatherResponse.getCurrent().getWindKph()));
                    windDegreeText.setText(getString(R.string.wind_degree_format, weatherResponse.getCurrent().getWindDegree(), weatherResponse.getCurrent().getWindDir()));
                    humidityText.setText(getString(R.string.humidity_format, weatherResponse.getCurrent().getHumidity()));

                    cloudText.setText(weatherResponse.getCurrent().getCloud() < 25 ? getString(R.string.no_cloud) :
                            weatherResponse.getCurrent().getCloud() < 50 ? getString(R.string.partial_clouds) :
                                    weatherResponse.getCurrent().getCloud() < 75 ? getString(R.string.cloudy) :
                                            getString(R.string.full_cloud));

                    SimpleDateFormat receivedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, h:mm a", Locale.ENGLISH);

                    try {
                        Date date = receivedFormat.parse(weatherResponse.getLocation().getLocaltime());
                        dateText.setText(newFormat.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    statusText.setText(weatherResponse.getCurrent().getCondition().getText());

                    GlideApp.with(getContext())
                            .load("http:" + weatherResponse.getCurrent().getCondition().getIcon())
                            .into(weatherImage);

                    weatherCard.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .alpha(1)
                            .setDuration(750)
                            .setInterpolator(new BounceInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    progressBar.setVisibility(View.GONE);
                                    weatherCard.setVisibility(View.VISIBLE);
                                }
                            });
                }
            });

        } else {
            Log.d(TAG, "WeatherResponse is null");
        }
    }
}
