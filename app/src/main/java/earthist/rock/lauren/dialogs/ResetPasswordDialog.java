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
import earthist.rock.lauren.datas.CommonDatas;

/**
 * Created by RockStar-0116 on 2016.07.19.
 */
public class ResetPasswordDialog extends Dialog {
    Context _this;
    Button m_bt_yes;
    EditText txt_user_name, txt_new_password, txt_new_password_confirm;
    View.OnClickListener m_listener;
    public ResetPasswordDialog(Context context){
        super(context);
        _this = context;
    }

    public ResetPasswordDialog(Context context, int themeResId) {
        super(context, themeResId);
        _this = context;
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_reset_pass);
        m_bt_yes = (Button)findViewById(R.id.button_reset_password);
        txt_user_name = (EditText) findViewById(R.id.txt_reset_username);
        txt_new_password = (EditText) findViewById(R.id.txt_reset_password);
        txt_new_password_confirm = (EditText) findViewById(R.id.txt_reset_password_confirm);

        m_bt_yes.setOnClickListener(m_listener);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
    public void setButtonClickListener(View.OnClickListener m_listener){
        this.m_listener = m_listener;
    }
    public int validForm(){
        if(getEmailText().isEmpty()){
            return CommonDatas.REGISTER_INPUT_INVALID_ERROR;
        }
        else if(txt_new_password_confirm.getText().toString().equals(txt_new_password.getText().toString()) == false)
            return CommonDatas.REGISTER_INPUT_PASSWORD_MATCH_ERROR;
        else if(txt_new_password_confirm.getText().toString().isEmpty() || txt_new_password.getText().toString().isEmpty())
            return CommonDatas.REGISTER_INPUT_EMPTY;
        else if(txt_new_password.getText().toString().length() < 6)
            return CommonDatas.REGISTER_INPUT_PASSWORD_SIX_ERROR;
        return CommonDatas.REGISTER_INPUT_VALID;
    }
    public String getEmailText(){
        return txt_user_name.getText().toString();
    }
    public String getPasswordText(){ return txt_new_password.getText().toString(); }
    public EditText getEmailTextInstance(){return txt_user_name;}
    public EditText getNewPasswordTextInstance(){return txt_new_password;}
    public EditText getNewPasswordConfirmTextInstance(){return txt_new_password_confirm;}
}
