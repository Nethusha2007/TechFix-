package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;

import java.util.List;

public class RepairHistoryActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_history);

        db = new DBHelper(this);
        session = new Session(this);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Appointment> history =
                db.getAppointmentsByUserAndStatus(session.getUserId(), "Completed");

        RecyclerView rv = findViewById(R.id.rvHistory);
        View empty = findViewById(R.id.emptyState);

        if (history.isEmpty()) {
            rv.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }

        rv.setAdapter(new AppointmentAdapter(history, appt -> {
            Intent i = new Intent(this, RepairHistoryDetailActivity.class);
            i.putExtra("appointment_id", appt.id);
            startActivity(i);
        }));
    }
}
