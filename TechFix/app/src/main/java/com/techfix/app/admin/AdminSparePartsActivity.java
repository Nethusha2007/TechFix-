package com.techfix.app.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.adapter.SparePartAdapter;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.SparePart;
import com.techfix.app.util.AdminNavHelper;

import java.util.List;

/** Admin spare-parts inventory with stock availability status. */
public class AdminSparePartsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_spare_parts);

        List<SparePart> parts = SampleData.spareParts();

        int inStock = 0;
        for (SparePart p : parts) {
            if (p.stock > 0) inStock++;
        }
        TextView tvSummary = findViewById(R.id.tvSummary);
        tvSummary.setText(parts.size() + " parts · " + inStock + " available");

        RecyclerView rv = findViewById(R.id.rvSpareParts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new SparePartAdapter(parts, p ->
                Toast.makeText(this,
                        p.name + " · " + p.status(),
                        Toast.LENGTH_SHORT).show()));

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        AdminNavHelper.setup(this, nav, R.id.nav_parts);
    }
}
