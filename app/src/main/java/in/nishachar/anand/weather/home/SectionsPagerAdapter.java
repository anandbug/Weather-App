package in.nishachar.anand.weather.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    private String city;
    SectionsPagerAdapter(FragmentManager fm, String city) {
        super(fm);
        this.city = city;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return position == 0 ? TodayFragment.newInstance(city) :
                position == 1 ? TomorrowFragment.newInstance(city) :
                        ForeCastFragment.newInstance(city);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
