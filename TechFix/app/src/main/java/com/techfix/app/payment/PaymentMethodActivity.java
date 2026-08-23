package com.techfix.app.payment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.techfix.app.R;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentMethodActivity extends AppCompatActivity {

    private final int[] rowIds = {R.id.rowCard, R.id.rowCash, R.id.rowBank, R.id.rowWallet};
    private final String[] methods = {"Credit / Debit Card", "Cash on Collection",
            "Bank Transfer", "Digital Wallet"};
    private int selected = 0;

    private int appointmentId;
    private String amount = "LKR 0.00";
    private String ref = "";
    private String service = "";

    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        db = new DBHelper(this);
        session = new Session(this);

        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        if (getIntent().hasExtra("amount")) amount = getIntent().getStringExtra("amount");
        if (getIntent().hasExtra("ref")) ref = getIntent().getStringExtra("ref");
        if (getIntent().hasExtra("service")) service = getIntent().getStringExtra("service");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.tvAmount)).setText(amount);
        ((TextView) findViewById(R.id.tvRef)).setText(ref);

        setupMethod(R.id.rowCard, R.drawable.ic_card, "Credit / Debit Card", "Visa, Mastercard, Amex", 0);
        setupMethod(R.id.rowCash, R.drawable.ic_cash, "Cash on Collection", "Pay when you collect your device", 1);
        setupMethod(R.id.rowBank, R.drawable.ic_bank, "Bank Transfer", "Direct transfer to TechFix", 2);
        setupMethod(R.id.rowWallet, R.drawable.ic_wallet, "Digital Wallet", "FriMi, eZ Cash, Genie", 3);
        select(0);

        TextView btnPay = findViewById(R.id.btnPay);
        btnPay.setText("Pay " + amount);
        btnPay.setOnClickListener(v -> {
          
            recordPayment();

            Intent i = new Intent(this, PaymentSuccessActivity.class);
            i.putExtra("success_title", "Payment Successful!");
            i.putExtra("success_message",
                    "Your payment has been received. A receipt has been sent to your email.");
            i.putExtra("success_ref", ref);
            i.putExtra("amount", amount);
            i.putExtra("method", methods[selected]);
            i.putExtra("appointment_id", appointmentId);
            i.putExtra("primary_action", "history");
            startActivity(i);
        });
    }

    private void recordPayment() {
        int userId = session.getUserId();
        if (userId <= 0) return;  
        String svc = (service == null || service.isEmpty()) ? "Repair Payment" : service;
        String today = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        db.insertPayment(userId, ref, svc, amount, today, "Paid");
    }

    private void setupMethod(int rowId, int iconRes, String title, String sub, int index) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.ivMethodIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.tvMethodTitle)).setText(title);
        ((TextView) row.findViewById(R.id.tvMethodSub)).setText(sub);
        row.setOnClickListener(v -> select(index));
    }

    private void select(int index) {
        selected = index;
        for (int i = 0; i < rowIds.length; i++) {
            View row = findViewById(rowIds[i]);
            ImageView radio = row.findViewById(R.id.ivRadio);
            boolean on = (i == index);
            row.setBackgroundResource(on ? R.drawable.bg_choice_selected : R.drawable.bg_choice);
            if (on) {
                radio.setBackground(null);
                radio.setImageResource(R.drawable.ic_check_circle);
                radio.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                        PorterDuff.Mode.SRC_IN);
            } else {
                radio.setImageDrawable(null);
                radio.setColorFilter(null);
                radio.setBackgroundResource(R.drawable.bg_circle_stroke);
            }
        }
    }
}
