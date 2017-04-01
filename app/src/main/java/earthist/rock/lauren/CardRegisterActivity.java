package earthist.rock.lauren;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
public class CardRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_debit;
    private int current_star_idx;
    private final int DEBITCARD_INFO_REGISTER_ACTIVITY = 200;
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
