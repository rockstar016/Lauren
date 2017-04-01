package earthist.rock.lauren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import earthist.rock.lauren.Utils.AsyncResponse;
import earthist.rock.lauren.Utils.ServerInformation;
import earthist.rock.lauren.Utils.UserInfoManagement;
import earthist.rock.lauren.datas.CommonDatas;
import earthist.rock.lauren.datas.cause_data_list;
import earthist.rock.lauren.datas.star_datas_list;
import earthist.rock.lauren.dialogs.RegisterDialog;
import earthist.rock.lauren.dialogs.ResetPasswordDialog;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.florent37.viewanimator.ViewAnimator;

import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

public class LoginDialogActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_login, button_register, button_forgot_password;
    TextView txt_chosen_starname, txt_invest_target_star, txt_chosen_cause;
    EditText txt_user_email, txt_user_password;
    private static final String TAG = LoginDialogActivity.class.getSimpleName();
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
        if(current_choosen_idx == Fragment_Index.WonderGypC) {
            txt_chosen_cause.setText(cause_data_list.cause_title_array[current_choosen_idx] + ",\n"+ cause_data_list.cause_title_array[current_choosen_idx+1]+" ");
        }
        else{
            txt_chosen_cause.setText(cause_data_list.cause_title_array[current_choosen_idx+1]);
        }

        txt_invest_target_star.setText(star_datas_list.star_name_array[current_choosen_idx] + " ");
        FaceBookManage();
    }
    /**
     * Remove this dialog
     */
    private void updateUI() {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
    }

    private void FaceBookManage(){
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "Facebook Login success");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        try {
                            String email  = object.getString("email");
                            facebookProcess(email);
                        }catch (Exception e){

                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    public void facebookProcess(String email_address){
        LoginManager.getInstance().logOut();
        showDialog("Log in");
        FacebookLoginService service = new FacebookLoginService(email_address, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                try{
                    JSONObject json_object = new JSONObject((String)output);
                    if(json_object.getBoolean("result") == true){
                        String token = json_object.getString("data");
                        UserInfoManagement.setToken(LoginDialogActivity.this, token);
                        updateUI();
                    }
                    else{
                        Toast.makeText(LoginDialogActivity.this, json_object.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
                hideDialog();
            }
        });
        service.execute();
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
                showDialog("Log in");
                /**
                 * Login
                 */
                LoginService service = new LoginService(txt_user_email.getText().toString().trim(), txt_user_password.getText().toString().trim(), new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        processLogin(output);
                    }
                });
                service.execute();
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
    public void processLogin(Object object){
        /**
         * if login is successful, go to card screen
         */
        hideDialog();
        try {
            JSONObject result_from_service = new JSONObject((String) object);
            if (result_from_service.getBoolean("result") == true) {
                String token = result_from_service.getString("data");
                UserInfoManagement.setToken(LoginDialogActivity.this, token);
                updateUI();

            } else {
                Toast.makeText(LoginDialogActivity.this, result_from_service.getString("data"), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }
    private void register(){
        final RegisterDialog registerdialog = new RegisterDialog(this, R.style.dialog_anim_style);
        registerdialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!registerdialog.getIsSecondStep()) {
                    if (isValidEmail(registerdialog.getEmailText())) {

                            /**
                             * Verify email address
                             */
//                            registerdialog.showPasswordBoxes();
                            showDialog("Verifying email");
                            RegisterEmailCheckService service = new RegisterEmailCheckService(registerdialog.getEmailText().toString().trim(), new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    try {
                                        JSONObject result_from_service = new JSONObject((String) output);
                                        if (result_from_service.getBoolean("result") == true) {
                                            registerdialog.showPasswordBoxes();

                                        } else {
                                            Toast.makeText(LoginDialogActivity.this, result_from_service.getString("data"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {

                                    }
                                    hideDialog();
                                }
                            });
                            service.execute();

                    } else {
                        registerdialog.getEmailTextInstance().setError("Input correct EmailAddress");
                    }
                }
                else{
                    /**
                     * Register.
                     */
                    showDialog("Signing up");
                    RegisterService service = new RegisterService(registerdialog.getEmailText().trim(), registerdialog.getPasswordText().trim(), registerdialog.getEmailNumberText().trim(), new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                            try {
                                JSONObject result_from_service = new JSONObject((String) output);
                                if (result_from_service.getBoolean("result") == true) {
                                        String token = result_from_service.getString("data");
                                        UserInfoManagement.setToken(LoginDialogActivity.this, token);
                                        updateUI();

                                } else {
                                    Toast.makeText(LoginDialogActivity.this, result_from_service.getString("data"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }
                            hideDialog();
                        }
                    });
                    service.execute();
                }

            }
        });
        registerdialog.show();

    }
    private void forgotpassword(){
        final ResetPasswordDialog resetdialog = new ResetPasswordDialog(this,R.style.dialog_anim_style);
        resetdialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resetdialog.validForm() == CommonDatas.REGISTER_INPUT_VALID) {
                    if(isValidEmail(resetdialog.getEmailText())){
                        showDialog("Reset Password");
                        PasswordResetService service = new PasswordResetService(resetdialog.getEmailText().trim(), resetdialog.getPasswordText(), new AsyncResponse() {
                            @Override
                            public void processFinish(Object output) {
                                try{
                                    JSONObject object_result = new JSONObject((String)output);
                                    if(object_result.getBoolean("result") == true){
                                        Toast.makeText(LoginDialogActivity.this, "Your password is changed", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(LoginDialogActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(LoginDialogActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                }
                                hideDialog();
                                resetdialog.dismiss();
                            }
                        });
                        service.execute();
                    }
                    else{
                        resetdialog.getEmailTextInstance().setError("Input correct E-mail address");
                    }
                }
                else if(resetdialog.validForm() == CommonDatas.REGISTER_INPUT_INVALID_ERROR){
                    resetdialog.getEmailTextInstance().setError("Input correct EmailAddress");
                }
                else if(resetdialog.validForm() == CommonDatas.REGISTER_INPUT_PASSWORD_MATCH_ERROR){
                    resetdialog.getNewPasswordConfirmTextInstance().setError("Password doesn't match");
                    resetdialog.getNewPasswordTextInstance().setError("Password doesn't match");
                }
                else if(resetdialog.validForm() == CommonDatas.REGISTER_INPUT_EMPTY){
                    resetdialog.getNewPasswordConfirmTextInstance().setError("Fill out field");
                    resetdialog.getNewPasswordTextInstance().setError("Fill out field");
                }
                else if(resetdialog.validForm() == CommonDatas.REGISTER_INPUT_PASSWORD_SIX_ERROR){
                    resetdialog.getNewPasswordConfirmTextInstance().setError("Password should be 6 char at least");
                    resetdialog.getNewPasswordTextInstance().setError("Password should be 6 char at least");
                }
            }
        });
        resetdialog.show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
    public void showDialog(String title){
        m_dialog = ProgressDialog.show(LoginDialogActivity.this,title,"Processing",false, false);
        m_dialog.show();
    }
    public void hideDialog(){
        if(m_dialog.isShowing()){
            m_dialog.dismiss();
        }
    }
    public class LoginService extends AsyncTask{
        String email, password;
        AsyncResponse response;
        OkHttpClient okhttp;
        public LoginService(String email, String password, earthist.rock.lauren.Utils.AsyncResponse response) {
            this.email = email;
            this.password = password;
            this.response = response;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(0, TimeUnit.MICROSECONDS).build();
        }
        @Override
        protected void onPostExecute(Object o) {
            response.processFinish(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                RequestBody requestbody = new FormBody.Builder()
                                                    .add("email", this.email)
                                                    .add("password", this.password)
                                                    .build();
                Request request = new Request.Builder()
                        .url(ServerInformation.LOGIN_URL)
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
    public class RegisterEmailCheckService extends AsyncTask{
        String email;
        AsyncResponse response;
        OkHttpClient okhttp;
        public RegisterEmailCheckService(String email, earthist.rock.lauren.Utils.AsyncResponse response) {
            this.email = email;
            this.response = response;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(25, TimeUnit.SECONDS).readTimeout(25, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build();
        }
        @Override
        protected void onPostExecute(Object o) {
            response.processFinish(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

                try {
                    RequestBody requestbody = new FormBody.Builder()
                            .add("email", this.email)
                            .build();
                    Request request = new Request.Builder()
                            .url(ServerInformation.REGISTER_VERIFY_EMAIL_URL)
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
    public class RegisterService extends AsyncTask{
        String email, password, email_num;
        AsyncResponse response;
        OkHttpClient okhttp;
        public RegisterService(String email, String password, String email_num, earthist.rock.lauren.Utils.AsyncResponse response) {
            this.email = email;
            this.password = password;
            this.response = response;
            this.email_num = email_num;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(0, TimeUnit.MICROSECONDS).build();
        }
        @Override
        protected void onPostExecute(Object o) {
            response.processFinish(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                RequestBody requestbody = new FormBody.Builder()
                        .add("email", this.email)
                        .add("password", this.password)
                        .add("email_verification_token", this.email_num)
                        .build();
                Request request = new Request.Builder()
                        .url(ServerInformation.REGISTER_URL)
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
    public class FacebookLoginService extends AsyncTask{
        String email;
        AsyncResponse response;
        OkHttpClient okhttp;
        public FacebookLoginService(String email, AsyncResponse response) {
            this.email = email;
            this.response = response;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).build();
        }
        @Override
        protected void onPostExecute(Object o) {
            response.processFinish(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                RequestBody requestbody = new FormBody.Builder()
                        .add("email", this.email)
                        .build();
                Request request = new Request.Builder()
                        .url(ServerInformation.FACEBOOK_LOGIN)
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
    public class PasswordResetService extends AsyncTask{
        String email, password;
        AsyncResponse response;
        OkHttpClient okhttp;
        public PasswordResetService(String email, String password, AsyncResponse response) {
            this.email = email;
            this.password = password;
            this.response = response;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).build();
        }
        @Override
        protected void onPostExecute(Object o) {
            response.processFinish(o);
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                RequestBody requestbody = new FormBody.Builder()
                        .add("email", this.email)
                        .add("new_password", this.password)
                        .build();
                Request request = new Request.Builder()
                        .url(ServerInformation.CHANGE_PASSWORD)
                        .post(requestbody)
                        .build();
                Response response = okhttp.newCall(request).execute();
                String result = response.body().string();
                return result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
