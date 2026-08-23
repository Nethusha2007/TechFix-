package com.techfix.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.auth.LoginActivity;
import com.techfix.app.data.Session;
import com.techfix.app.payment.PaymentHistoryActivity;

/** App settings: preference toggles, account shortcuts, support links and logout. */
public class SettingsActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new Session(this);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        setupRow(R.id.rowEditProfile, R.drawable.ic_edit, "Edit Profile", null,
                () -> startActivity(new Intent(this, EditProfileActivity.class)));
        setupRow(R.id.rowChangePassword, R.drawable.ic_lock, "Change Password", null,
                () -> toast("Password reset link sent to your email"));
        setupRow(R.id.rowPayments, R.drawable.ic_receipt, "Payment History", null,
                () -> startActivity(new Intent(this, PaymentHistoryActivity.class)));

        setupRow(R.id.rowHelp, R.drawable.ic_help, "Help & Support", null,
                () -> toast("Contact us at support@techfix.lk"));
        setupRow(R.id.rowPrivacy, R.drawable.ic_shield, "Privacy Policy", null,
                () -> toast("Opening privacy policy…"));
        setupRow(R.id.rowAbout, R.drawable.ic_info, "About TechFix", "Version 1.0.0",
                () -> toast("TechFix Repair Management · v1.0.0"));

        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());
    }

    private void setupRow(int rowId, int iconRes, String title, String sub, Runnable onClick) {
        View row = findViewById(rowId);
        ((ImageView) row.findViewById(R.id.srIcon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.srTitle)).setText(title);
        TextView subView = row.findViewById(R.id.srSub);
        if (sub != null) {
            subView.setText(sub);
            subView.setVisibility(View.VISIBLE);
        } else {
            subView.setVisibility(View.GONE);
        }
        row.setOnClickListener(v -> onClick.run());
    }

    private void logout() {
        session.logout();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
