package com.techfix.app.model;

/** A repair service offered (e.g. Screen Replacement). */
public class ServiceItem {
    public int id;
    public String name;
    public String priceText;   // e.g. "From LKR 4,500"
    public int iconResId;      // R.drawable.*
    public String description;

    public ServiceItem(int id, String name, String priceText, int iconResId, String description) {
        this.id = id;
        this.name = name;
        this.priceText = priceText;
        this.iconResId = iconResId;
        this.description = description;
    }
}
