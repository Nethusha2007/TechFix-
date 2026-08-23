package com.techfix.app.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Appointment;
import com.techfix.app.util.StatusUtil;

import java.util.List;

public class AdminAppointmentDetailsActivity extends AppCompatActivity {

    private static final String[] STATUS_OPTIONS = {
            "Pending", "In Progress", "Ready for Collection", "Completed"
    };
    private static final int[] STATUS_STEPS = {0, 4, 6, 7};

    private DBHelper db;
    private int appointmentId;
    private Appointment appointment;

    private ImageView ivDeviceIcon;
    private TextView tvDevice, tvRef, tvStatus, tvDescription, tvTechnician;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_appointment_details);

        db = new DBHelper(this);
        appointmentId = getIntent().getIntExtra("appointment_id", -1);

        ivDeviceIcon = findViewById(R.id.ivDeviceIcon);
        tvDevice = findViewById(R.id.tvDevice);
        tvRef = findViewById(R.id.tvRef);
        tvStatus = findViewById(R.id.tvStatus);
        tvDescription = findViewById(R.id.tvDescription);
        tvTechnician = findViewById(R.id.tvTechnician);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAssign).setOnClickListener(v -> showAssignDialog());
        findViewById(R.id.btnUpdateStatus).setOnClickListener(v -> showStatusDialog());

        loadAppointment();
    }

    private void loadAppointment() {
        appointment = db.getAppointmentById(appointmentId);
        if (appointment == null) {
            Toast.makeText(this, "Appointment not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ivDeviceIcon.setImageResource(AppointmentAdapter.iconForDevice(appointment.device));
        tvDevice.setText(appointment.device);
        tvRef.setText(appointment.refNo);
        StatusUtil.applyStatus(tvStatus, appointment.status);

        setRow(R.id.rowCustomer, "Customer", value(appointment.customerName, "—"));
        setRow(R.id.rowService, "Service", appointment.service);
        setRow(R.id.rowBranch, "Branch", appointment.branch);
        setRow(R.id.rowDateTime, "Date & Time", appointment.dateTime);
        setRow(R.id.rowCost, "Estimated Cost", value(appointment.estimatedCost, "—"));

        tvDescription.setText(value(appointment.description, "No description provided."));
        tvTechnician.setText(value(appointment.technicianName, "Not assigned yet"));
    }

    private void showAssignDialog() {
        List<String> names = SampleData.technicianNames();
        final String[] arr = names.toArray(new String[0]);
        new AlertDialog.Builder(this)
                .setTitle("Assign Technician")
                .setItems(arr, (dialog, which) -> {
                    String tech = arr[which];
                    if (db.assignTechnician(appointmentId, tech)) {
                        Toast.makeText(this, "Assigned to " + tech, Toast.LENGTH_SHORT).show();
                        loadAppointment();
                    } else {
                        Toast.makeText(this, "Could not assign technician.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showStatusDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Update Status")
                .setItems(STATUS_OPTIONS, (dialog, which) -> {
                    String status = STATUS_OPTIONS[which];
                    int step = STATUS_STEPS[which];
                    if (db.updateAppointmentStatus(appointmentId, status, step)) {
                        Toast.makeText(this, "Status updated to " + status, Toast.LENGTH_SHORT).show();
                        loadAppointment();
                    } else {
                        Toast.makeText(this, "Could not update status.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /** Fills one item_detail_row include (scoped lookups since rows share child ids). */
    private void setRow(int rowId, String label, String value) {
        View row = findViewById(rowId);
        ((TextView) row.findViewById(R.id.tvLabel)).setText(label);
        ((TextView) row.findViewById(R.id.tvValue)).setText(value);
    }

    private String value(String v, String fallback) {
        return TextUtils.isEmpty(v) ? fallback : v;
    }
}
