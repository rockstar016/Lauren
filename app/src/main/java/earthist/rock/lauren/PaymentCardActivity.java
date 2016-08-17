package earthist.rock.lauren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import java.util.HashMap;
import java.util.Map;


public class PaymentCardActivity extends AppCompatActivity implements View.OnClickListener{
    Button saveButton;
    EditText cardNumber;
    EditText cvc;
    Spinner monthSpinner;
    Spinner yearSpinner;
    ProgressDialog m_prog_dialog;
    private static final String CURRENCY_UNSPECIFIED = "Unspecified";
    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_uVTCJTso0HTJLz1o5xTEwlg8";
    private int current_star_idx;
    private Stripe m_stripe;
    public FirebaseUser current_user;
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
        saveButton.setOnClickListener(this);
        showProgress("Getting information");
        getPublishSecretKey();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
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
        return "4242424242424242";
        //return this.cardNumber.getText().toString();
    }
    public String getCvc() {
        return "123";
        //return this.cvc.getText().toString();
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
        card.setCurrency("usd");
        return card;
    }
    private void createCustomer(Token token){
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("source", token.getId()); // obtained with Stripe.js
        customerParams.put("plan", "invest");
        customerParams.put("email", current_user.getEmail());
        HashMap<String, String> metadata = new HashMap<String, String>();
        metadata.put("staridx",current_star_idx + "");
        customerParams.put("metadata",metadata);
        createCustomerClass customerClass = new createCustomerClass(customerParams,new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                Customer customer = (Customer)output;
                if(customer != null) {
                    saveDataToFirebase(customer.getId());
                }
            }
        });
        customerClass.execute();
    }
    private void EngineFunction(){
        showProgress("Card Registering");
        Card card = generateCard();
        boolean card_validation = card.validateCard();
        if (card_validation) {
            m_stripe.createToken(card, STRIPE_PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            createCustomer(token);
                            hideProgress();
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
    private void saveDataToFirebase(String customer_token){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("customer_token").child(current_user.getUid()).setValue(customer_token);
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
    @NonNull
    private void getPublishSecretKey() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stripe_secret").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        com.stripe.Stripe.apiKey = dataSnapshot.getValue(String.class);
                        m_stripe = new Stripe();
                        hideProgress();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("error","database connect error");
                        Toast.makeText(PaymentCardActivity.this, "Connection is lost to server", Toast.LENGTH_SHORT).show();
                        String stripe_secret = "";
                        com.stripe.Stripe.apiKey = "";
                        hideProgress();
                    }
                });
    }
    private class createCustomerClass extends AsyncTask {
        Map<String, Object> customerParams;
        public AsyncResponse delegate = null;//Call back interface
        public createCustomerClass(Map<String, Object> params, AsyncResponse response)
        {
            customerParams = params;
            delegate = response;
        }
        @Override
        protected void onPostExecute(Object o) {
            delegate.processFinish(o);
        }
        @Override
        protected Customer doInBackground(Object[] objects) {
            try {
                Customer customer = Customer.create(customerParams);
                return customer;
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (CardException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public interface AsyncResponse {
        void processFinish(Object output);
    }
}
