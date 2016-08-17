package earthist.rock.lauren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import earthist.rock.lauren.datas.CommonDatas;
import earthist.rock.lauren.datas.cause_data_list;
import earthist.rock.lauren.datas.star_datas_list;
import earthist.rock.lauren.dialogs.RegisterDialog;
import earthist.rock.lauren.dialogs.ResetPasswordDialog;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginDialogActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_login, button_register, button_forgot_password;
    TextView txt_chosen_starname, txt_invest_target_star, txt_chosen_cause;
    EditText txt_user_email, txt_user_password;

    private static final String TAG = LoginDialogActivity.class.getSimpleName();
    private FirebaseAuth m_firebase_auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog m_dialog;

    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    private LoginButton mFacebookLoginButton;
    private CallbackManager mFacebookCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
        txt_chosen_starname = (TextView)findViewById(R.id.txt_invest_starname);
        txt_chosen_cause = (TextView)findViewById(R.id.txt_invest_cause_name);
        txt_invest_target_star = (TextView)findViewById(R.id.txt_invest_star_target);
        txt_user_email = (EditText)findViewById(R.id.txt_user_name);
        txt_user_password = (EditText)findViewById(R.id.txt_user_password);
        txt_chosen_starname.setText("You've chosen to invest in " + star_datas_list.star_name_array[current_choosen_idx]);
        txt_chosen_cause.setText(cause_data_list.cause_title_array[current_choosen_idx] + ":");
        txt_invest_target_star.setText(star_datas_list.star_name_array[current_choosen_idx] + ":");
        m_firebase_auth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI();
                } else {
                    clearAllTextField();
                }
            }
        };
        FaceBookManage();
    }
    private void clearAllTextField(){
        try{
            txt_user_email.setText("");
            txt_user_password.setText("");
        }
        catch (Exception e){
          Log.d("TextField null","Firebase auth null");
        }
    }
    private void updateUI() {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        m_firebase_auth.addAuthStateListener(mAuthListener);
    }
    private void FaceBookManage(){
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        showDialog("Signin");
        m_firebase_auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideDialog();
                        Log.d("facebook signin", "facebook signin" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginDialogActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog();
                        Log.d("facebook signin",e.toString());
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        switch (view_id){
            case R.id.button_login:
                Login();
                break;
            case R.id.button_register:
                register();
                break;
            case R.id.button_forgot_password:
                forgotpassword();
                break;
        }
    }
    public int validForm(){
        if(txt_user_email.getText().toString().isEmpty() || txt_user_password.getText().toString().isEmpty()){
            return CommonDatas.REGISTER_INPUT_INVALID_ERROR;
        }
        else if(txt_user_password.getText().length() < 6){
            return CommonDatas.REGISTER_INPUT_PASSWORD_SIX_ERROR;
        }
        return CommonDatas.REGISTER_INPUT_VALID;
    }
    private void Login(){
        int valid_result = validForm();
        if(valid_result == CommonDatas.REGISTER_INPUT_VALID){
            if(isValidEmail(txt_user_email.getText())) {
                showDialog("Sign in");
                loginWithPassword(txt_user_email.getText().toString().trim(), txt_user_password.getText().toString().trim());
            }
            else
                txt_user_email.setError("Input Email address, please");
        }
        else if(valid_result == CommonDatas.REGISTER_INPUT_INVALID_ERROR){
            txt_user_email.setError("Input Email address, please.");
            txt_user_password.setError("Input password");
        }
        else if(valid_result == CommonDatas.REGISTER_INPUT_PASSWORD_SIX_ERROR){
            txt_user_password.setError("Password at least 6 characters");
        }
    }
    public void loginWithPassword(String useremail, String userpwd) {
        m_firebase_auth.signInWithEmailAndPassword(useremail, userpwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("firebase signin", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginDialogActivity.this, "Don't you have account?\nRegist your information, please.", Toast.LENGTH_SHORT).show();
                            Log.d("firebase signin", "signInWithEmail:failed", task.getException());
                        }
                        hideDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog();
                        Log.d("firebase signin",e.toString());
                    }
                });
    }
    private void register(){
        final RegisterDialog registerdialog = new RegisterDialog(this);
        registerdialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerdialog.validForm() == CommonDatas.REGISTER_INPUT_VALID) {
                    if(isValidEmail(registerdialog.getEmailText())){
                        showDialog("Sign up");
                        onClickRegisterButton(registerdialog.getEmailText(), registerdialog.getPasswordText());
                        registerdialog.dismiss();
                    }
                    else{
                        registerdialog.getEmailTextInstance().setError("Input correct E-mail address");
                    }
                }
                else if(registerdialog.validForm() == CommonDatas.REGISTER_INPUT_INVALID_ERROR){
                        registerdialog.getEmailTextInstance().setError("Input correct EmailAddress");
                        registerdialog.getPasswordTextInstance().setError("Input Password at least 6 characters");
                }
                else if(registerdialog.validForm() == CommonDatas.REGISTER_INPUT_PASSWORD_SIX_ERROR){
                    registerdialog.getPasswordTextInstance().setError("Input Password at least 6 characters");
                }
            }
        });
        registerdialog.show();
    }
    private void forgotpassword(){
        final ResetPasswordDialog resetdialog = new ResetPasswordDialog(this);
        resetdialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resetdialog.validForm() == CommonDatas.REGISTER_INPUT_VALID) {
                    if(isValidEmail(resetdialog.getEmailText())){
                        showDialog("Reset Password");
                        onClickPasswordResetButton(resetdialog.getEmailText());
                        resetdialog.dismiss();
                    }
                    else{
                        resetdialog.getEmailTextInstance().setError("Input correct E-mail address");
                    }
                }
                else if(resetdialog.validForm() == CommonDatas.REGISTER_INPUT_INVALID_ERROR){
                    resetdialog.getEmailTextInstance().setError("Input correct EmailAddress");
                }
            }
        });
        resetdialog.show();
    }
    public void onClickPasswordResetButton(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideDialog();
                        if (task.isSuccessful()) {
                            Log.d("firebase password reset", "Email sent.");

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog();
                        Log.d("firebase password reset", e.toString());
                    }
                });
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void onClickRegisterButton(String email, String pwd){
        m_firebase_auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideDialog();
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginDialogActivity.this, "Email Address already exists.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Firebase signup","failed");
                        }
                        else{
                            Toast.makeText(LoginDialogActivity.this, "User Signup Success", Toast.LENGTH_SHORT).show();
                            Log.d("Firebase signup","success");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog();
                        Log.d("Signup failure",e.toString());
                        Toast.makeText(LoginDialogActivity.this, "Failure to sign up", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
    public void showDialog(String title){
        m_dialog = ProgressDialog.show(LoginDialogActivity.this,title,"",false, false);
        m_dialog.show();
    }
    public void hideDialog(){
        if(m_dialog.isShowing()){
            m_dialog.dismiss();
        }
    }
}
