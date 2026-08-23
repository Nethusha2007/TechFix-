package com.techfix.app.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.model.Appointment;
import com.techfix.app.payment.PaymentSummaryActivity;
import com.techfix.app.util.ImageStore;
import com.techfix.app.util.StatusUtil;

import java.util.List;

public class RepairDetailsActivity extends AppCompatActivity {

    private DBHelper db;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);

        db = new DBHelper(this);
        appointmentId = getIntent().getIntExtra("appointment_id", -1);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnTrack).setOnClickListener(v -> {
            Intent i = new Intent(this, RepairTrackingActivity.class);
            i.putExtra("appointment_id", appointmentId);
            startActivity(i);
        });

        findViewById(R.id.btnPay).setOnClickListener(v -> {
            Intent i = new Intent(this, PaymentSummaryActivity.class);
            i.putExtra("appointment_id", appointmentId);
            startActivity(i);
        });

        findViewById(R.id.cardPhotos).setOnClickListener(v ->
                startActivity(new Intent(this, GalleryActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    private void bind() {
        Appointment a = db.getAppointmentById(appointmentId);
        if (a == null) {
            finish();
            return;
        }

        ((ImageView) findViewById(R.id.ivDeviceIcon))
                .setImageResource(AppointmentAdapter.iconForDevice(a.device));
        ((TextView) findViewById(R.id.tvDevice)).setText(a.device);
        ((TextView) findViewById(R.id.tvService)).setText(a.service);
        StatusUtil.applyStatus(findViewById(R.id.tvStatus), a.status);

        String tech = (a.technicianName == null || a.technicianName.trim().isEmpty())
                ? "Not assigned yet" : a.technicianName;

        setRow(R.id.rowRef, "Reference No.", a.refNo);
        setRow(R.id.rowBranch, "Branch", a.branch);
        setRow(R.id.rowDate, "Date & Time", a.dateTime);
        setRow(R.id.rowCustomer, "Customer", a.customerName);
        setRow(R.id.rowTechnician, "Technician", tech);
        setRow(R.id.rowCost, "Estimated Cost", a.estimatedCost);

        String desc = (a.description == null || a.description.trim().isEmpty())
                ? "No additional description was provided for this repair."
                : a.description;
        ((TextView) findViewById(R.id.tvDescription)).setText(desc);

        bindPhotos(a.imagePaths);

        boolean payable = a.status.equalsIgnoreCase("Completed")
                || a.status.equalsIgnoreCase("Ready")
                || a.status.equalsIgnoreCase("Ready for Collection");
        findViewById(R.id.btnPay).setVisibility(payable ? View.VISIBLE : View.GONE);
    }

    private void bindPhotos(String joined) {
        LinearLayout container = findViewById(R.id.photosContainer);
        View strip = findViewById(R.id.photosStrip);
        container.removeAllViews();

        List<String> paths = ImageStore.split(joined);
        if (paths.isEmpty()) {
            strip.setVisibility(View.GONE);
            return;
        }
        strip.setVisibility(View.VISIBLE);

        int size = dp(84);
        int margin = dp(10);
        for (String p : paths) {
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
            lp.setMarginEnd(margin);
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundResource(R.drawable.bg_input);
            Bitmap bmp = ImageStore.decodeSampled(p, dp(112));
            if (bmp != null) iv.setImageBitmap(bmp);
            else iv.setImageResource(R.drawable.ic_gallery);
            container.addView(iv);
        }
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    private void setRow(int rowId, String label, String value) {
        View row = findViewById(rowId);
        ((TextView) row.findViewById(R.id.tvLabel)).setText(label);
        ((TextView) row.findViewById(R.id.tvValue)).setText(value);
    }
}
