package com.techfix.app.auth;

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
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.techfix.app.R;
import com.techfix.app.admin.AdminDashboardActivity;
import com.techfix.app.admin.AdminLoginActivity;
import com.techfix.app.customer.HomeActivity;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ImageView btnTogglePassword;
    private boolean passwordVisible = false;
    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);
        session = new Session(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        etEmail.setText(DBHelper.DEMO_EMAIL);
        etPassword.setText(DBHelper.DEMO_PASSWORD);

        btnTogglePassword.setOnClickListener(v -> togglePassword());
        findViewById(R.id.btnLogin).setOnClickListener(v -> attemptLogin());

        findViewById(R.id.btnForgot).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
        findViewById(R.id.btnRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.btnAdmin).setOnClickListener(v ->
                startActivity(new Intent(this, AdminLoginActivity.class)));

        MaterialButton btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In is not available in this demo.",
                        Toast.LENGTH_SHORT).show());
    }

    private void togglePassword() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btnTogglePassword.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnTogglePassword.setColorFilter(ContextCompat.getColor(this, R.color.textMuted));
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

        session.login(user.id, user.fullName, user.email, user.phone, user.address, user.isAdmin);

        Intent intent = user.isAdmin
                ? new Intent(this, AdminDashboardActivity.class)
                : new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
