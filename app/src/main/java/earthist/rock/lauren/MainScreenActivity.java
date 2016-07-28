package earthist.rock.lauren;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import earthist.rock.lauren.adapters.first_screen_adapter;
import earthist.rock.lauren.datas.star_datas_list;
import earthist.rock.lauren.fragment.fragment_star_profile_view;
import me.relex.circleindicator.CircleIndicator;

public class MainScreenActivity extends AppCompatActivity {
    ViewPager pager;
    CircleIndicator indicator;
    first_screen_adapter adapter;
    ImageButton love_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        });

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
    }
    private void showDialog(){
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(MainScreenActivity.this);
        mProgressDialog.setMessage("Data Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                mProgressDialog.dismiss();
            }
        }, 1000);
    }
    private void onClickStarLove(){
        Intent i = new Intent(MainScreenActivity.this, Wander_GypCActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("index",pager.getCurrentItem());
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }
}
