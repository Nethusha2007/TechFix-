package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.booking.BookStep1CategoryActivity;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;
import com.techfix.app.util.BottomNavHelper;

import java.util.List;

public class AppointmentsActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;
    private RecyclerView rv;
    private View emptyState;
    private TextView chipAll, chipActive, chipCompleted;
    private String filter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        db = new DBHelper(this);
        session = new Session(this);
        rv = findViewById(R.id.rvAppointments);
        emptyState = findViewById(R.id.emptyState);
        chipAll = findViewById(R.id.chipAll);
        chipActive = findViewById(R.id.chipActive);
        chipCompleted = findViewById(R.id.chipCompleted);

        chipAll.setOnClickListener(v -> selectFilter("all"));
        chipActive.setOnClickListener(v -> selectFilter("active"));
        chipCompleted.setOnClickListener(v -> selectFilter("completed"));

        findViewById(R.id.btnBookNow).setOnClickListener(v ->
                startActivity(new Intent(this, BookStep1CategoryActivity.class)));

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        BottomNavHelper.setup(this, nav, R.id.nav_appointments);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        ((BottomNavigationView) findViewById(R.id.bottomNav))
                .setSelectedItemId(R.id.nav_appointments);
    }

    private void selectFilter(String which) {
        filter = which;
        paintChips();
        load();
    }

    private void paintChips() {
        style(chipAll, filter.equals("all"));
        style(chipActive, filter.equals("active"));
        style(chipCompleted, filter.equals("completed"));
    }

    private void style(TextView chip, boolean selected) {
        chip.setBackgroundResource(selected ? R.drawable.bg_chip_selected : R.drawable.bg_chip);
        chip.setTextColor(getResources().getColor(selected ? R.color.white : R.color.textSecondary));
    }

    private void load() {
        int uid = session.getUserId();
        List<Appointment> data;
        switch (filter) {
            case "active":
                data = db.getActiveAppointmentsByUser(uid);
                break;
            case "completed":
                data = db.getAppointmentsByUserAndStatus(uid, "Completed");
                break;
            default:
                data = db.getAppointmentsByUser(uid);
                break;
        }

        if (data.isEmpty()) {
            rv.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }

        rv.setAdapter(new AppointmentAdapter(data, appt -> {
            Intent i = new Intent(this, RepairDetailsActivity.class);
            i.putExtra("appointment_id", appt.id);
            startActivity(i);
        }));
    }
}
