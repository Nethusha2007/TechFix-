package com.techfix.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.model.Booking;

/** Booking step 4 of 8 — describe the problem. */
public class BookStep4ProblemActivity extends AppCompatActivity {

    private Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step4);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 4 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(4);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("Describe the problem");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Tell us what's going wrong with your device.");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        EditText etProblem = findViewById(R.id.etProblem);
        if (!booking.problem.isEmpty()) etProblem.setText(booking.problem);

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            String text = etProblem.getText().toString().trim();
            if (text.length() < 5) {
                Toast.makeText(this, "Please describe the problem (at least 5 characters)",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            booking.problem = text;
            Intent i = new Intent(this, BookStep5ImagesActivity.class);
            i.putExtra("booking", booking);
            startActivity(i);
        });
    }
}
