package com.techfix.app.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.techfix.app.R;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Branch;
import com.techfix.app.util.MapUtil;

/** Stylized "nearby branches" map with a selected-branch detail card. */
public class BranchMapActivity extends AppCompatActivity {

    private String phone = "+94 11 234 5678";
    private String name = "TechFix Colombo";
    private String address = "No. 24, Galle Road, Colombo 03";
    private double lat = 0;
    private double lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_map);

        // Default to the first branch, override with any passed extras.
        Branch first = SampleData.branches().get(0);
        name = getIntent().getStringExtra("branch_name");
        if (name == null) name = first.name;
        address = getIntent().getStringExtra("branch_address");
        if (address == null) address = first.address;
        String hours = getIntent().getStringExtra("branch_hours");
        if (hours == null) hours = first.hours;
        String distance = getIntent().getStringExtra("branch_distance");
        if (distance == null) distance = first.distance;

        // Resolve the selected branch to pick up its phone + coordinates.
        Branch selected = first;
        for (Branch b : SampleData.branches()) {
            if (b.name.equals(name)) {
                selected = b;
                break;
            }
        }
        phone = selected.phone;
        lat = selected.lat;
        lng = selected.lng;

        ((TextView) findViewById(R.id.tvBranchName)).setText(name);
        ((TextView) findViewById(R.id.tvBranchAddress)).setText(address);
        ((TextView) findViewById(R.id.tvBranchHours)).setText(hours);
        ((TextView) findViewById(R.id.tvBranchDistance)).setText(distance);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        MaterialButton btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
            } catch (Exception e) {
                Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton btnDirections = findViewById(R.id.btnDirections);
        btnDirections.setOnClickListener(v -> {
            try {
                MapUtil.openDirections(this, lat, lng, name + ", " + address);
            } catch (Exception e) {
                Toast.makeText(this, "Opening directions…", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
