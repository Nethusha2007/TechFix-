package com.techfix.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;
import com.techfix.app.model.Booking;
import com.techfix.app.payment.PaymentSuccessActivity;

import java.util.Locale;

/** Booking step 8 of 8 — review the summary and confirm (writes to the database). */
public class BookStep8ConfirmActivity extends AppCompatActivity {

    private Booking booking;
    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step8);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();
        db = new DBHelper(this);
        session = new Session(this);

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 8 of 8");
        ((android.widget.ProgressBar) findViewById(R.id.stepProgress)).setProgress(8);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("Review & confirm");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Please check everything looks right before you confirm.");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String photos = booking.imageCount() == 0 ? "None"
                : booking.imageCount() + (booking.imageCount() == 1 ? " photo" : " photos");

        setRow(R.id.rowDevice, "Device", booking.deviceBrand);
        setRow(R.id.rowService, "Service", booking.service);
        setRow(R.id.rowBranch, "Branch", booking.branch);
        setRow(R.id.rowDateTime, "Date & Time", booking.date + " · " + booking.time);
        setRow(R.id.rowPhotos, "Photos", photos);

        ((TextView) findViewById(R.id.tvProblem)).setText(
                booking.problem.isEmpty() ? "—" : booking.problem);

        String cost = formatCost(booking.priceText);
        booking.estimatedCost = cost;
        ((TextView) findViewById(R.id.tvCost)).setText(cost);

        findViewById(R.id.btnConfirm).setOnClickListener(v -> confirm());
    }

    private void confirm() {
        String ref = String.format(Locale.US, "#AAP-%d", 10027 + db.countAll());
        String dateTime = booking.date + " - " + booking.time;
        String customer = session.getName();

        Appointment a = new Appointment(0, ref, booking.deviceBrand, booking.service,
                booking.branch, dateTime, "Pending", booking.estimatedCost,
                customer, "", booking.problem, 0);
        a.userId = session.getUserId();
        a.imagePaths = booking.imagePathsJoined();
        long id = db.insertAppointment(a);

        Intent i = new Intent(this, PaymentSuccessActivity.class);
        i.putExtra("success_title", "Booking Confirmed!");
        i.putExtra("success_message",
                "Your repair has been booked. We'll keep you updated at every stage.");
        i.putExtra("success_ref", ref);
        i.putExtra("appointment_id", (int) id);
        i.putExtra("primary_action", "appointment");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void setRow(int rowId, String label, String value) {
        View row = findViewById(rowId);
        ((TextView) row.findViewById(R.id.tvLabel)).setText(label);
        ((TextView) row.findViewById(R.id.tvValue)).setText(value.isEmpty() ? "—" : value);
    }

    /** Turns a price label such as "From LKR 4,500" into "LKR 4,500.00". */
    private String formatCost(String priceText) {
        if (priceText == null) return "LKR 0.00";
        String digits = priceText.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return "LKR 0.00";
        try {
            long amount = Long.parseLong(digits);
            return String.format(Locale.US, "LKR %,d.00", amount);
        } catch (NumberFormatException e) {
            return "LKR 0.00";
        }
    }
}
