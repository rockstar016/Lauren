package earthist.rock.lauren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import java.util.HashMap;
import java.util.Map;
public class CardRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_debit;
    private int current_star_idx;
    private final int DEBITCARD_INFO_REGISTER_ACTIVITY = 200;
    ProgressDialog m_prog_dialog;
    private Stripe m_stripe;
    private String customer_token = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_card_register);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("LINK A CARD");
        Bundle b = getIntent().getExtras();
        if(b != null) {
            current_star_idx = b.getInt("staridx");
        }
        button_debit = (Button)findViewById(R.id.button_debit_card);
        button_debit.setOnClickListener(this);
        showProgress("Retrieving your history...");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String userId = auth.getCurrentUser().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("customer_token").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        customer_token = dataSnapshot.getValue(String.class);
                        hideProgress();
                        EngineFunction(customer_token);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("error","database connect error");
                        Toast.makeText(CardRegisterActivity.this, "Connection is lost to server", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                });

    }
    private void showProgress(String title){
        m_prog_dialog = ProgressDialog.show(this,title,"",false, false);
        m_prog_dialog.show();
    }
    private void hideProgress(){
        if(m_prog_dialog.isShowing())
            m_prog_dialog.dismiss();
    }
    private void EngineFunction(String customer_token){
        if(customer_token != null)
        {
            showProgress("Getting Information for you");
            getPublishSecretKey();

        }
    }
    private void getPublishSecretKey() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stripe_secret").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         String secret_key = dataSnapshot.getValue(String.class);
                        com.stripe.Stripe.apiKey = secret_key;
                        m_stripe = new Stripe();
                        updateUser(customer_token);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("error","database connect error");
                        Toast.makeText(CardRegisterActivity.this, "Connection is lost to server", Toast.LENGTH_SHORT).show();
                        String stripe_secret = "";
                        com.stripe.Stripe.apiKey = "";
                        hideProgress();
                    }
                });
    }
    private void updateUser(String customer_token) {
        try {
            updateCustomerClass customerClass = new updateCustomerClass(customer_token, new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    Customer customer = (Customer) output;
                    hideProgress();
                    Intent i = new Intent(CardRegisterActivity.this, MainScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            customerClass.execute();
        } catch (Exception e) {
        }
    }
    private class updateCustomerClass extends AsyncTask {
        String customerParams;
        public AsyncResponse delegate = null;//Call back interface
        public updateCustomerClass(String params, AsyncResponse response)
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
                Customer customer = Customer.retrieve(customerParams);
                String invest_star = customer.getMetadata().get("staridx");;
                invest_star += ", " + current_star_idx;
                HashMap<String, String> user_update_meta = new HashMap<>();
                user_update_meta.put("staridx",invest_star);

                Map<String, Object> updateParams = new HashMap<String, Object>();
                updateParams.put("metadata", user_update_meta);
                customer.update(updateParams);

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("plan", "invest");
                params.put("customer", customer.getId());
                Subscription.create(params);

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
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    public interface AsyncResponse {
        void processFinish(Object output);
    }
    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        if(view_id == R.id.button_debit_card){
            Intent i = new Intent(CardRegisterActivity.this, PaymentCardActivity.class);
            i.putExtra("staridx",current_star_idx);
            startActivityForResult(i,DEBITCARD_INFO_REGISTER_ACTIVITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DEBITCARD_INFO_REGISTER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Intent i = new Intent(CardRegisterActivity.this, MainScreenActivity.class);
                startActivity(i);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CardRegisterActivity.this, MainScreenActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent i = new Intent(CardRegisterActivity.this, MainScreenActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}
