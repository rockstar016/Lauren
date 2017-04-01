package earthist.rock.lauren.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import earthist.rock.lauren.R;

/**
 * Created by RockStar0116 on 2016.10.20.
 */

public class ThanksDialog extends Dialog {
    Context _this;
    View.OnClickListener m_listener;
    public ThanksDialog(Context context){
        super(context);
        _this = context;
    }
    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_thanks);
        Button back_button = (Button)findViewById(R.id.thanks_ok);
        back_button.setOnClickListener(m_listener);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
    @Override
    public void onBackPressed() {

    }
    public void setButtonClickListener(View.OnClickListener m_listener){
        this.m_listener = m_listener;
    }
}
