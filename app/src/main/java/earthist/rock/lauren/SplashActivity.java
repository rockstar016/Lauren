package earthist.rock.lauren;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class SplashActivity extends AppCompatActivity {
    private boolean isRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isRunning = true;

        startSplash();
}
    private void startSplash()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("token","");
                    ed.commit();
                    Thread.sleep(3000);

                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            doFinish();
                        }
                    });
                }
            }
        }).start();
    }
    private synchronized void doFinish()
    {
        if (isRunning)
        {
            isRunning = false;
            Intent i = new Intent(SplashActivity.this, MainScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            isRunning = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
