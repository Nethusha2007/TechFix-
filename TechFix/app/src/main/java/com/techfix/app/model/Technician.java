package com.techfix.app.model;

/** A repair technician (used in the admin app). */
public class Technician {
    public int id;
    public String name;
    public String specialty;   // e.g. "Mobile Repair Specialist"
    public String branch;      // e.g. "Colombo"
    public boolean available;  // true = Available, false = Busy

    public Technician(int id, String name, String specialty, String branch, boolean available) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.branch = branch;
        this.available = available;
    }
}
