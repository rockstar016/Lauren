package earthist.rock.lauren;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import earthist.rock.lauren.datas.cause_data_list;
import earthist.rock.lauren.datas.star_datas_list;
import earthist.rock.lauren.dialogs.RegisterDialog;
import earthist.rock.lauren.dialogs.ResetPasswordDialog;

public class LoginDialogActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_login, button_register, button_forgot_password;
    ImageButton face_book_login;
    TextView txt_chosen_starname, txt_invest_target_star, txt_chosen_cause;
    EditText txt_user_name, txt_user_password;
    int current_choosen_idx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login_dialog);
        Bundle b = getIntent().getExtras();
        int current_choosen_idx =0;
        if(b != null)
            current_choosen_idx = b.getInt("index");
        this.setFinishOnTouchOutside(false);
        button_login = (Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(this);
        button_forgot_password = (Button)findViewById(R.id.button_forgot_password);
        button_forgot_password.setOnClickListener(this);
        button_register = (Button)findViewById(R.id.button_register);
        button_register.setOnClickListener(this);
        face_book_login = (ImageButton)findViewById(R.id.button_facebook_login);

        txt_chosen_starname = (TextView)findViewById(R.id.txt_invest_starname);
        txt_chosen_cause = (TextView)findViewById(R.id.txt_invest_cause_name);
        txt_invest_target_star = (TextView)findViewById(R.id.txt_invest_star_target);

        txt_user_name = (EditText)findViewById(R.id.txt_user_name);
        txt_user_password = (EditText)findViewById(R.id.txt_user_password);



        txt_chosen_starname.setText("You've chosen to invest in " + star_datas_list.star_name_array[current_choosen_idx]);
        txt_chosen_cause.setText(cause_data_list.cause_title_array[current_choosen_idx] + ":");
        txt_invest_target_star.setText(star_datas_list.star_name_array[current_choosen_idx] + ":");
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        switch (view_id){
            case R.id.button_login:
                Login();
                break;
            case R.id.button_facebook_login:
                facebookLogin();
                break;
            case R.id.button_register:
                register();
                break;
            case R.id.button_forgot_password:
                forgotpassword();
                break;
        }
    }
    private void Login(){
        if(txt_user_name.getText().toString().isEmpty() || txt_user_password.getText().toString().isEmpty())
        {
            Toast.makeText(LoginDialogActivity.this, "Input user name and password", Toast.LENGTH_SHORT).show();
        }
        else{
            // do login with ok http
            dologin();
        }
    }
    private void dologin(){
        boolean return_value = true;
        if(return_value == true){
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        else{
            Toast.makeText(LoginDialogActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void facebookLogin(){

    }
    private void register(){
        final RegisterDialog registerdialog = new RegisterDialog(this);
        registerdialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerdialog.dismiss();
                onClickRegisterButton();
            }
        });
        registerdialog.show();
    }
    private void forgotpassword(){
        final ResetPasswordDialog resetdialog = new ResetPasswordDialog(this);
        resetdialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetdialog.dismiss();
                onClickPasswordResetButton();
            }
        });
        resetdialog.show();
    }
    public void onClickPasswordResetButton(){
        //password change service call
        Toast.makeText(LoginDialogActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
    }
    public void onClickRegisterButton(){
        //register service call
        Toast.makeText(LoginDialogActivity.this, "user registered", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}
