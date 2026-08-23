package com.techfix.app.model;

/** A device category / brand tile (e.g. Mobile Phone, Samsung). */
public class DeviceCategory {
    public int id;
    public String name;
    public int iconResId;   // R.drawable.*

    public DeviceCategory(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }
}
