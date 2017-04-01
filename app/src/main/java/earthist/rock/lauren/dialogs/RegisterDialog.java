package earthist.rock.lauren.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;

import java.util.List;

import earthist.rock.lauren.R;
import earthist.rock.lauren.datas.CommonDatas;

/**
 * Created by RockStar-0116 on 2016.07.19.
 */
public class RegisterDialog extends Dialog {
    Context _this;
    Button m_bt_yes;
    EditText txt_user_name, txt_user_password, txt_user_emailnum;
    TextView txt_explaination;
    View.OnClickListener m_listener;
    CheckBox chk_show_password;
    private boolean step_second = false;
    public RegisterDialog(Context context){
        super(context);
        _this = context;
    }

    public RegisterDialog(Context context, int themeResId) {
        super(context, themeResId);
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
        txt_user_emailnum = (EditText) findViewById(R.id.txt_regi_user_emailnumber);
        txt_explaination = (TextView) findViewById(R.id.explaination);
        chk_show_password = (CheckBox) findViewById(R.id.chk_regi_show_password);
        chk_show_password.setChecked(step_second);

        txt_user_emailnum.setScaleY(0);
        txt_user_password.setScaleY(0);

        txt_user_emailnum.setVisibility(View.GONE);
        txt_user_password.setVisibility(View.GONE);
        txt_explaination.setVisibility(View.VISIBLE);


        chk_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    showPasswordBoxes();
                }
                else{
                    hidePasswordBoxes();
                }
            }
        });
        m_bt_yes.setOnClickListener(m_listener);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
    public void showPasswordBoxes(){
        step_second = true;
        chk_show_password.setChecked(true);
        ViewAnimator.animate(txt_explaination)
                .scaleY(1.f, 0.f)
                .duration(300)
                .thenAnimate(txt_user_emailnum, txt_user_password)
                .scaleY(0.f, 1.f)
                .duration(300)
                .start();

        txt_user_password.setVisibility(View.VISIBLE);
        txt_user_emailnum.setVisibility(View.VISIBLE);
        txt_explaination.setVisibility(View.GONE);
        m_bt_yes.setText("register");
//        Animation show_animation = AnimationUtils.loadAnimation(_this, R.anim.show_animation);
//        txt_user_password.startAnimation(show_animation);
//        txt_user_emailnum.startAnimation(show_animation);
//        Animation hide_animation = AnimationUtils.loadAnimation(_this, R.anim.hide_animation);
//        txt_explaination.startAnimation(hide_animation);
    }

    public void hidePasswordBoxes(){
        step_second = false;
        chk_show_password.setChecked(false);
        m_bt_yes.setText("verify email address");
        ViewAnimator.animate(txt_user_emailnum, txt_user_password)
                .scaleY(1.f, 0.f)
                .duration(300)
                .thenAnimate(txt_explaination)
                .scaleY(0.f, 1.f)
                .duration(300)
                .start();

        txt_user_emailnum.setVisibility(View.GONE);
        txt_user_password.setVisibility(View.GONE);
        txt_explaination.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed() {
        dismiss();
    }
    public void setButtonClickListener(View.OnClickListener m_listener){
        this.m_listener = m_listener;
    }
    public String getEmailText(){
        return txt_user_name.getText().toString();
    }
    public String getPasswordText(){
        return txt_user_password.getText().toString();
    }
    public String getEmailNumberText(){
        return txt_user_emailnum.getText().toString();
    }
    public EditText getEmailTextInstance(){return txt_user_name;}
    public EditText getPasswordTextInstance(){return txt_user_password;}

    public boolean getIsSecondStep(){
        return step_second;
    }
    public void setIsSecondStep(boolean value){
        step_second = value;
    }
}
