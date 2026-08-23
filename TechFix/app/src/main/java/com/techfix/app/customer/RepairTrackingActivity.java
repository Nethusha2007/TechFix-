package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.adapter.TrackStepAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Appointment;
import com.techfix.app.model.TrackStep;
import com.techfix.app.util.StatusUtil;

import java.util.List;

/** Shows the live 8-step repair-tracking timeline for one appointment. */
public class RepairTrackingActivity extends AppCompatActivity {

    private DBHelper db;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_tracking);

        db = new DBHelper(this);
        appointmentId = getIntent().getIntExtra("appointment_id", -1);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnViewDetails).setOnClickListener(v -> {
            Intent i = new Intent(this, RepairDetailsActivity.class);
            i.putExtra("appointment_id", appointmentId);
            startActivity(i);
        });
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

        ((ImageView) findViewById(R.id.ivDeviceIcon))
                .setImageResource(AppointmentAdapter.iconForDevice(a.device));
        ((TextView) findViewById(R.id.tvDevice)).setText(a.device);
        ((TextView) findViewById(R.id.tvService)).setText(a.service);
        ((TextView) findViewById(R.id.tvRef)).setText(a.refNo);
        StatusUtil.applyStatus(findViewById(R.id.tvStatus), a.status);

        boolean completed = a.status.equalsIgnoreCase("Completed")
                || a.status.equalsIgnoreCase("Collected");
        ((TextView) findViewById(R.id.tvEta))
                .setText(completed ? "Completed" : "Est. 2–3 working days");

        String tech = (a.technicianName == null || a.technicianName.trim().isEmpty())
                ? "Not assigned yet" : a.technicianName;
        ((TextView) findViewById(R.id.tvTechnician)).setText(tech);

        List<TrackStep> steps = SampleData.trackSteps(completed ? 8 : a.trackStep);
        RecyclerView rv = findViewById(R.id.rvTimeline);
        rv.setAdapter(new TrackStepAdapter(steps));

        int total = steps.size();
        float units = completed ? total : (a.trackStep + 0.5f);
        int percent = Math.max(0, Math.min(100, Math.round(units * 100f / total)));
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(percent);
        ((TextView) findViewById(R.id.tvPercent)).setText(percent + "%");
    }
}
