package earthist.rock.lauren;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;


import earthist.rock.lauren.adapters.first_screen_adapter;
import me.relex.circleindicator.CircleIndicator;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainScreenActivity extends AppCompatActivity {
    ViewPager pager;
    CircleIndicator indicator;
    first_screen_adapter adapter;
    ImageButton love_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_main_screen);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.INTERNET}, 120);
            }
        }
        indicator = (CircleIndicator)findViewById(R.id.indicator);
        pager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new first_screen_adapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
        pager.setCurrentItem(0);
        love_button = (ImageButton) findViewById(R.id.button_star_love);
        love_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStarLove();
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 120) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use this app without permission.", Toast.LENGTH_LONG).show();
                finish();
            }
            return;
        }
    }
    private void FaceBookLogout(){
       AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null)
            LoginManager.getInstance().logOut();
    }
    private void onClickStarLove(){
        Intent i = new Intent(MainScreenActivity.this, Wander_GypCActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("index",pager.getCurrentItem());
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
    private void FirebaseLogout(){

    }
    @Override
    public void onBackPressed() {
        FaceBookLogout();
        FirebaseLogout();
        finish();
    }
}
