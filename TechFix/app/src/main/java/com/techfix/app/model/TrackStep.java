package com.techfix.app.model;

/** One step in the repair-tracking timeline. */
public class TrackStep {
    public static final int DONE = 0;
    public static final int CURRENT = 1;
    public static final int PENDING = 2;

    public String title;   // e.g. "Repair In Progress"
    public String time;    // e.g. "18 May 2026 - 03:30 PM" ("" if not reached)
    public int state;      // DONE | CURRENT | PENDING

    public TrackStep(String title, String time, int state) {
        this.title = title;
        this.time = time;
        this.state = state;
    }
}
