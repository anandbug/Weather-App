package in.nishachar.anand.weather.shared;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by anand on 13/03/18.
 */

public class UserPrefManager {
    // Shared Preferences reference
    private SharedPreferences pref;

    // Shared pref file name
    private static final String PREFER_NAME = "weatherUser";

    // All Shared Preferences Keys
    private static final String USER_CITY = "userCity";

    /**
     * constructor for initializing the object parameters
     * @param context context for contextual calls.
     */
    public UserPrefManager(Context context){
        pref = context.getSharedPreferences(PREFER_NAME, 0);
    }

    /**
     * method to create a session and save credentials
     * @param city first name of the user
     */
    public void setUserCity(String city){
        // Editor reference for Shared preferences
        SharedPreferences.Editor editor = pref.edit();
        // Storing city name in pref
        editor.putString(USER_CITY, city);
        // commit changes
        editor.apply();
    }

    /**
     * method to read access token
     * @return access token
     */
    public String getUserCity(){
        return pref.getString(USER_CITY, "Bengalooru");
    }
}
