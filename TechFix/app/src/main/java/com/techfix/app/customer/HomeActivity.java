package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.adapter.CategoryAdapter;
import com.techfix.app.adapter.ServiceAdapter;
import com.techfix.app.booking.BookStep1CategoryActivity;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.SampleData;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;
import com.techfix.app.settings.NotificationsActivity;
import com.techfix.app.util.BottomNavHelper;
import com.techfix.app.util.StatusUtil;

import java.util.List;

/** Customer home dashboard. */
public class HomeActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;

    private View cardActiveRepair;
    private TextView tvActiveLabel, tvActiveDevice, tvActiveService, tvActiveStatus, tvActiveStep;
    private ProgressBar progressActive;
    private int activeAppointmentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DBHelper(this);
        session = new Session(this);

        cardActiveRepair = findViewById(R.id.cardActiveRepair);
        tvActiveLabel = findViewById(R.id.tvActiveLabel);
        tvActiveDevice = findViewById(R.id.tvActiveDevice);
        tvActiveService = findViewById(R.id.tvActiveService);
        tvActiveStatus = findViewById(R.id.tvActiveStatus);
        tvActiveStep = findViewById(R.id.tvActiveStep);
        progressActive = findViewById(R.id.progressActive);

        // Quick actions
        findViewById(R.id.actionBook).setOnClickListener(v -> startBooking());
        findViewById(R.id.actionServices).setOnClickListener(v ->
                startActivity(new Intent(this, ServicesActivity.class)));
        findViewById(R.id.actionBranches).setOnClickListener(v ->
                startActivity(new Intent(this, BranchesActivity.class)));
        findViewById(R.id.actionTrack).setOnClickListener(v -> openActiveTracking());

        findViewById(R.id.btnNotifications).setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));
        findViewById(R.id.searchBar).setOnClickListener(v ->
                startActivity(new Intent(this, ServicesActivity.class)));
        findViewById(R.id.btnSeeAllServices).setOnClickListener(v ->
                startActivity(new Intent(this, ServicesActivity.class)));

        findViewById(R.id.btnTrackActive).setOnClickListener(v -> openActiveTracking());

        setupLists();

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        BottomNavHelper.setup(this, nav, R.id.nav_home);
    }

    private void setupLists() {
        RecyclerView rvServices = findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvServices.setNestedScrollingEnabled(false);
        rvServices.setAdapter(new ServiceAdapter(SampleData.services(), R.layout.item_service_home,
                item -> startBooking()));

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setNestedScrollingEnabled(false);
        rvCategories.setAdapter(new CategoryAdapter(SampleData.deviceCategories(),
                item -> startBooking()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.tvGreetingName)).setText(session.getName());
        bindActiveRepair();
        ((BottomNavigationView) findViewById(R.id.bottomNav)).setSelectedItemId(R.id.nav_home);
    }

    private void bindActiveRepair() {
        List<Appointment> active = db.getActiveAppointmentsByUser(session.getUserId());
        if (active.isEmpty()) {
            cardActiveRepair.setVisibility(View.GONE);
            tvActiveLabel.setVisibility(View.GONE);
            activeAppointmentId = -1;
            return;
        }
        cardActiveRepair.setVisibility(View.VISIBLE);
        tvActiveLabel.setVisibility(View.VISIBLE);

        Appointment a = active.get(0);
        activeAppointmentId = a.id;
        tvActiveDevice.setText(a.device);
        tvActiveService.setText(a.service);
        StatusUtil.applyStatus(tvActiveStatus, a.status);
        progressActive.setMax(7);
        progressActive.setProgress(a.trackStep);
        tvActiveStep.setText(SampleData.stepTitle(a.trackStep));
    }

    private void openActiveTracking() {
        Intent intent = new Intent(this, RepairTrackingActivity.class);
        if (activeAppointmentId != -1) {
            intent.putExtra("appointment_id", activeAppointmentId);
        }
        startActivity(intent);
    }

    private void startBooking() {
        startActivity(new Intent(this, BookStep1CategoryActivity.class));
    }
}
