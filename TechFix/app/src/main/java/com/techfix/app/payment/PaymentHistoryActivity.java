package com.techfix.app.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.PaymentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.Payment;
import com.techfix.app.util.Money;

import java.util.List;

/** Lists the signed-in customer's own payments with a paid total. Each account is isolated. */
public class PaymentHistoryActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;
    private RecyclerView rv;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        db = new DBHelper(this);
        session = new Session(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rv = findViewById(R.id.rvPayments);
        emptyState = findViewById(R.id.tvEmpty);
        rv.setLayoutManager(new LinearLayoutManager(this));

        bindPayments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // A payment just made in the flow should appear when we come back to this screen.
        bindPayments();
    }

    private void bindPayments() {
        List<Payment> payments = db.getPaymentsByUser(session.getUserId());

        long totalPaid = 0;
        for (Payment p : payments) {
            if ("Paid".equalsIgnoreCase(p.status)) totalPaid += Money.parse(p.amount);
        }
        ((TextView) findViewById(R.id.tvTotalPaid)).setText(Money.format(totalPaid));
        ((TextView) findViewById(R.id.tvTxnCount)).setText(String.valueOf(payments.size()));

        emptyState.setVisibility(payments.isEmpty() ? View.VISIBLE : View.GONE);
        rv.setAdapter(new PaymentAdapter(payments, p ->
                Toast.makeText(this, "Receipt " + p.refNo, Toast.LENGTH_SHORT).show()));
    }
}
