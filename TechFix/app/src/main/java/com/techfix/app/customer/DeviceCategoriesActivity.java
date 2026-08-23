package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.CategoryAdapter;
import com.techfix.app.booking.BookStep1CategoryActivity;
import com.techfix.app.data.SampleData;

/** Grid of device categories. Selecting one starts the booking flow for that category. */
public class DeviceCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_categories);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvCategories);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new CategoryAdapter(SampleData.deviceCategories(), R.layout.item_category_grid,
                category -> {
                    Intent intent = new Intent(this, BookStep1CategoryActivity.class);
                    intent.putExtra("preselect_category", category.name);
                    startActivity(intent);
                }));
    }
}
