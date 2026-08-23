package com.techfix.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.User;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ImageView btnTogglePassword;
    private boolean passwordVisible = false;
    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        db = new DBHelper(this);
        session = new Session(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        etEmail.setText(DBHelper.ADMIN_EMAIL);
        etPassword.setText(DBHelper.ADMIN_PASSWORD);

        btnTogglePassword.setOnClickListener(v -> togglePassword());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCustomer).setOnClickListener(v -> finish());
        findViewById(R.id.btnLogin).setOnClickListener(v -> attemptLogin());
    }

    private void togglePassword() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btnTogglePassword.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnTogglePassword.setColorFilter(getResources().getColor(R.color.textMuted));
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        User user = db.login(email, password);
        if (user == null) {
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!user.isAdmin) {
            Toast.makeText(this, "This account is not an admin account.", Toast.LENGTH_SHORT).show();
            return;
        }

        session.login(user.id, user.fullName, user.email, user.phone, user.address, true);

        Intent intent = new Intent(this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
