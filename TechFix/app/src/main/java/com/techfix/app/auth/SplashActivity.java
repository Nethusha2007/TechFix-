package com.techfix.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.admin.AdminDashboardActivity;
import com.techfix.app.customer.HomeActivity;
import com.techfix.app.data.Session;

/** Branded splash screen. Routes to the correct start screen after a short delay. */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1800L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(this::routeNext, SPLASH_DELAY_MS);
    }

    private void routeNext() {
        Session session = new Session(this);
        Intent intent;
        if (session.isLoggedIn()) {
            intent = session.isAdmin()
                    ? new Intent(this, AdminDashboardActivity.class)
                    : new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, Onboarding1Activity.class);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
