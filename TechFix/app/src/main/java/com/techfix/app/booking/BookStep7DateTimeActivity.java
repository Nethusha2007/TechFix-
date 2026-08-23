package com.techfix.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.techfix.app.R;
import com.techfix.app.model.Booking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/** Booking step 7 of 8 — pick an appointment date and time slot. */
public class BookStep7DateTimeActivity extends AppCompatActivity {

    private static final String[] TIME_SLOTS = {
            "09:00 AM", "10:30 AM", "12:00 PM", "01:30 PM", "03:00 PM", "04:30 PM"
    };

    private Booking booking;
    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step7);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();
        selectedDate = booking.date;
        selectedTime = booking.time;

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 7 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(7);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("Pick a date & time");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Choose when you'd like to drop off your device.");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        LayoutInflater inflater = LayoutInflater.from(this);
        ChipGroup dates = findViewById(R.id.chipGroupDates);
        ChipGroup times = findViewById(R.id.chipGroupTimes);

        // Next 8 days as date chips.
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        for (int i = 0; i < 8; i++) {
            String label = fmt.format(cal.getTime());
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip_choice, dates, false);
            chip.setText(label);
            chip.setChecked(label.equals(selectedDate));
            chip.setOnCheckedChangeListener((b, checked) -> {
                if (checked) selectedDate = label;
            });
            dates.addView(chip);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        for (String slot : TIME_SLOTS) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip_choice, times, false);
            chip.setText(slot);
            chip.setChecked(slot.equals(selectedTime));
            chip.setOnCheckedChangeListener((b, checked) -> {
                if (checked) selectedTime = slot;
            });
            times.addView(chip);
        }

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
                return;
            }
            booking.date = selectedDate;
            booking.time = selectedTime;
            Intent i = new Intent(this, BookStep8ConfirmActivity.class);
            i.putExtra("booking", booking);
            startActivity(i);
        });
    }
}
