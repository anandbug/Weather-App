package in.nishachar.anand.weather.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.anand.weather.R;
import in.nishachar.anand.weather.models.LocationResponse;
import in.nishachar.anand.weather.models.WeatherResponse;
import in.nishachar.anand.weather.shared.APIClient;
import in.nishachar.anand.weather.shared.ApiService;
import in.nishachar.anand.weather.shared.UserPrefManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String CITY = "city";
    private String TAG = "HomeActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SearchView searchView;

    private UserPrefManager userPrefManager;

    private ApiService mApiService;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userPrefManager = new UserPrefManager(this);

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        viewPagerSetUp();

        mApiService = APIClient.getClient().create(ApiService.class);
    }

    private void viewPagerSetUp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), userPrefManager.getUserCity());

                // Set up the ViewPager with the sections adapter.
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = findViewById(R.id.tabs);

                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Do not iconify the widget; expand it by default
        searchView.setIconifiedByDefault(false);
        //hint text color
        ((EditText) searchView.findViewById(R.id.search_src_text)).setHintTextColor(Color.WHITE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setSuggestionsAdapter(null);
                return true;
            }
        });

        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(this);

        /*searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    searchView.requestFocus();
            }
        });*/

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("I am search ---" + query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        makeSearchCall(newText);
        return true;
    }

    private void makeSearchCall(String query) {
        mApiService.doGetSearchResponse(getString(R.string.api_key), query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private DisposableObserver<List<LocationResponse>> getObserver(){
        return new DisposableObserver<List<LocationResponse>>() {

            @Override
            public void onNext(@NonNull final List<LocationResponse> locationResponses) {
                Log.d(TAG,"OnNext");

                // Cursor
                String[] columns = new String[]{"_id", "text"};
                Object[] temp = new Object[]{0, "default"};

                MatrixCursor matrixCursor = new MatrixCursor(columns);
                if (locationResponses.size() > 0) {
                    for (int i = 0; i < locationResponses.size(); i++) {

                        temp[0] = locationResponses.get(i).getId();
                        temp[1] = locationResponses.get(i).getName();
                        //replaced s with i as s not used anywhere.
                        matrixCursor.addRow(temp);
                    }
                }

                SearchListAdapter searchListAdapter = new SearchListAdapter(HomeActivity.this, matrixCursor, locationResponses);

                // SearchView
                searchView.setSuggestionsAdapter(searchListAdapter);

                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    @Override
                    public boolean onSuggestionSelect(int position) {
                        return false;
                    }

                    @Override
                    public boolean onSuggestionClick(int position) {
                        String city = locationResponses.get(position).getName().split(",")[0];
                        userPrefManager.setUserCity(city);
                        viewPagerSetUp();
                        searchView.setQuery(city, true);
                        searchView.clearFocus();
                        return false;
                    }
                });
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error" + e);
                e.printStackTrace();
                //mvi.displayError("Error fetching Movie Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
            }
        };
    }
}
