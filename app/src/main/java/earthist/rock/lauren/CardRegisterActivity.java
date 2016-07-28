package earthist.rock.lauren;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CardRegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_debit, button_credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_register);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("LINK A CARD");
        button_debit = (Button)findViewById(R.id.button_debit_card);
        button_credit = (Button)findViewById(R.id.button_credit_card);
        button_credit.setOnClickListener(this);
        button_debit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();
        if(view_id == R.id.button_debit_card){
            Toast.makeText(CardRegisterActivity.this, "Debit card selected", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(CardRegisterActivity.this, "Credit Card selected", Toast.LENGTH_SHORT).show();
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
