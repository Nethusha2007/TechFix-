package com.techfix.app.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.customer.AppointmentsActivity;
import com.techfix.app.customer.HomeActivity;
import com.techfix.app.customer.RepairDetailsActivity;


public class PaymentSuccessActivity extends AppCompatActivity {

    private int appointmentId = -1;
    private String action = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        Intent in = getIntent();
        appointmentId = in.getIntExtra("appointment_id", -1);
        action = in.getStringExtra("primary_action");
        if (action == null) action = "home";

        String title = in.getStringExtra("success_title");
        String message = in.getStringExtra("success_message");
        String ref = in.getStringExtra("success_ref");
        String amount = in.getStringExtra("amount");
        String method = in.getStringExtra("method");

        ((TextView) findViewById(R.id.tvTitle))
                .setText(title != null ? title : "Success!");
        ((TextView) findViewById(R.id.tvMessage))
                .setText(message != null ? message : "Your request has been completed.");
        ((TextView) findViewById(R.id.tvRefValue))
                .setText(ref != null && !ref.isEmpty() ? ref : "—");

        if (amount != null && !amount.isEmpty()) {
            ((TextView) findViewById(R.id.tvAmountValue)).setText(amount);
        } else {
            findViewById(R.id.rowAmount).setVisibility(View.GONE);
        }
        if (method != null && !method.isEmpty()) {
            ((TextView) findViewById(R.id.tvMethodValue)).setText(method);
        } else {
            findViewById(R.id.rowMethod).setVisibility(View.GONE);
        }

        TextView btnPrimary = findViewById(R.id.btnPrimary);
        switch (action) {
            case "appointment":
                btnPrimary.setText("View My Bookings");
                break;
            case "history":
                btnPrimary.setText("View Payment History");
                break;
            case "details":
                btnPrimary.setText("View Details");
                break;
            default:
                btnPrimary.setText("Done");
                break;
        }

        btnPrimary.setOnClickListener(v -> runPrimary());
        findViewById(R.id.btnSecondary).setOnClickListener(v -> goHome());
    }

    private void runPrimary() {
        switch (action) {
            case "appointment":
                launch(new Intent(this, AppointmentsActivity.class));
                break;
            case "history":
                launch(new Intent(this, PaymentHistoryActivity.class));
                break;
            case "details":
                Intent d = new Intent(this, RepairDetailsActivity.class);
                d.putExtra("appointment_id", appointmentId);
                launch(d);
                break;
            default:
                goHome();
                break;
        }
    }

    private void goHome() {
        launch(new Intent(this, HomeActivity.class));
    }

    private void launch(Intent target) {
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(target);
        finish();
    }

    @Override
    public void onBackPressed() {
        goHome();
    }
}
