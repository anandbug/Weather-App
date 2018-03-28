package in.nishachar.anand.weather.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.anand.weather.R;
import in.nishachar.anand.weather.models.ForecastDay;
import in.nishachar.anand.weather.models.WeatherResponse;
import in.nishachar.anand.weather.shared.APIClient;
import in.nishachar.anand.weather.shared.ApiService;

/**
 * A ForeCastFragment fragment containing a forecast view.
 */
public class ForeCastFragment extends Fragment implements HomeContract.View {

    @BindView(R.id.weatherCard)
    View weatherCard;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.weatherLayout)
    View weatherLayout;

    @BindView(R.id.foreCastRecycler)
    RecyclerView foreCastRecycler;

    private String city;

    public ForeCastFragment() {}
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ForeCastFragment newInstance(String city) {
        ForeCastFragment foreCastFragment = new ForeCastFragment();
        Bundle args = new Bundle();
        args.putString(HomeActivity.CITY, city);
        foreCastFragment.setArguments(args);
        return foreCastFragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

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
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

        HomeContract.UserActionsListener mUserActionsListener = new HomePresenter(APIClient.getClient().create(ApiService.class),this);

        if (city != null)
            mUserActionsListener.fetchForeCastWeather(getString(R.string.api_key), city, 5);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void showWeatherData(WeatherResponse weatherResponse) {
        if (getActivity() == null) {
            return;
        }

        if (weatherResponse != null) {
            List<ForecastDay> forecastDays = weatherResponse.getForecast().getForecastDays();

            if(forecastDays.size() == 5) {
                //discarding first two days
                forecastDays.remove(0);
                forecastDays.remove(0);

                final ForeCastRecyclerAdapter foreCastRecyclerAdapter = new ForeCastRecyclerAdapter(forecastDays);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        foreCastRecycler.setAdapter(foreCastRecyclerAdapter);
                        weatherCard.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .alpha(1)
                                .setInterpolator(new BounceInterpolator())
                                .setDuration(750)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        progressBar.setVisibility(View.GONE);
                                        weatherCard.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                });

            }

        }

    }
}
