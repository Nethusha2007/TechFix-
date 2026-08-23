package com.techfix.app.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.adapter.TechnicianAdapter;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Technician;
import com.techfix.app.util.AdminNavHelper;

import java.util.List;

/** Admin technicians roster with availability status. */
public class AdminTechniciansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_technicians);

        List<Technician> technicians = SampleData.technicians();

        int available = 0;
        for (Technician t : technicians) {
            if (t.available) available++;
        }
        TextView tvSummary = findViewById(R.id.tvSummary);
        tvSummary.setText(technicians.size() + " technicians · " + available + " available");

        RecyclerView rv = findViewById(R.id.rvTechnicians);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TechnicianAdapter(technicians, t ->
                Toast.makeText(this,
                        t.name + " · " + (t.available ? "Available" : "Busy"),
                        Toast.LENGTH_SHORT).show()));

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        AdminNavHelper.setup(this, nav, R.id.nav_technicians);
    }
}
