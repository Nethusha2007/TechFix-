package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.ServiceAdapter;
import com.techfix.app.booking.BookStep1CategoryActivity;
import com.techfix.app.data.SampleData;
import com.techfix.app.model.ServiceItem;

import java.util.ArrayList;
import java.util.List;

/** Full list of repair services with search filtering. Tapping one starts the booking flow. */
public class ServicesActivity extends AppCompatActivity {

    private final List<ServiceItem> allServices = new ArrayList<>();
    private final List<ServiceItem> shown = new ArrayList<>();
    private ServiceAdapter adapter;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        allServices.addAll(SampleData.services());
        shown.addAll(allServices);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        emptyState = findViewById(R.id.emptyState);

        RecyclerView rv = findViewById(R.id.rvServices);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter(shown, R.layout.item_service,
                item -> startActivity(new Intent(this, BookStep1CategoryActivity.class)));
        rv.setAdapter(adapter);

        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filter(String query) {
        String q = query.trim().toLowerCase();
        shown.clear();
        if (q.isEmpty()) {
            shown.addAll(allServices);
        } else {
            for (ServiceItem s : allServices) {
                if (s.name.toLowerCase().contains(q)
                        || s.description.toLowerCase().contains(q)
                        || s.priceText.toLowerCase().contains(q)) {
                    shown.add(s);
                }
            }
        }
        adapter.notifyDataSetChanged();
        emptyState.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
