package com.techfix.app.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Appointment;
import com.techfix.app.model.TrackStep;

import java.util.List;

public class RepairUpdateActivity extends AppCompatActivity {

    private DBHelper db;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_update);

        db = new DBHelper(this);
        appointmentId = getIntent().getIntExtra("appointment_id", -1);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    private void bind() {
        Appointment a = db.getAppointmentById(appointmentId);
        if (a == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvDevice)).setText(a.device);
        ((TextView) findViewById(R.id.tvRef)).setText(a.refNo);

        boolean completed = a.status.equalsIgnoreCase("Completed")
                || a.status.equalsIgnoreCase("Collected");
        List<TrackStep> steps = SampleData.trackSteps(completed ? 8 : a.trackStep);

        LinearLayout container = findViewById(R.id.updatesContainer);
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        boolean any = false;
        for (int i = steps.size() - 1; i >= 0; i--) {
            TrackStep s = steps.get(i);
            if (s.state == TrackStep.PENDING) continue;
            any = true;

            View card = inflater.inflate(R.layout.item_update, (ViewGroup) container, false);
            ((TextView) card.findViewById(R.id.tvUpdateTitle)).setText(s.title);
            ((TextView) card.findViewById(R.id.tvUpdateNote)).setText(noteFor(s.title));
            ((TextView) card.findViewById(R.id.tvUpdateTime))
                    .setText(s.time.isEmpty() ? "Just now" : s.time);
            container.addView(card);
        }

        if (!any) {
            TextView empty = new TextView(this);
            empty.setText("No updates yet. We'll post progress here as your repair moves forward.");
            empty.setTextColor(getResources().getColor(R.color.textSecondary));
            empty.setTextSize(14f);
            container.addView(empty);
        }
    }

    private String noteFor(String title) {
        switch (title) {
            case "Booking Confirmed":
                return "Your repair booking has been confirmed. We'll keep you posted at every stage.";
            case "Device Received":
                return "We've received your device at the branch and logged it for repair.";
            case "Diagnosis Started":
                return "Our technician has started diagnosing the reported issue.";
            case "Diagnosis Complete":
                return "Diagnosis is complete and a repair plan has been prepared.";
            case "Repair In Progress":
                return "Our technician is now actively working on your device.";
            case "Quality Check":
                return "Repair finished — the device is going through final quality checks.";
            case "Ready for Collection":
                return "Great news! Your device is ready to collect from the branch.";
            case "Repair Completed":
                return "Repair completed and handed over. Thank you for choosing TechFix.";
            default:
                return "Status updated to \"" + title + "\".";
        }
    }
}
