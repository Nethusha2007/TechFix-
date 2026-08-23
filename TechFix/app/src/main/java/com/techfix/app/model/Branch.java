package com.techfix.app.model;

/** A TechFix branch location. */
public class Branch {
    public int id;
    public String name;
    public String address;
    public String city;
    public String distance;   // e.g. "2.4 km"
    public String hours;      // e.g. "Open · Closes 8:00 PM"
    public String phone;
    public double lat;        // latitude  (0 = unknown)
    public double lng;        // longitude (0 = unknown)

    public Branch(int id, String name, String address, String city, String distance, String hours, String phone) {
        this(id, name, address, city, distance, hours, phone, 0, 0);
    }

    public Branch(int id, String name, String address, String city, String distance,
                  String hours, String phone, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.distance = distance;
        this.hours = hours;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
    }
}
