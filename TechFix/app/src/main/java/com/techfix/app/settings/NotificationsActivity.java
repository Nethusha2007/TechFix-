package com.techfix.app.settings;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.NotificationAdapter;
import com.techfix.app.data.DBHelper;
import com.techfix.app.data.Session;
import com.techfix.app.model.Appointment;
import com.techfix.app.model.NotificationItem;
import com.techfix.app.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private final List<NotificationItem> items = new ArrayList<>();
    private NotificationAdapter adapter;
    private DBHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        db = new DBHelper(this);
        session = new Session(this);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvNotifications);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new NotificationAdapter(items);
        rv.setAdapter(adapter);

        findViewById(R.id.btnMarkAll).setOnClickListener(v -> {
            for (NotificationItem n : items) n.unread = false;
            adapter.notifyDataSetChanged();
        });

        loadNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        items.clear();

        int userId = session.getUserId();
        if (userId > 0) {
            int seq = 1;
            
            for (Appointment a : db.getAppointmentsByUser(userId)) {
                items.add(buildForAppointment(seq++, a));
            }
            for (Payment p : db.getPaymentsByUser(userId)) {
                if ("Paid".equalsIgnoreCase(p.status)) {
                    items.add(new NotificationItem(seq++,
                            "Payment of " + p.amount + " received. Thank you!",
                            p.date, false, R.drawable.ic_receipt));
                }
            }
        }

        boolean hasItems = !items.isEmpty();
        findViewById(R.id.rvNotifications).setVisibility(hasItems ? View.VISIBLE : View.GONE);
        findViewById(R.id.emptyState).setVisibility(hasItems ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
    }
    
    private NotificationItem buildForAppointment(int id, Appointment a) {
        String ref = (a.refNo == null) ? "" : a.refNo;
        String device = (a.device == null) ? "your device" : a.device;
        String status = (a.status == null) ? "" : a.status;

        String title;
        int icon;
        boolean unread;
        switch (status) {
            case "In Progress":
                title = "Your repair " + ref + " is now in progress.";
                icon = R.drawable.ic_wrench;
                unread = true;
                break;
            case "Completed":
                title = "Your device " + ref + " is ready for collection.";
                icon = R.drawable.ic_check_circle;
                unread = false;
                break;
            case "Pending":
                title = "Booking confirmed for " + device + " (" + ref + ").";
                icon = R.drawable.ic_calendar;
                unread = true;
                break;
            default:
                title = device + " — " + status;
                icon = R.drawable.ic_info;
                unread = false;
                break;
        }
        return new NotificationItem(id, title, a.dateTime, unread, icon);
    }
}
