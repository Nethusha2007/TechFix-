package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.SampleData;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;
import com.techfix.app.model.Branch;
import com.techfix.app.payment.PaymentSummaryActivity;
import com.techfix.app.util.MapUtil;

import java.util.List;

public class ReadyForCollectionActivity extends AppCompatActivity {

    private DBHelper db;
    private Appointment appt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_for_collection);

        db = new DBHelper(this);
        Session session = new Session(this);
        int id = getIntent().getIntExtra("appointment_id", -1);

        appt = db.getAppointmentById(id);
        if (appt == null) {
            List<Appointment> done =
                    db.getAppointmentsByUserAndStatus(session.getUserId(), "Completed");
            if (!done.isEmpty()) appt = done.get(0);
        }
        if (appt == null) {
            finish();
            return;
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        ((ImageView) findViewById(R.id.ivDeviceIcon))
                .setImageResource(AppointmentAdapter.iconForDevice(appt.device));
        ((TextView) findViewById(R.id.tvDevice)).setText(appt.device);
        ((TextView) findViewById(R.id.tvRef)).setText(appt.refNo);
        ((TextView) findViewById(R.id.tvAmount)).setText(appt.estimatedCost);

        Branch branch = findBranch(appt.branch);
        if (branch != null) {
            ((TextView) findViewById(R.id.tvBranch)).setText(branch.name);
            ((TextView) findViewById(R.id.tvBranchAddress)).setText(branch.address);
            ((TextView) findViewById(R.id.tvBranchHours)).setText(branch.hours);
        } else {
            ((TextView) findViewById(R.id.tvBranch)).setText(appt.branch);
        }

        findViewById(R.id.btnPay).setOnClickListener(v -> {
            Intent i = new Intent(this, PaymentSummaryActivity.class);
            i.putExtra("appointment_id", appt.id);
            startActivity(i);
        });

        findViewById(R.id.btnDirections).setOnClickListener(v -> openDirections(branch));
    }

    private Branch findBranch(String name) {
        if (name == null) return null;
        for (Branch b : SampleData.branches()) {
            if (name.contains(b.name) || b.name.contains(name)
                    || (b.city != null && name.contains(b.city))) {
                return b;
            }
        }
        return null;
    }

    private void openDirections(Branch branch) {
        String label = branch != null ? branch.name + ", " + branch.address
                : (appt.branch == null ? "TechFix" : appt.branch);
        double lat = branch != null ? branch.lat : 0;
        double lng = branch != null ? branch.lng : 0;
        MapUtil.openDirections(this, lat, lng, label);
    }
}
