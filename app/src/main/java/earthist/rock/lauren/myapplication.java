package earthist.rock.lauren;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
//import com.firebase.client.Firebase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by RockStar0116 on 2016.07.28.
 */
public class myapplication extends Application {
    // Updated your class body:
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        // Initialize the SDK before executing any other operations,
        AppEventsLogger.activateApp(this);
    }
}
