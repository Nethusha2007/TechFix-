package com.techfix.app.model;

/** A notification list item. */
public class NotificationItem {
    public int id;
    public String title;    // notification message
    public String time;     // e.g. "10 min ago"
    public boolean unread;
    public int iconResId;   // R.drawable.*

    public NotificationItem(int id, String title, String time, boolean unread, int iconResId) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.unread = unread;
        this.iconResId = iconResId;
    }
}
