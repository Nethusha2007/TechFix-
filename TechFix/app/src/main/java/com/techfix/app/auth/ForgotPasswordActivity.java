package com.techfix.app.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.data.DBHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        db = new DBHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnBackToLogin).setOnClickListener(v -> finish());
        findViewById(R.id.btnReset).setOnClickListener(v -> attemptReset());
    }

    private void attemptReset() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!db.emailExists(email)) {
            etEmail.setError("No account found with this email");
            etEmail.requestFocus();
            return;
        }

        boolean updated = db.updatePassword(email, password);
        if (updated) {
            Toast.makeText(this, "Password reset successful. Please sign in.",
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Could not reset password. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
