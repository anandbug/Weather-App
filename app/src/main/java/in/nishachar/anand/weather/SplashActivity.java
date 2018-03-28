package in.nishachar.anand.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import in.nishachar.anand.weather.home.HomeActivity;

/**
 * Created by anand on 14/03/18.
 */

public class SplashActivity extends AppCompatActivity {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        callWelcome();
    }

    /**
     * Method to call {@link HomeActivity}
     */
    private void callWelcome() {
        Intent welcomeIntent = new Intent(this, HomeActivity.class);
        startActivity(welcomeIntent);

        finish();
    }
}
