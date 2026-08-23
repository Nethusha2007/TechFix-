package com.techfix.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.CategoryAdapter;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.Booking;
import com.techfix.app.model.DeviceCategory;

/** Booking step 1 of 8 — choose the device category. */
public class BookStep1CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step1);

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 1 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(1);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("What are you fixing?");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Select the type of device you need repaired.");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // "Book similar repair" may pre-select a category and skip straight ahead.
        String preselect = getIntent().getStringExtra("preselect_category");
        if (preselect != null && !preselect.isEmpty()) {
            proceed(preselect);
        }

        RecyclerView rv = findViewById(R.id.rvCategories);
        rv.setAdapter(new CategoryAdapter(SampleData.deviceCategories(),
                R.layout.item_category_grid, (DeviceCategory c) -> proceed(c.name)));
    }

    private void proceed(String category) {
        Booking booking = new Booking();
        booking.category = category;
        Intent i = new Intent(this, BookStep2DeviceActivity.class);
        i.putExtra("booking", booking);
        startActivity(i);
    }
}
