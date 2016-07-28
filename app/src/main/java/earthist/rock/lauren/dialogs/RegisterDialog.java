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
import android.widget.TextView;

import java.util.List;

import earthist.rock.lauren.R;

/**
 * Created by RockStar-0116 on 2016.07.19.
 */
public class RegisterDialog extends Dialog {
    Context _this;
    Button m_bt_yes;
    EditText txt_user_name, txt_user_password;
    View.OnClickListener m_listener;
    public RegisterDialog(Context context){
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
        setContentView(R.layout.dialog_register);
        m_bt_yes = (Button)findViewById(R.id.button_register);
        txt_user_name = (EditText) findViewById(R.id.txt_regi_user_name);
        txt_user_password = (EditText) findViewById(R.id.txt_regi_user_pass);
        m_bt_yes.setOnClickListener(m_listener);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
    public void setButtonClickListener(View.OnClickListener m_listener){
        this.m_listener = m_listener;
    }

}
