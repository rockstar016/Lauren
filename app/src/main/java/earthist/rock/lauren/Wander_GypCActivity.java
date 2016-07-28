package earthist.rock.lauren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import earthist.rock.lauren.commonControllerModels.IsUserLoggedIn;
import earthist.rock.lauren.datas.cause_data_list;
import earthist.rock.lauren.datas.color_list_based_cause;
import earthist.rock.lauren.datas.star_datas_list;
import earthist.rock.lauren.datas.support_profile_list;
import earthist.rock.lauren.fragment.CauseBioFragment;
import earthist.rock.lauren.fragment.OtherEarthistFragment;
import earthist.rock.lauren.fragment.WanderinGypCFragment;

public class Wander_GypCActivity extends AppCompatActivity {
    private Button invest_button;
    private int current_fragement_idx;
    ActionBar actionbar;
    private static int LOGINACTIVITY_RESULT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_detail);
        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
            value = b.getInt("index");
        actionbar = getSupportActionBar();
        FragmentSetupStarIdx(value);
        setActonBarTextStarIdx(value);
        invest_button = (Button)findViewById(R.id.button_invest);
        invest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickinvest();
            }
        });
    }
    private void showDialog(){
        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(Wander_GypCActivity.this);
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
    public void onClickinvest(){
        if(IsUserLoggedIn.isLoggedIn(getApplicationContext()) == false){
            int param = current_fragement_idx % 5;
            Intent i = new Intent(Wander_GypCActivity.this, LoginDialogActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index",param);
            i.putExtras(bundle);
            startActivityForResult(i, LOGINACTIVITY_RESULT);
        }
        else{
            gotoInvestActivity();
        }
    }
    private void gotoInvestActivity(){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGINACTIVITY_RESULT)
        {
            if(resultCode == Activity.RESULT_OK)
            {

                Intent i = new Intent(Wander_GypCActivity.this, CardRegisterActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    public void setActonBarTextStarIdx(int value){
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(star_datas_list.star_name_array[value]);
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(color_list_based_cause.cause_theme_color[value])));
    }
    public void setActonBarTextCauseIdx(int value){
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(cause_data_list.cause_title_array[value]);
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(color_list_based_cause.cause_theme_color[value])));
    }
    public void setActonBarTextSupportIdx(int value){
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(support_profile_list.support_name_array[value]);
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(color_list_based_cause.cause_theme_color[value])));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(current_fragement_idx <Fragment_Index.ChooseLoveCauseBio) {
                Intent i = new Intent(Wander_GypCActivity.this, MainScreenActivity.class);
                startActivity(i);
                finish();
            }
            else if(current_fragement_idx > Fragment_Index.LoganCoats && current_fragement_idx < Fragment_Index.LanaSheaBio){
                FragmentSetupStarIdx(current_fragement_idx - 5);
                setActonBarTextStarIdx(current_fragement_idx);
            }
            else if(current_fragement_idx > Fragment_Index.ArtIsTherapyCauseBio && current_fragement_idx < 15)
            {
                FragmentSetupStarIdx(current_fragement_idx - 10);
                setActonBarTextStarIdx(current_fragement_idx);
            }
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if(current_fragement_idx <Fragment_Index.ChooseLoveCauseBio) {
            Intent i = new Intent(Wander_GypCActivity.this, MainScreenActivity.class);
            startActivity(i);
            finish();
        }
        else if(current_fragement_idx > Fragment_Index.LoganCoats && current_fragement_idx < Fragment_Index.LanaSheaBio){
            FragmentSetupStarIdx(current_fragement_idx - 5);
            setActonBarTextStarIdx(current_fragement_idx);
        }
        else if(current_fragement_idx > Fragment_Index.ArtIsTherapyCauseBio && current_fragement_idx < 15)
        {
            FragmentSetupStarIdx(current_fragement_idx - 10);
            setActonBarTextStarIdx(current_fragement_idx);
        }
    }
    public void FragmentSetupStarIdx(int current_step) {
        current_fragement_idx = current_step;
            Fragment frag = WanderinGypCFragment.newInstance(current_step);
            LoadFragment(frag);
    }
    public void FragmentSetupCauseIdx(int current_step) {
        current_fragement_idx = current_step;
        Fragment frag = CauseBioFragment.newInstance(current_step);
        LoadFragment(frag);
    }
    public void FragmentSetupSupportIdx(int current_step) {
        current_fragement_idx = current_step;
        Fragment frag = OtherEarthistFragment.newInstance(current_step);
        LoadFragment(frag);
    }

    private void LoadFragment(Fragment frag) {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStackImmediate();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, frag)
                .commit();
    }
}