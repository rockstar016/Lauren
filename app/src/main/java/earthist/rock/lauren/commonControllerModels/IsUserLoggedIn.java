package earthist.rock.lauren.commonControllerModels;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by RockStar-0116 on 2016.07.25.
 */
public class IsUserLoggedIn {
    public static boolean isLoggedIn(Context context){

        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        String result = sp.getString("token","");
        //return !result.isEmpty();
        return false;
    }
    public static void setUserLogin(Context context){

    }
}
