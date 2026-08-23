package com.techfix.app.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.model.Appointment;
import com.techfix.app.util.Money;

public class PaymentSummaryActivity extends AppCompatActivity {

    private static final long DIAGNOSTIC_FEE = 500;
    private static final long SERVICE_FEE = 300;

    private DBHelper db;
    private int appointmentId;
    private long total;
    private String refNo = "";
    private String service = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_summary);

        db = new DBHelper(this);
        appointmentId = getIntent().getIntExtra("appointment_id", -1);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        Appointment a = db.getAppointmentById(appointmentId);
        long repairCharge;
        if (a != null) {
            refNo = a.refNo;
            service = a.service;
            ((ImageView) findViewById(R.id.ivDeviceIcon))
                    .setImageResource(AppointmentAdapter.iconForDevice(a.device));
            ((TextView) findViewById(R.id.tvDevice)).setText(a.device);
            ((TextView) findViewById(R.id.tvService)).setText(a.service);
            ((TextView) findViewById(R.id.tvRef)).setText(a.refNo);
            repairCharge = Money.parse(a.estimatedCost);
        } else {
            repairCharge = 0;
        }

        ((TextView) findViewById(R.id.tvRepairCharge)).setText(Money.format(repairCharge));
        total = repairCharge + DIAGNOSTIC_FEE + SERVICE_FEE;
        ((TextView) findViewById(R.id.tvTotal)).setText(Money.format(total));

        findViewById(R.id.btnProceed).setOnClickListener(v -> {
            Intent i = new Intent(this, PaymentMethodActivity.class);
            i.putExtra("appointment_id", appointmentId);
            i.putExtra("amount", Money.format(total));
            i.putExtra("ref", refNo);
            i.putExtra("service", service);
            startActivity(i);
        });
    }
}
