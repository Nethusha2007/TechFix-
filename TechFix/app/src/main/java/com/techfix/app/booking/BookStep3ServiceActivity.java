package com.techfix.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.ServiceAdapter;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Booking;

/** Booking step 3 of 8 — choose the repair service. */
public class BookStep3ServiceActivity extends AppCompatActivity {

    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step3);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 3 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(3);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("What needs repairing?");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Choose the service that best fits the problem.");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvServices);
        rv.setAdapter(new ServiceAdapter(SampleData.services(), R.layout.item_service, s -> {
            booking.service = s.name;
            booking.priceText = s.priceText;
            Intent i = new Intent(this, BookStep4ProblemActivity.class);
            i.putExtra("booking", booking);
            startActivity(i);
        }));
    }
}
