package earthist.rock.lauren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import com.google.firebase.auth.FirebaseAuth;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import earthist.rock.lauren.Utils.AsyncResponse;
import earthist.rock.lauren.Utils.ServerInformation;
import earthist.rock.lauren.Utils.UserInfoManagement;
import earthist.rock.lauren.datas.cause_data_list;
import earthist.rock.lauren.datas.star_datas_list;
import earthist.rock.lauren.fragment.CauseBioFragment;
import earthist.rock.lauren.fragment.WanderinGypCFragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Wander_GypCActivity extends AppCompatActivity {
    private Button invest_button;
    private int current_fragement_idx;
    ActionBar actionbar;
    private static int LOGINACTIVITY_RESULT = 100;
    ProgressDialog m_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_star_detail);
        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
            value = b.getInt("index");
        actionbar = getSupportActionBar();
        invest_button = (Button)findViewById(R.id.button_invest);
        invest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickinvest();
            }
        });
        FragmentSetupStarIdx(value);
        setActonBarTextStarIdx(value);

    }
    public void showDialog(String title){
        m_dialog = ProgressDialog.show(Wander_GypCActivity.this,title,"Processing",false, false);
        m_dialog.show();
    }
    public void hideDialog(){
        if(m_dialog.isShowing()){
            m_dialog.dismiss();
        }
    }

    public void onClickinvest(){
        if(UserInfoManagement.getToken(this) == null){
            Intent i = new Intent(Wander_GypCActivity.this, LoginDialogActivity.class);
            i.putExtra("index",current_fragement_idx);
            startActivityForResult(i, LOGINACTIVITY_RESULT);
        }
        else{
            showDialog("Checking previous information");
            CheckTokenService tokenService = new CheckTokenService(UserInfoManagement.getToken(this), new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    try{

                        JSONObject result_object = new JSONObject((String)output);
                        if(result_object.getBoolean("result") == true){
                            gotoInvestActivity();
                        }
                        else{
                            Intent i = new Intent(Wander_GypCActivity.this, LoginDialogActivity.class);
                            i.putExtra("index",current_fragement_idx);
                            startActivityForResult(i, LOGINACTIVITY_RESULT);
                        }
                    }
                    catch (Exception e){
                        Intent i = new Intent(Wander_GypCActivity.this, LoginDialogActivity.class);
                        i.putExtra("index",current_fragement_idx);
                        startActivityForResult(i, LOGINACTIVITY_RESULT);
                    }
                    hideDialog();
                }
            });
            tokenService.execute();
        }
    }
    private void gotoInvestActivity( ){
        Intent i =new Intent(Wander_GypCActivity.this, CardRegisterActivity.class);
        i.putExtra("staridx",current_fragement_idx);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGINACTIVITY_RESULT) {
            if(resultCode == Activity.RESULT_OK) {
                gotoInvestActivity();
            }
        }
    }
    public void setActonBarTextStarIdx(int value){
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(star_datas_list.star_name_array[value]);
    }
    public void setActonBarTextCauseIdx(int value){
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(cause_data_list.cause_title_array[value]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(current_fragement_idx >=Fragment_Index.WonderGypC && current_fragement_idx <= Fragment_Index.DandoraMusic) {
                Intent i = new Intent(Wander_GypCActivity.this, MainScreenActivity.class);
                startActivity(i);
                finish();
            }
            else if(current_fragement_idx  == Fragment_Index.chooselove_bio || current_fragement_idx == Fragment_Index.waterislife_bio){
                invest_button.setVisibility(View.VISIBLE);
                FragmentSetupStarIdx(Fragment_Index.WonderGypC);
                setActonBarTextStarIdx(Fragment_Index.WonderGypC);
            }
            else if(current_fragement_idx >= Fragment_Index.raceequity_bio && current_fragement_idx <= Fragment_Index.wildlife_bio){
                FragmentSetupStarIdx(current_fragement_idx - 8);
                setActonBarTextStarIdx(current_fragement_idx);
            }
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if(current_fragement_idx >=Fragment_Index.WonderGypC && current_fragement_idx <= Fragment_Index.DandoraMusic) {
            Intent i = new Intent(Wander_GypCActivity.this, MainScreenActivity.class);
            startActivity(i);
            finish();
        }
        else if(current_fragement_idx  == Fragment_Index.chooselove_bio || current_fragement_idx == Fragment_Index.waterislife_bio){
            invest_button.setVisibility(View.VISIBLE);
            FragmentSetupStarIdx(Fragment_Index.WonderGypC);
            setActonBarTextStarIdx(Fragment_Index.WonderGypC);
        }
        else if(current_fragement_idx >= Fragment_Index.raceequity_bio && current_fragement_idx <= Fragment_Index.wildlife_bio){
            int tmp_index = current_fragement_idx - 8;
            FragmentSetupStarIdx(tmp_index);
            setActonBarTextStarIdx(tmp_index);
        }
    }
    public void FragmentSetupStarIdx(int current_step) {
        invest_button.setVisibility(View.VISIBLE);
        current_fragement_idx = current_step;
            Fragment frag = WanderinGypCFragment.newInstance(current_step);
            LoadFragment(frag);
    }
    public void FragmentSetupCauseIdx(int current_step) {
        invest_button.setVisibility(View.GONE);
        current_fragement_idx = current_step;
        Fragment frag = CauseBioFragment.newInstance(current_step);
        LoadFragment(frag);
    }
    private void LoadFragment(Fragment frag) {
        if(frag != null) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, frag)
                    .commit();
        }
    }

    public class CheckTokenService extends AsyncTask {
        String token;
        AsyncResponse response;
        OkHttpClient okhttp;
        public CheckTokenService(String token, AsyncResponse response) {
            this.token = token;
            this.response = response;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
        }
        @Override
        protected void onPostExecute(Object o) {
            response.processFinish(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                RequestBody requestbody = new FormBody.Builder()
                        .add("token", this.token)
                        .build();
                Request request = new Request.Builder()
                        .url(ServerInformation.CHECK_TOKEN)
                        .post(requestbody)
                        .build();
                Response response = okhttp.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}