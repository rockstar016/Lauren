package earthist.rock.lauren;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import earthist.rock.lauren.Utils.AsyncResponse;
import earthist.rock.lauren.Utils.ServerInformation;
import earthist.rock.lauren.Utils.UserInfoManagement;
import earthist.rock.lauren.dialogs.ThanksDialog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PaymentCardActivity extends AppCompatActivity implements View.OnClickListener{
    Button saveButton;
    EditText cardNumber;
    EditText cvc;
    Spinner monthSpinner;
    Spinner yearSpinner;
    ProgressDialog m_prog_dialog;
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_live_yb8coGpyOukzs1bAP9Nqbjui";
    private int current_star_idx;
    private Stripe m_stripe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_payment_card);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("SAVE CARD");
        Bundle b = getIntent().getExtras();
        if(b != null) {
            current_star_idx = b.getInt("staridx");
        }
        this.cardNumber = (EditText) findViewById(R.id.txt_cardinfo_cardnumber);
        this.cvc = (EditText) findViewById(R.id.txt_cardinfo_cvc);
        this.monthSpinner = (Spinner) findViewById(R.id.spin_cardinfo_expmonth);
        this.yearSpinner = (Spinner) findViewById(R.id.spin_cardinfo_expyear);
        this.saveButton = (Button)findViewById(R.id.bt_cardinfo_save);

        try {
            m_stripe = new Stripe(STRIPE_PUBLISHABLE_KEY);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        saveButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_cardinfo_save){
            EngineFunction();
        }
    }
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }
    public String getCardNumber() {
        return this.cardNumber.getText().toString();
    }
    public String getCvc() {
        return this.cvc.getText().toString();
    }
    public Integer getExpMonth() {
        return getInteger(this.monthSpinner);
    }
    public Integer getExpYear() {
        return getInteger(this.yearSpinner);
    }

    private Card generateCard(){
        Card card = new Card(
                getCardNumber(),
                getExpMonth(),
                getExpYear(),
                getCvc());
        return card;
    }

    public void displayWelcomeDialog(){
        final ThanksDialog thanksDialog = new ThanksDialog(this);
        thanksDialog.setButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainScreen();
                thanksDialog.dismiss();
            }
        });
        thanksDialog.show();
    }
    public void showErrorTryagain(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Sorry")
                .setMessage("Error occured. Try again?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        EngineFunction();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        gotoMainScreen();
                    }
                })
                .show();
    }

    private void EngineFunction(){

        Card card = generateCard();
        boolean card_validation = card.validateCard();
        if (card_validation) {
            showProgress("Card Registering");

            m_stripe.createToken(card, new TokenCallback() {
                        public void onSuccess(Token strip_token) {
                            String token = UserInfoManagement.getToken(PaymentCardActivity.this);
                            CheckoutService service = new CheckoutService(token, strip_token.getId(), current_star_idx + "", new AsyncResponse() {
                                @Override
                                public void processFinish(Object output) {
                                    hideProgress();
                                    try{
                                        JSONObject result_object = new JSONObject((String) output);
                                        if(result_object.getBoolean("result") == true){
                                            displayWelcomeDialog();
                                        }
                                        else{
                                            String error_reason = result_object.getString("data");
                                            if(error_reason.equals("TokenError")){
                                                UserInfoManagement.setToken(PaymentCardActivity.this,"");
                                                Toast.makeText(PaymentCardActivity.this, "Sorry. Log in again, please", Toast.LENGTH_SHORT).show();
                                                gotoMainScreen();
                                            }
                                            else if(error_reason.equals("PayError")){
                                                showErrorTryagain();
                                            }
                                        }
                                    }catch (Exception e){
                                        showErrorTryagain();
                                    }

                                }
                            });
                            service.execute();

                        }
                        public void onError(Exception error) {
                            handleError(error.getLocalizedMessage());
                            hideProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    public void gotoMainScreen(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    private void handleError(String title){
        cardNumber.setText("");
        cvc.setText("");
        monthSpinner.setSelection(0,true);
        yearSpinner.setSelection(0,true);
        hideProgress();
        Toast.makeText(PaymentCardActivity.this, title, Toast.LENGTH_SHORT).show();
    }
    private void showProgress(String title){
        m_prog_dialog = ProgressDialog.show(this,title,"",false, false);
        m_prog_dialog.show();
    }
    private void hideProgress(){
        if(m_prog_dialog == null) return;
        if(m_prog_dialog.isShowing())
            m_prog_dialog.dismiss();
    }
    @NonNull
    private Integer getInteger(Spinner spinner) {
        try {
            return Integer.parseInt(spinner.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public class CheckoutService extends AsyncTask{
        String token, stripe_token;
        String star_idx;
        AsyncResponse response;
        OkHttpClient okhttp;
        public CheckoutService(String token, String stripe_token, String start_idx, AsyncResponse response) {
            this.token = token;
            this.stripe_token = stripe_token;
            this.star_idx = start_idx;
            this.response = response;
            okhttp = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS).build();
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
                        .add("stripe_token", this.stripe_token)
                        .add("index", this.star_idx)
                        .build();
                Request request = new Request.Builder()
                        .url(ServerInformation.PAYMENT)
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
