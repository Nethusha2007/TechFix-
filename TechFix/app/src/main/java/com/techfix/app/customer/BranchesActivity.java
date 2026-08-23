package com.techfix.app.customer;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.BranchAdapter;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Branch;
import com.techfix.app.util.MapUtil;

import java.util.List;

/** Lists all TechFix branches with call / directions actions and GPS "nearest branch". */
public class BranchesActivity extends AppCompatActivity {

    private static final int REQ_LOCATION = 4101;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnMap).setOnClickListener(v -> openMap());
        findViewById(R.id.btnOpenMap).setOnClickListener(v -> openMap());

        rv = findViewById(R.id.rvBranches);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Show the branches immediately; upgrade to nearest-first once we have a location.
        showBranches(SampleData.branches());
        requestNearest();
    }

    /** Requests location permission (if needed) then re-orders branches by distance. */
    private void requestNearest() {
        if (!MapUtil.hasLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQ_LOCATION);
            return;
        }
        applyNearest();
    }

    private void applyNearest() {
        Location loc = MapUtil.lastKnownLocation(this);
        List<Branch> branches = SampleData.branches();
        if (loc != null) {
            MapUtil.sortByDistance(branches, loc.getLatitude(), loc.getLongitude());
            Branch nearest = branches.get(0);
            Toast.makeText(this, "Nearest: " + nearest.name + " · " + nearest.distance,
                    Toast.LENGTH_SHORT).show();
        }
        showBranches(branches);
    }

    private void showBranches(List<Branch> branches) {
        rv.setAdapter(new BranchAdapter(branches, branch -> {
            Intent intent = new Intent(this, BranchMapActivity.class);
            intent.putExtra("branch_name", branch.name);
            intent.putExtra("branch_address", branch.address);
            intent.putExtra("branch_hours", branch.hours);
            intent.putExtra("branch_distance", branch.distance);
            startActivity(intent);
        }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            boolean granted = false;
            for (int r : grantResults) {
                if (r == android.content.pm.PackageManager.PERMISSION_GRANTED) granted = true;
            }
            if (granted) {
                applyNearest();
            }
            // If denied, we simply keep the default (static) branch list already shown.
        }
    }

    private void openMap() {
        startActivity(new Intent(this, BranchMapActivity.class));
    }
}
