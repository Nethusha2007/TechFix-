package com.techfix.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.ChoiceAdapter;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Booking;
import com.techfix.app.model.Branch;

import java.util.ArrayList;
import java.util.List;

/** Booking step 6 of 8 — choose the branch to drop off / collect the device. */
public class BookStep6BranchActivity extends AppCompatActivity {

    private Booking booking;
    private ChoiceAdapter adapter;
    private List<String> branchNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step6);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 6 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(6);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("Choose a branch");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Where would you like to hand in your device?");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        branchNames = new ArrayList<>();
        for (Branch b : SampleData.branches()) branchNames.add(b.name);

        int preselect = branchNames.indexOf(booking.branch);
        adapter = new ChoiceAdapter(branchNames, preselect,
                (index, value) -> booking.branch = value);

        RecyclerView rv = findViewById(R.id.rvChoices);
        rv.setAdapter(adapter);

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            String branch = adapter.getSelectedValue();
            if (branch == null) {
                Toast.makeText(this, "Please select a branch", Toast.LENGTH_SHORT).show();
                return;
            }
            booking.branch = branch;
            Intent i = new Intent(this, BookStep7DateTimeActivity.class);
            i.putExtra("booking", booking);
            startActivity(i);
        });
    }
}
