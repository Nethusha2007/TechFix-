package com.techfix.app.admin;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.adapter.AppointmentAdapter;
import com.techfix.app.auth.LoginActivity;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;
import com.techfix.app.model.Payment;
import com.techfix.app.util.AdminNavHelper;
import com.techfix.app.util.Money;
import com.techfix.app.view.PieChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin home. Shows live counts pulled from SQLite, a repairs-by-status pie chart
 * (drawn programmatically, no external chart library), and the most recent bookings.
 */
public class AdminDashboardActivity extends AppCompatActivity {

    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = new DBHelper(this);
        session = new Session(this);

        bindStats();
        buildChart();
        bindRecent();

        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());
        findViewById(R.id.btnViewAll).setOnClickListener(v ->
                startActivity(new Intent(this, AdminAppointmentsActivity.class)));

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        AdminNavHelper.setup(this, nav, R.id.nav_dashboard);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Counts, the chart and the recent list can all change after edits elsewhere.
        bindStats();
        buildChart();
        bindRecent();
    }

    private void bindStats() {
        int total = db.countAll();
        int inProgress = db.countByStatus("In Progress");
        int pending = db.countByStatus("Pending");

        // Real income: sum of every payment a customer has actually paid. The moment a
        // customer completes a payment (PaymentMethodActivity inserts a "Paid" row), it
        // shows up here on the next dashboard load.
        long revenue = 0;
        for (Payment p : db.getAllPayments()) {
            if ("Paid".equalsIgnoreCase(p.status)) revenue += Money.parse(p.amount);
        }

        setStat(R.id.statTotal, R.drawable.ic_appointments, R.drawable.bg_circle_primary_light,
                R.color.colorPrimary, String.valueOf(total), "Total Repairs");
        setStat(R.id.statProgress, R.drawable.ic_track, R.drawable.bg_circle_primary_light,
                R.color.info, String.valueOf(inProgress), "In Progress");
        setStat(R.id.statPending, R.drawable.ic_pending, R.drawable.bg_circle_warning_light,
                R.color.warning, String.valueOf(pending), "Pending");
        setStat(R.id.statRevenue, R.drawable.ic_revenue, R.drawable.bg_circle_success_light,
                R.color.success, Money.format(revenue), "Revenue");
    }

    /** Fills one stat-card include (scoped lookups since they share child ids). */
    private void setStat(int includeId, int iconRes, int bgRes, int tintColor,
                         String value, String label) {
        View card = findViewById(includeId);
        View iconBg = card.findViewById(R.id.statIconBg);
        ImageView icon = card.findViewById(R.id.statIcon);
        TextView tvValue = card.findViewById(R.id.statValue);
        TextView tvLabel = card.findViewById(R.id.statLabel);

        iconBg.setBackgroundResource(bgRes);
        icon.setImageResource(iconRes);
        icon.setColorFilter(getResources().getColor(tintColor));
        tvValue.setText(value);
        tvLabel.setText(label);
    }

    /** Draws a repairs-by-status donut chart into chartContainer using live DB counts. */
    private void buildChart() {
        LinearLayout container = findViewById(R.id.chartContainer);
        container.removeAllViews();

        int completed = db.countByStatus("Completed");
        int inProgress = db.countByStatus("In Progress");
        int pending = db.countByStatus("Pending");
        int total = db.countAll();
        int other = Math.max(0, total - completed - inProgress - pending);

        ((TextView) findViewById(R.id.tvChartTotal)).setText(String.valueOf(total));

        // Collect only the statuses that actually have repairs, so the pie + legend stay tidy.
        List<Float> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        addSlice(values, colors, labels, counts, completed, R.color.success, "Completed");
        addSlice(values, colors, labels, counts, inProgress, R.color.info, "In Progress");
        addSlice(values, colors, labels, counts, pending, R.color.warning, "Pending");
        addSlice(values, colors, labels, counts, other, R.color.textMuted, "Other");

        PieChartView pie = new PieChartView(this);
        pie.setLayoutParams(new LinearLayout.LayoutParams(dp(150), dp(150)));
        pie.setCenterText(String.valueOf(total), total == 1 ? "Repair" : "Repairs");

        if (values.isEmpty()) {
            // Nothing booked yet — show a single muted ring.
            pie.setData(new float[]{1f}, new int[]{getResources().getColor(R.color.divider)});
        } else {
            float[] v = new float[values.size()];
            int[] c = new int[colors.size()];
            for (int i = 0; i < values.size(); i++) {
                v[i] = values.get(i);
                c[i] = getResources().getColor(colors.get(i));
            }
            pie.setData(v, c);
        }
        container.addView(pie);

        LinearLayout legend = new LinearLayout(this);
        legend.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams legendParams =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        legendParams.setMarginStart(dp(20));
        legend.setLayoutParams(legendParams);

        if (labels.isEmpty()) {
            addLegendRow(legend, getResources().getColor(R.color.textMuted), "No repairs yet", -1);
        } else {
            for (int i = 0; i < labels.size(); i++) {
                addLegendRow(legend, getResources().getColor(colors.get(i)), labels.get(i), counts.get(i));
            }
        }
        container.addView(legend);
    }

    /** Adds a status to the pie/legend collections only when it has at least one repair. */
    private void addSlice(List<Float> values, List<Integer> colors, List<String> labels,
                          List<Integer> counts, int count, int colorRes, String label) {
        if (count <= 0) return;
        values.add((float) count);
        colors.add(colorRes);
        labels.add(label);
        counts.add(count);
    }

    /** Builds one legend row: a coloured dot, the status label, and its count. */
    private void addLegendRow(LinearLayout parent, int color, String label, int count) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = dp(12);
        row.setLayoutParams(rowParams);

        View dot = new View(this);
        dot.setLayoutParams(new LinearLayout.LayoutParams(dp(10), dp(10)));
        GradientDrawable dotBg = new GradientDrawable();
        dotBg.setShape(GradientDrawable.OVAL);
        dotBg.setColor(color);
        dot.setBackground(dotBg);

        TextView tvLabel = new TextView(this);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        labelParams.setMarginStart(dp(8));
        tvLabel.setLayoutParams(labelParams);
        tvLabel.setText(label);
        tvLabel.setTextSize(12f);
        tvLabel.setTextColor(getResources().getColor(R.color.textSecondary));

        row.addView(dot);
        row.addView(tvLabel);

        if (count >= 0) {
            TextView tvCount = new TextView(this);
            tvCount.setText(String.valueOf(count));
            tvCount.setTextSize(13f);
            tvCount.setTextColor(getResources().getColor(R.color.textPrimary));
            tvCount.setTypeface(tvCount.getTypeface(), android.graphics.Typeface.BOLD);
            row.addView(tvCount);
        }

        parent.addView(row);
    }

    private void bindRecent() {
        List<Appointment> all = db.getAllAppointments();
        // Show the four most recent (query already orders newest first).
        List<Appointment> recent = all.subList(0, Math.min(4, all.size()));

        RecyclerView rv = findViewById(R.id.rvRecent);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new AppointmentAdapter(recent, a -> {
            Intent i = new Intent(this, AdminAppointmentDetailsActivity.class);
            i.putExtra("appointment_id", a.id);
            startActivity(i);
        }));
    }

    private void logout() {
        session.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
