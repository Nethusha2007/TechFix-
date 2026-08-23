package com.techfix.app.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.auth.LoginActivity;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.payment.PaymentHistoryActivity;
import com.techfix.app.settings.EditProfileActivity;
import com.techfix.app.settings.NotificationsActivity;
import com.techfix.app.settings.SettingsActivity;
import com.techfix.app.util.BottomNavHelper;

/** Customer profile hub with account stats, menu shortcuts and logout. */
public class ProfileActivity extends AppCompatActivity {

    private Session session;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new Session(this);
        db = new DBHelper(this);

        configureRow(R.id.rowAppointments, R.drawable.ic_appointments, "My Appointments",
                v -> startActivity(new Intent(this, AppointmentsActivity.class)));
        configureRow(R.id.rowHistory, R.drawable.ic_track, "Repair History",
                v -> startActivity(new Intent(this, RepairHistoryActivity.class)));
        configureRow(R.id.rowPayments, R.drawable.ic_receipt, "Payments",
                v -> startActivity(new Intent(this, PaymentHistoryActivity.class)));
        configureRow(R.id.rowNotifications, R.drawable.ic_notification, "Notifications",
                v -> startActivity(new Intent(this, NotificationsActivity.class)));

        configureRow(R.id.rowEditProfile, R.drawable.ic_edit, "Edit Profile",
                v -> startActivity(new Intent(this, EditProfileActivity.class)));
        configureRow(R.id.rowSettings, R.drawable.ic_settings, "Settings",
                v -> startActivity(new Intent(this, SettingsActivity.class)));
        configureRow(R.id.rowHelp, R.drawable.ic_help, "Help & Support",
                v -> startActivity(new Intent(this, SettingsActivity.class)));

        findViewById(R.id.btnEditProfile).setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));
        findViewById(R.id.btnSettingsTop).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        BottomNavHelper.setup(this, nav, R.id.nav_profile);
    }

    private void configureRow(int rowId, int iconRes, String title, View.OnClickListener click) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.ivRowIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.tvRowTitle)).setText(title);
        row.setOnClickListener(click);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.tvProfileName)).setText(session.getName());
        ((TextView) findViewById(R.id.tvProfileEmail)).setText(session.getEmail());

        int total = db.countAllForUser(session.getUserId());
        int done = db.countByStatusForUser(session.getUserId(), "Completed");
        int active = total - done;
        ((TextView) findViewById(R.id.tvStatTotal)).setText(String.valueOf(total));
        ((TextView) findViewById(R.id.tvStatActive)).setText(String.valueOf(Math.max(active, 0)));
        ((TextView) findViewById(R.id.tvStatDone)).setText(String.valueOf(done));

        ((BottomNavigationView) findViewById(R.id.bottomNav)).setSelectedItemId(R.id.nav_profile);
    }

    private void logout() {
        session.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
