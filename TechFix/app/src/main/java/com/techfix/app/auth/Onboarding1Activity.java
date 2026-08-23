package com.techfix.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;

public class Onboarding1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);

        findViewById(R.id.btnNext).setOnClickListener(v ->
                startActivity(new Intent(this, Onboarding2Activity.class)));

        findViewById(R.id.btnSkip).setOnClickListener(this::goToLogin);
    }

    private void goToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
