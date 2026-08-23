package com.techfix.app.admin;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techfix.app.R;
import com.techfix.app.data.DBHelper;
import com.techfix.app.model.Payment;
import com.techfix.app.util.AdminNavHelper;
import com.techfix.app.util.Money;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Admin reports: period summary cards, a revenue-by-month bar chart, and a monthly
 * breakdown table. Everything here is computed live from SQLite — total repairs come
 * from the appointments table and all revenue comes from real customer payments — so
 * the screen starts at zero on a fresh install and grows as customers book and pay.
 */
public class AdminReportsActivity extends AppCompatActivity {

    private DBHelper db;

    /** One month's aggregated figures, built from real paid payments. */
    private static class MonthRow {
        final String label;   // e.g. "Aug 2026"
        int repairs;          // number of paid repairs that month
        long revenue;         // total received that month (cents/whole units per Money)

        MonthRow(String label) {
            this.label = label;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reports);

        db = new DBHelper(this);
        bindReport();

        BottomNavigationView nav = findViewById(R.id.bottomNav);
        AdminNavHelper.setup(this, nav, R.id.nav_reports);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh whenever the screen comes back into view, so a payment made elsewhere
        // is reflected here immediately.
        bindReport();
    }

    /** Pulls live figures from the DB and (re)draws the summary, chart and table. */
    private void bindReport() {
        int totalRepairs = db.countAll();

        List<MonthRow> months = new ArrayList<>();
        long totalRevenue = aggregatePayments(months);

        setStat(R.id.statRepairs, R.drawable.ic_appointments, R.drawable.bg_circle_primary_light,
                R.color.colorPrimary, String.valueOf(totalRepairs), "Total Repairs");
        setStat(R.id.statRevenue, R.drawable.ic_revenue, R.drawable.bg_circle_success_light,
                R.color.success, Money.format(totalRevenue), "Total Revenue");

        buildChart(months);
        buildTable(months);
    }

    /**
     * Groups every <b>Paid</b> payment by calendar month (parsed from its "dd MMM yyyy"
     * date), filling {@code out} with one {@link MonthRow} per month in chronological
     * order. Returns the grand total revenue.
     */
    private long aggregatePayments(List<MonthRow> out) {
        SimpleDateFormat parser = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat keyFmt = new SimpleDateFormat("yyyy-MM", Locale.getDefault());   // sortable
        SimpleDateFormat labelFmt = new SimpleDateFormat("MMM yyyy", Locale.getDefault()); // display

        // Sorted by key so months come out oldest-first for the chart/table.
        Map<String, MonthRow> byMonth = new TreeMap<>();
        // Any payment whose date we can't parse is bucketed here and shown last.
        Map<String, MonthRow> unknown = new LinkedHashMap<>();

        long total = 0;
        for (Payment p : db.getAllPayments()) {
            if (!"Paid".equalsIgnoreCase(p.status)) continue;

            long amount = Money.parse(p.amount);
            total += amount;

            String key;
            String label;
            try {
                Date d = parser.parse(p.date);
                key = keyFmt.format(d);
                label = labelFmt.format(d);
            } catch (Exception e) {
                key = null;
                label = (p.date == null || p.date.isEmpty()) ? "Undated" : p.date;
            }

            Map<String, MonthRow> target = (key != null) ? byMonth : unknown;
            String mapKey = (key != null) ? key : label;
            MonthRow row = target.get(mapKey);
            if (row == null) {
                row = new MonthRow(label);
                target.put(mapKey, row);
            }
            row.repairs += 1;
            row.revenue += amount;
        }

        out.addAll(byMonth.values());
        out.addAll(unknown.values());
        return total;
    }

    private void setStat(int includeId, int iconRes, int bgRes, int tintColor,
                         String value, String label) {
        View card = findViewById(includeId);
        card.findViewById(R.id.statIconBg).setBackgroundResource(bgRes);
        ImageView icon = card.findViewById(R.id.statIcon);
        icon.setImageResource(iconRes);
        icon.setColorFilter(getResources().getColor(tintColor));
        ((TextView) card.findViewById(R.id.statValue)).setText(value);
        ((TextView) card.findViewById(R.id.statLabel)).setText(label);
    }

    /** Bar chart of monthly revenue, tallest month highlighted. Shows a message when empty. */
    private void buildChart(List<MonthRow> months) {
        LinearLayout container = findViewById(R.id.chartContainer);
        container.removeAllViews();

        if (months.isEmpty()) {
            container.addView(emptyMessage("No revenue recorded yet"));
            return;
        }

        long max = 1;
        for (MonthRow m : months) {
            if (m.revenue > max) max = m.revenue;
        }

        int maxBarPx = dp(96);
        int minBarPx = dp(6);

        for (MonthRow m : months) {
            LinearLayout column = new LinearLayout(this);
            column.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            column.setOrientation(LinearLayout.VERTICAL);
            column.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            int barHeight = Math.max(minBarPx, Math.round(maxBarPx * (m.revenue / (float) max)));

            View bar = new View(this);
            bar.setLayoutParams(new LinearLayout.LayoutParams(dp(18), barHeight));
            bar.setBackgroundResource(m.revenue == max ? R.drawable.bg_bar : R.drawable.bg_bar_track);

            TextView label = new TextView(this);
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            labelParams.topMargin = dp(8);
            label.setLayoutParams(labelParams);
            label.setText(shortMonth(m.label));
            label.setTextSize(11f);
            label.setTextColor(getResources().getColor(R.color.textSecondary));

            column.addView(bar);
            column.addView(label);
            container.addView(column);
        }
    }

    /** Inflates one item_report_row per month into the breakdown table. */
    private void buildTable(List<MonthRow> months) {
        LinearLayout container = findViewById(R.id.tableContainer);
        container.removeAllViews();

        if (months.isEmpty()) {
            TextView empty = emptyMessage("No transactions yet");
            empty.setPadding(0, dp(18), 0, dp(18));
            container.addView(empty);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < months.size(); i++) {
            MonthRow m = months.get(i);
            View v = inflater.inflate(R.layout.item_report_row, container, false);
            ((TextView) v.findViewById(R.id.tvMonth)).setText(m.label);
            ((TextView) v.findViewById(R.id.tvRepairs)).setText(String.valueOf(m.repairs));
            ((TextView) v.findViewById(R.id.tvRevenue)).setText(Money.format(m.revenue));
            container.addView(v);

            if (i < months.size() - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(getResources().getColor(R.color.divider));
                container.addView(divider);
            }
        }
    }

    /** A centred, muted placeholder used when there's no data to chart or tabulate. */
    private TextView emptyMessage(String text) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setTextSize(13f);
        tv.setTextColor(getResources().getColor(R.color.textMuted));
        return tv;
    }

    private static String shortMonth(String month) {
        if (month == null || month.length() < 3) return month;
        return month.substring(0, 3);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
