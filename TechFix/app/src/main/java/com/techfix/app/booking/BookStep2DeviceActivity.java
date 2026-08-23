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

/** Booking step 2 of 8 — choose the device brand / model. */
public class BookStep2DeviceActivity extends AppCompatActivity {

    private Booking booking;
    private ChoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step2);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 2 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(2);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("Which device is it?");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Pick the brand that matches your " + booking.category.toLowerCase() + ".");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        int preselect = indexOf(booking.deviceBrand);
        adapter = new ChoiceAdapter(SampleData.deviceBrands(booking.category), preselect,
                (index, value) -> booking.deviceBrand = value);

        RecyclerView rv = findViewById(R.id.rvChoices);
        rv.setAdapter(adapter);

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            String brand = adapter.getSelectedValue();
            if (brand == null) {
                Toast.makeText(this, "Please select a device", Toast.LENGTH_SHORT).show();
                return;
            }
            booking.deviceBrand = brand;
            Intent i = new Intent(this, BookStep3ServiceActivity.class);
            i.putExtra("booking", booking);
            startActivity(i);
        });
    }

    private int indexOf(String value) {
        if (value == null || value.isEmpty()) return -1;
        return SampleData.deviceBrands(booking.category).indexOf(value);
    }
}
