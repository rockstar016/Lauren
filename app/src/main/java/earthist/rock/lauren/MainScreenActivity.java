package earthist.rock.lauren;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import earthist.rock.lauren.adapters.first_screen_adapter;
import me.relex.circleindicator.CircleIndicator;

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
        indicator = (CircleIndicator)findViewById(R.id.indicator);
        pager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new first_screen_adapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
        pager.setCurrentItem(0);
        love_button = (ImageButton)findViewById(R.id.button_star_love);
        love_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStarLove();
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth != null)
            auth.signOut();
    }
    @Override
    public void onBackPressed() {
        FaceBookLogout();
        FirebaseLogout();
        finish();
    }
}
