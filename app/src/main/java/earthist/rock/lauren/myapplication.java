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
//        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
        // Initialize the SDK before executing any other operations,
        AppEventsLogger.activateApp(this);
        getHashKey();
    }

    public void getHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "earthist.rock.lauren",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash value:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
