package com.techfix.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.model.Appointment;
import com.techfix.app.util.AdminNavHelper;

import java.util.List;

public class AdminAppointmentsActivity extends AppCompatActivity {

    private DBHelper db;
    private RecyclerView rv;
    private View emptyState;
    private TextView tvCount;
    private TextView chipAll, chipPending, chipProgress, chipCompleted;
    private String filter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_appointments);

        db = new DBHelper(this);
        rv = findViewById(R.id.rvAppointments);
        emptyState = findViewById(R.id.emptyState);
        tvCount = findViewById(R.id.tvCount);
        chipAll = findViewById(R.id.chipAll);
        chipPending = findViewById(R.id.chipPending);
        chipProgress = findViewById(R.id.chipProgress);
        chipCompleted = findViewById(R.id.chipCompleted);

        chipAll.setOnClickListener(v -> selectFilter("all"));
        chipPending.setOnClickListener(v -> selectFilter("Pending"));
        chipProgress.setOnClickListener(v -> selectFilter("In Progress"));
        chipCompleted.setOnClickListener(v -> selectFilter("Completed"));

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        AdminNavHelper.setup(this, nav, R.id.nav_admin_appointments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        ((BottomNavigationView) findViewById(R.id.bottomNav))
                .setSelectedItemId(R.id.nav_admin_appointments);
    }

    private void selectFilter(String which) {
        filter = which;
        paintChips();
        load();
    }

    private void paintChips() {
        style(chipAll, filter.equals("all"));
        style(chipPending, filter.equals("Pending"));
        style(chipProgress, filter.equals("In Progress"));
        style(chipCompleted, filter.equals("Completed"));
    }

    private void style(TextView chip, boolean selected) {
        chip.setBackgroundResource(selected ? R.drawable.bg_chip_selected : R.drawable.bg_chip);
        chip.setTextColor(getResources().getColor(selected ? R.color.white : R.color.textSecondary));
    }

    private void load() {
        List<Appointment> data = filter.equals("all")
                ? db.getAllAppointments()
                : db.getAppointmentsByStatus(filter);

        tvCount.setText(data.size() + (data.size() == 1 ? " booking" : " bookings"));

        if (data.isEmpty()) {
            rv.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }

        rv.setAdapter(new AppointmentAdapter(data, appt -> {
            Intent i = new Intent(this, AdminAppointmentDetailsActivity.class);
            i.putExtra("appointment_id", appt.id);
            startActivity(i);
        }));
    }
}
